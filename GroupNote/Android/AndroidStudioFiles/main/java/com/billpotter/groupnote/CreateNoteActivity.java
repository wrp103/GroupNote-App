package com.billpotter.groupnote;

// activity for creating a new note

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

import java.util.HashMap;

public class CreateNoteActivity extends AppCompatActivity implements Database.AsyncResponse  {

    private static final String TAG = "CreateNoteActivity";
    UserSession session; // current session
    HashMap<String, String> user; // user list
    String link = "note.php"; // php file to interact with database

    // activity elements
    Button btnSave;
    EditText txtNoteTitle, txtNoteText;
    String sNoteId, sUserId, setting, sNoteTitle, sNoteText, sGroupId;
    String errorMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        txtNoteTitle = findViewById(R.id.txtNoteTitle);
        txtNoteText = findViewById(R.id.txtNoteBody);
        btnSave = findViewById(R.id.btnSaveNote);
        session = new UserSession(getApplicationContext());
        user = session.getUserDetails();
        if (user.get(UserSession.USER_NOTEID) == null) {
            setting = "create";
        } else {
            setting = "edit";
            // load data into note
            Log.d(TAG, "onCreate: Title should be.." + user.get(UserSession.USER_NOTE_TITLE));
            Log.d(TAG, "onCreate: Text should be.." + user.get(UserSession.USER_NOTE_TEXT));
            txtNoteTitle.setText(user.get(UserSession.USER_NOTE_TITLE));
            txtNoteText.setText(user.get(UserSession.USER_NOTE_TEXT));
        }
        Log.d(TAG, "onCreate: FINISHED CREATING GROUP ACTIVITY");
    }

    // action for clicking the save button (connect to database and run query)
    public void saveNote(View v) {

        Log.d(TAG, "SAVE BUTTON CLICKED");
        sNoteTitle = txtNoteTitle.getText().toString();
        if (validateNote(sNoteTitle)) {
            Log.d(TAG, "saveNote: ATTEMPTING TO SAVE DETAILS TO DATABASE");
            new Database(this, this, this, link, buildQuery()).execute();
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    // validation (check note not empty)
    public boolean validateNote(String text) {
        if (text.isEmpty()) {
            errorMsg = "Empty note title";
            return false;
        } else {
            return true;
        }
    }

    // write the query to send to the database
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");

        sUserId = user.get(UserSession.USER_USERID);
        sNoteId = user.get(UserSession.USER_NOTEID);
        sGroupId = user.get(UserSession.USER_GROUPID);
        sNoteTitle = txtNoteTitle.getText().toString();
        sNoteText = txtNoteText.getText().toString();

        Log.d(TAG, "buildQuery: GROUP ID = " + user.get(UserSession.USER_GROUPID));
        Log.d(TAG, "buildQuery: NOTE ID = " + user.get(UserSession.USER_NOTEID));
        Log.d(TAG, "buildQuery: NOTE TITLE = " + txtNoteTitle.getText().toString());
        Log.d(TAG, "buildQuery: NOTE TEXT = " + txtNoteText.getText().toString());
        Log.d(TAG, "buildQuery: SETTING = " + setting);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("setting", setting)
                .appendQueryParameter("group_id", sGroupId)
                .appendQueryParameter("note_id", sNoteId)
                .appendQueryParameter("note_title", sNoteTitle)
                .appendQueryParameter("note_text", sNoteText);

        return builder.build().getEncodedQuery();
    }


    // after query finished, return to previous activity
    @Override
    public void processFinish(JSONArray output) {

        Log.d(TAG, "processFinish: Starts");
        Intent i = new Intent(getApplicationContext(), NoteListActivity.class);
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
        Intent i = new Intent(getApplicationContext(), NoteListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}