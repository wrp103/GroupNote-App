package com.billpotter.groupnote;

// activity to display/create users profile

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity implements Database.AsyncResponse {

    private static final String TAG = "ProfileActivity";
    UserSession session; // current session
    String newProfile = "true"; // setting used in the PHP script to distinguish between create or update
    HashMap<String, String> user; // list of users
    int maxLength = 30; // max length of field
    String link = "profile.php";  // php file to interact with database

    // activity elements
    EditText txtFirstname, txtLastname, txtTelephone;
    String sUserId, sFirstname, sLastname, sTelephone;
    Button btnSave;
    String errorMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: STARTING PROFILE ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtFirstname = findViewById(R.id.txtFirstname);
        txtLastname = findViewById(R.id.txtLastname);
        txtTelephone = findViewById(R.id.txtTelephone);
        btnSave = findViewById(R.id.btnRegister);
        session = new UserSession(getApplicationContext());

        if (session.isUserProfileCreated()) {

            Log.d(TAG, "onCreate: PROFILE ALREADY CREATED SETTING MAKE NEW PROFILE TO FALSE");
            newProfile = "false";
            user = session.getUserDetails();
            sUserId = user.get(UserSession.USER_USERID);
            sFirstname = user.get(UserSession.USER_FIRSTNAME);
            sLastname = user.get(UserSession.USER_LASTNAME);
            sTelephone = user.get(UserSession.USER_TELEPHONE);
            txtFirstname.setText(sFirstname);
            txtLastname.setText(sLastname);
            txtTelephone.setText(sTelephone);
        }
        Log.d(TAG, "onCreate: FINISHED SETTING UP PROFILE ACTIVITY");
    }

    // save button action, updates the database with new profile settings
    public void saveProfile(View v) {


        Log.d(TAG, "profile: SAVE BUTTON CLICKED");

        sFirstname = txtFirstname.getText().toString();
        sLastname = txtLastname.getText().toString();
        sTelephone = txtTelephone.getText().toString();

        if (validateNameEntry(sFirstname) && validateNameEntry(sLastname) && validateTelephoneEntry(sTelephone)) {
            Log.d(TAG, "login: ATTEMPTING TO SAVE DETAILS TO DATABASE");
            new Database(this, this, this, link, buildQuery()).execute();
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    // validation for entering the persons name
    public boolean validateNameEntry(String name) {

        Pattern pattern = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        boolean check = matcher.find();

        if (name.equals("")) {
            errorMsg = "Entry cannot be empty";
            return false;
        } else if (name.length() > maxLength) {
            errorMsg = "Entry cannot be longer than 30 characters";
            return false;
        } else if (check) {
            errorMsg = "Only use characters A-Z";
            return false;
        } else {
            return true;
        }
    }

    // validation for the telephone entry
    public boolean validateTelephoneEntry(String number) {

        if (number.matches("\\d{11}")) return true;
        else if(number.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        else if(number.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        else if(number.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        else {
            errorMsg = "Incorrect number";
            return false;
        }
    }

    // write query to create or update the profile
    public String buildQuery() {

        HashMap<String, String> user = session.getUserDetails();
        Log.d(TAG, "buildQuery: BUILDING QUERY");
        sUserId = user.get(UserSession.USER_USERID);
        sFirstname = txtFirstname.getText().toString();
        sLastname = txtLastname.getText().toString();
        sTelephone = txtTelephone.getText().toString();
        Log.d(TAG, "buildQuery: USER ID = " + user.get(UserSession.USER_USERID));
        Log.d(TAG, "buildQuery: FIRST NAME = " + txtFirstname.getText().toString());
        Log.d(TAG, "buildQuery: LAST NAME = " + txtLastname.getText().toString());
        Log.d(TAG, "buildQuery: TELEPHONE = " + txtTelephone.getText().toString());
        Log.d(TAG, "buildQuery: QUERY DATA USED = " + newProfile + " " + sUserId + " " + sFirstname + " " + sLastname + " " + sTelephone);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("newProfile", newProfile)
                .appendQueryParameter("user_id", sUserId)
                .appendQueryParameter("firstname", sFirstname)
                .appendQueryParameter("lastname", sLastname)
                .appendQueryParameter("telephone", sTelephone);

        return builder.build().getEncodedQuery();
    }

    // retrieve and process result of query
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: Starts");

        if (output.get(0).toString().equals("created")) {
            Log.d(TAG, "processFinish: PROFILE CREATED");
        } else if (output.get(0).toString().equals("saved")) {
            Log.d(TAG, "processFinish: PROFILE SAVED");
        } else {
            Log.d(TAG, "processFinish: ERROR");
        }

        session.createUserProfileSession(
                sFirstname,
                sLastname,
                sTelephone);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

        Log.d(TAG, "processFinish: Ends");

    }

    // load menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.menuLogout:
                session.logoutUser();
                break;
            case R.id.menuCreateGroup:
                Intent createGroup = new Intent(getApplicationContext(), CreateGroupActivity.class);
                startActivity(createGroup);
                finish();
                break;
            case R.id.menuMyGroups:
                Intent groupList = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(groupList);
                finish();
                break;
            case R.id.menuManageProfile:
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profile);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // action for back button
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}