package com.billpotter.groupnote;

// activity for the user logging in

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements Database.AsyncResponse {

    private static final String TAG = "LoginActivity";
    String link = "login.php"; // php file to interact with database
    UserSession session; // current session

    // activity elements
    EditText txtUsername, txtPassword;
    String susername, spassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: STARTING LOGIN ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new UserSession(getApplicationContext());
        txtUsername = findViewById(R.id.txtUsernameLogin);
        txtPassword = findViewById(R.id.txtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
    }

    // action for login button pressed, runs the query to confirm login details
    public void login(View v) {

        Log.d(TAG, "login: CLICKED LOGIN BUTTON");
        susername = txtUsername.getText().toString();
        spassword = txtPassword.getText().toString();
        if (susername.equals("") || spassword.equals("")) {
            Toast.makeText(this, "Cannot be empty", Toast.LENGTH_SHORT).show();
        } else {

            Log.d(TAG, "login: CHECKING LOGIN DETAILS FROM DATABASE");
            new Database(this, this, this, link, buildQuery()).execute();
        }
    }

    // create account button pressed loads register activity
    public void createAccount(View v) {

        Log.d(TAG, "createAccount: CLICKED CREATE ACCOUNT BUTTON");
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    // write the query to check the username and password are valid in the database
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");
        susername = txtUsername.getText().toString();
        spassword = txtPassword.getText().toString();
        Log.d("text",susername+spassword);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", susername)
                .appendQueryParameter("password", spassword);
        return builder.build().getEncodedQuery();
    }

    // process result of database query, logging the user in or display an error message
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: RETRIEVED DATA FROM DATABASE");
        if (!output.get(0).toString().equals("false")) {
            JSONObject userData = new JSONObject(output.get(1).toString());
            boolean profileSet = false;
            String profileCheck = output.get(2).toString();
            JSONObject profileData = null;
            if (profileCheck.equals("1")) {
                profileSet = true;
                profileData = new JSONObject(output.get(3).toString());
            }
            processLogin(userData, profileSet, profileData);
        } else {
            Toast.makeText(this, "Incorrect login details", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "processFinish: INCORRECT LOGIN DETAILS");
        }
        Log.d(TAG, "processFinish: FINISHED READING DATA");
    }

    // action to log the user into the app, depending on account information the user will be sent to,
    // either the profile activity or the main activity
    public void processLogin(JSONObject userData, boolean profileSet, JSONObject profileData) throws JSONException {

        Log.d(TAG, "processLogin: SETTING SESSION VARIABLES");

                if (profileSet) {
                    session.createUserLoginSession(
                            String.valueOf(userData.getInt("user_id")),
                            userData.getString("username"),
                            userData.getString("email"),
                            profileData.getString("firstname"),
                            profileData.getString("lastname"),
                            profileData.getString("telephone"));
                } else {
                    session.createUserLoginSession(
                            String.valueOf(userData.getInt("user_id")),
                            userData.getString("username"),
                            userData.getString("email"));

                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                Log.d(TAG, "processLogin: FINISHED SETTING VARIABLES");
    }
}