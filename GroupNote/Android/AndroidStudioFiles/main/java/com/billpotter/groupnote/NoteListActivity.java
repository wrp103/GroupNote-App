package com.billpotter.groupnote;

// activity to display the list of notes contained in a group

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteListActivity extends AppCompatActivity implements Database.AsyncResponse {

    private static final String TAG = "NoteListActivity";
    String setting = "listNotes"; // setting to send to the php script to list the notes
    String link = "note.php"; // php file to interact with database
    UserSession session; // current session
    HashMap<String, String> user; // user list
    ArrayList<Note> notes = new ArrayList<>(); // notes list

    // activity elements
    String sNoteId, sGroupId;
    String[] values;
    NoteListActivity.NoteArrayAdapter adapter;
    ListView listview;
    Note noteToDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        listview = findViewById(R.id.noteList);
        session = new UserSession(getApplicationContext());
        user = session.getUserDetails();
        sGroupId = user.get(UserSession.USER_GROUPID);
        new Database(this, this, this, link, buildQuery()).execute();
    }

    // write query to retrieve notes based on current user
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");
        if (setting.equals("listNotes")) {
            Log.d(TAG, "buildQuery: params = " + setting + "(setting)   " + sGroupId + "(group_id)   ");
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("group_id", sGroupId)
                    .appendQueryParameter("setting", setting);
            return builder.build().getEncodedQuery();
        } else if (setting.equals("delete")) {
            Log.d(TAG, "buildQuery: params = " + setting + "(setting)   " + sNoteId + "(note_id)   ");
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("note_id", sNoteId)
                    .appendQueryParameter("setting", setting);
            return builder.build().getEncodedQuery();
        } else {
            return null;
        }
    }

    // process retrieved notes
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: RETRIEVED LIST DATA FROM DATABASE");
        if (output.getString(0).equals("deleted")) {

            setting = "listNotes";
        } else {
            if (output.length() > 0) {
                Log.d(TAG, "processFinish: ADDING DATA TO LIST ARRAY LIST");
                for (int i = 0; i < output.length(); i++) {
                    JSONObject data = output.getJSONObject(i);
                    Log.d(TAG, "processFinish: Adding note : " + data.getString("note_id") + ") " + data.getString("note_text") + data.getString("note_title") + "(note_title)");
                    Note note = new Note(data.getString("note_id"), data.getString("note_text"), data.getString("note_title"));
                    notes.add(note);
                }
                buildListView();
            } else {
                Log.d(TAG, "processFinish: ARRAY IS EMPTY");
            }
        }
        Log.d(TAG, "processFinish: FINISHED READING DATA");
    }

    // create the view to hold the notes
    public void buildListView() {
        values = new String[notes.size()];
        adapter = new NoteListActivity.NoteArrayAdapter(notes, this, values);
        listview.setAdapter(adapter);
    }

    // inner class for building each note
    public class NoteArrayAdapter extends android.widget.ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<Note> notes;


        // access array of notes
        public NoteArrayAdapter(ArrayList<Note> notes, Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.notes = notes;
        }


        // create view for each note
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View rowView;

            if (notes.get(position).active > 0) {

                rowView = inflater.inflate(R.layout.note, parent, false);
                TextView noteTitle = rowView.findViewById(R.id.noteTitle);
                noteTitle.setText(notes.get(position).note_title);
                TextView noteText = rowView.findViewById(R.id.noteText);
                noteText.setText(notes.get(position).note_text);

                ImageView deleteNoteButton = rowView.findViewById(R.id.btnDeleteNote);
                deleteNoteButton.setOnClickListener(v -> {
                    deleteNote(notes.get(position));
                    notes.get(position).setActive(0);
                    adapter.notifyDataSetChanged();
                });

                ImageView editNoteButton = rowView.findViewById(R.id.btnUpdateNote);
                editNoteButton.setOnClickListener(v -> editNote(
                        notes.get(position).getNote_id(),
                        notes.get(position).getNote_title(),
                        notes.get(position).getNote_text()));
            } else {
                rowView = inflater.inflate(R.layout.blank, parent, false);
            }
            return rowView;

        }
    }

    // action to delete the note from the database
    public void deleteNote(Note note) {

        setting = "delete";
        sNoteId = note.note_id;
        noteToDelete = note;
        new Database(this, this, this, link, buildQuery()).execute();
    }

    // action to go to the edit note activity
    public void editNote(String id, String title, String text) {

        session.setUserNoteDetails(id, title, text);
        Intent i = new Intent(getApplicationContext(), CreateNoteActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    // action to create a new note and go to create note activity
    public void createNote(View v) {
        session.setUserNoteDetails(null,null,null);
        Intent i = new Intent(getApplicationContext(), CreateNoteActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
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


    // action when back button is pressed
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}