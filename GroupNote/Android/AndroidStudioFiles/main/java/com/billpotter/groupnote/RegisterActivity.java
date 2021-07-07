package com.billpotter.groupnote;

// activity to register as a new user

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements Database.AsyncResponse {

    private static final String TAG = "RegisterActivity";
    UserSession session; // current session
    String link = "register.php"; // php file to interact with database
    int maxLength = 15; // max length of username

    // activity elements
    EditText txtUsername, txtPassword, txtEmail;
    String susername, spassword, semail;
    Button btnRegister;
    String errorMsg = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: STARTING REGISTER ACTIVITY");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new UserSession(getApplicationContext());
        txtUsername = findViewById(R.id.txtUsernameRegister);
        txtPassword = findViewById(R.id.txtPasswordRegister);
        txtEmail = findViewById(R.id.txtEmailRegister);
        btnRegister = findViewById(R.id.btnRegister);
    }

    // action for register button, attempts to create a new account based on field entry
    public void register(View v) {

        Log.d(TAG, "login: CLICKED REGISTER BUTTON");
        Log.d(TAG, "login: CLICKED LOGIN BUTTON");
        susername = txtUsername.getText().toString();
        spassword = txtPassword.getText().toString();
        semail = txtEmail.getText().toString();
        if (validateTextEntry(susername) && validateTextEntry(spassword) && validateEmailEntry(semail)) {
            Log.d(TAG, "login: ATTEMPTING TO REGISTER DETAILS TO DATABASE");
            new Database(this, this, this, link, buildQuery()).execute();

        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    // validate the field entry
    public boolean validateTextEntry(String text) {

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean check = matcher.find();

        if (text.equals("")) {
            errorMsg = "Entry cannot be empty";
            return false;
        } else if (text.length() > maxLength) {
            errorMsg = "Entry cannot be longer than 15 characters";
            return false;
        } else if (check) {
            errorMsg = "Only use characters a-z, A-Z and 0-9";
            return false;
        } else {
            return true;
        }
    }

    // validate email entry
    public boolean validateEmailEntry(String email) {
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(email.isEmpty()) {
            errorMsg = "Empty email field";
            return false;
        }else {
            if (email.trim().matches(pattern)) {
                return true;
            } else {
                errorMsg = "Invalid email";
                return false;
            }
        }
    }

    // write query for inserting a new account
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");
        susername = txtUsername.getText().toString();
        spassword = txtPassword.getText().toString();
        semail = txtEmail.getText().toString();
        Log.d("entered information",susername+" " + spassword +" " + semail);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", susername)
                .appendQueryParameter("password", spassword)
                .appendQueryParameter("email", semail);

        return builder.build().getEncodedQuery();
    }

    // retrieve result of insert user, display error message if required
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: RETRIEVED RESPONSE FROM PHP SCRIPT");
        if (output.get(0).toString().equals("false")) {
            Log.d(TAG, "processFinish: USERNAME ALREADY EXISTS");
        } else if (output.get(0).toString().equals("0")) {
            Log.d(TAG, "processFinish: FAILED FOR SOME REASON");
        }
        else {
            Log.d(TAG, "processFinish: SUCCESS, CREATING ACCOUNT");
            session.createUserLoginSession(
                    output.get(0).toString(),
                    susername,
                    semail);
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        Log.d(TAG, "processFinish: FINISHED READING DATA");
    }

    // back button action
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

}