package com.billpotter.groupnote;

// Activity page for adding users to a group //

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUsersActivity extends AppCompatActivity implements Database.AsyncResponse {

    private static final String TAG = "AddUsers";
    UserSession session; // current session
    HashMap<String, String> user; // list of users
    String link = "group.php"; // php file to interact with database
    int maxLength = 15; // max username length

    // elements of activity
    Button btnAdd;
    TextView txtGroupName;
    EditText txtAddUsername;
    String errorMsg;
    String sAddUsername, setting, sGroupId, sUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: STARTS");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        txtGroupName = null;
        btnAdd = findViewById(R.id.btnAddUser);
        txtAddUsername = findViewById(R.id.txtUsernameAdd);
        session = new UserSession(getApplicationContext());
        user = session.getUserDetails();
        Log.d(TAG, "onCreate: ENDS");
    }

    // add user button action (query the database and add username if valid)
    public void addUser(View v) {

        Log.d(TAG, "CREATE BUTTON CLICKED");
        sAddUsername = txtAddUsername.getText().toString();
        if (validateTextEntry(sAddUsername)) {
            Log.d(TAG, "addUser: ATTEMPTING TO SAVE DETAILS TO DATABASE");
            new Database(this, this, this, link, buildQuery()).execute();
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    // username validation
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

    // build the query to send to the database
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");
        sUserId = user.get(UserSession.USER_USERID);
        sAddUsername = txtAddUsername.getText().toString();
        sGroupId = user.get(UserSession.USER_GROUPID);
        setting = "addUser";
        Log.d(TAG, "buildQuery: CURRENT USER ID = " + user.get(UserSession.USER_USERID));
        Log.d(TAG, "buildQuery: USER NAME ENTERED = " + sAddUsername);
        Log.d(TAG, "buildQuery: CURRENT GROUP ID = " + user.get(UserSession.USER_GROUPID));
        Log.d(TAG, "buildQuery: QUERY DATA USED = " + setting + " " + sUserId + " " + sAddUsername + " " + sGroupId);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("setting", setting)
                .appendQueryParameter("group_id", sGroupId)
                .appendQueryParameter("add_username", sAddUsername)
                .appendQueryParameter("new_user_id", sUserId);

        return builder.build().getEncodedQuery();
    }

    // action event after query is complete
    // either show error or success message
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: Starts");
        String feedback = output.get(0).toString();

        if (feedback.equals("user already in group") || feedback.equals("unknown username")) {
            errorMsg = output.get(0).toString();
            Log.d(TAG, "processFinish: ERROR : " + errorMsg);
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "processFinish: SUCCESS : "  + output.get(0).toString());
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

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



    // action when back button pressed on mobile
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}