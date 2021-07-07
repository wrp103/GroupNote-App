package com.billpotter.groupnote;

// Activity page for creating a group //

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

public class CreateGroupActivity extends AppCompatActivity implements Database.AsyncResponse {

    private static final String TAG = "CreateGroup";
    int maxLength = 45; // max group name length
    UserSession session; // current session
    String link = "group.php"; // php file to interact with database

    // activity elements
    Button btnCreate;
    EditText txtGroupName;
    String errorMsg = "";
    String sGroupName, sUserId, setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate: STARTING CREATE GROUP ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        txtGroupName = findViewById(R.id.txtGroupTitle);
        btnCreate = findViewById(R.id.btnCreateGroup);
        session = new UserSession(getApplicationContext());
        Log.d(TAG, "onCreate: FINISHED CREATING GROUP ACTIVITY");
    }

    // action for clicking create button (query the database and add a new group)
    public void createGroup(View v) {

        Log.d(TAG, "CREATE BUTTON CLICKED");
        sGroupName = txtGroupName.getText().toString();
        if (validateTextEntry(sGroupName)) {
            Log.d(TAG, "createGroup: ATTEMPTING TO SAVE DETAILS TO DATABASE");
            new Database(this, this, this, link, buildQuery()).execute();
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    // validation for group name entry
    public boolean validateTextEntry(String text) {

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean check = matcher.find();

        if (text.equals("")) {
            errorMsg = "Entry cannot be empty";
            return false;
        } else if (text.length() > maxLength) {
            errorMsg = "Entry cannot be longer than 45 characters";
            return false;
        } else if (check) {
            errorMsg = "Only use characters a-z, A-Z and 0-9";
            return false;
        } else {
            return true;
        }
    }

    // write the query to be send to the database
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");
        HashMap<String, String> user = session.getUserDetails();
        sUserId = user.get(UserSession.USER_USERID);
        sGroupName = txtGroupName.getText().toString();
        setting = "create";
        Log.d(TAG, "buildQuery: USER ID = " + user.get(UserSession.USER_USERID));
        Log.d(TAG, "buildQuery: GROUP NAME = " + txtGroupName.getText().toString());
        Log.d(TAG, "buildQuery: QUERY DATA USED = " + setting + " " + sUserId + " " + sGroupName);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("setting", setting)
                .appendQueryParameter("user_id", sUserId)
                .appendQueryParameter("group_name", sGroupName);

        return builder.build().getEncodedQuery();
    }

    // process the retrieved information from the database, showing an error or success message to user
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: Starts");
        String feedback = output.get(0).toString();
        Log.d(TAG, "processFinish: FEEDBACK FROM OUTPUT = " + feedback);
        session.setUserLastGroupId(feedback);
        if (Integer.parseInt(feedback) > 0) {
            Log.d(TAG, "processFinish: CREATED GROUP WITH ID OF " + feedback);
            Intent i = new Intent(getApplicationContext(), AddUsersActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        } else {
            Log.d(TAG, "processFinish: ERROR : EMPTY ID STRING RETURNED :  " + feedback);
        }
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


    // action for when back button is pressed
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}