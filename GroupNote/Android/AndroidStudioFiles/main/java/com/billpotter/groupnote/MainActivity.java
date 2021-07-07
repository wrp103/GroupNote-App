package com.billpotter.groupnote;

// main activity contains a menu of options for the user to navigate to
// it also checks if the user is logged in already or not as a mobile device can store a sessions
// information to keep the user logged in permanently and prevent having to retrieve data every
// time the user opens the app.

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Database.AsyncResponse  {

    private static final String TAG = "MainActivity";
    UserSession session; // current session

    HashMap<String, String> user; // user list
    ArrayList<Group> groups = new ArrayList<>(); // group list
    String link = "group.php"; // php file to interact with database

    // activity elements
    String sUserId;
    String setting = "listGroups";
    String[] values;
    MainActivity.GroupArrayAdapter adapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        listview = findViewById(R.id.groupList);
        session = new UserSession(getApplicationContext());
        user = session.getUserDetails();
        sUserId = user.get(UserSession.USER_USERID);
        new Database(this, this, this, link, buildQuery()).execute();

        // check if use ris already logged in, if not go to login page
        if (session.checkLogin()) {
            Log.d(TAG, "onCreate: CHECK RETURNED FALSE, CLOSING ACTIVITY");
            finish();
        }


    }

    // write query to retrieve current groups that user is in
    public String buildQuery() {

        Log.d(TAG, "buildQuery: BUILDING QUERY");
        Log.d(TAG, "buildQuery: params = " + sUserId + "(userid)   " +  setting + "(setting)");
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("user_id", sUserId)
                .appendQueryParameter("setting", setting);
        return builder.build().getEncodedQuery();
    }


    // process all groups retrieved from the database query
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void processFinish(JSONArray output) throws JSONException {

        Log.d(TAG, "processFinish: RETRIEVED GROUP DATA FROM DATABASE");
        if (output.length() > 0) {
            Log.d(TAG, "processFinish: ADDING DATA TO GROUPS ARRAY LIST");
            for (int i = 0; i < output.length(); i++) {
                JSONObject data = output.getJSONObject(i);
                Log.d(TAG, "processFinish: Adding group : " + data.getString("group_id") + ") " +data.getString("group_name") );
                Group group = new Group(data.getString("group_id"), data.getString("group_name"));
                groups.add(group);
            }
            buildListView();
        } else {
            Log.d(TAG, "processFinish: ARRAY IS EMPTY");
        }
        Log.d(TAG, "processFinish: FINISHED READING DATA");
    }

    // add the groups retrieved to the list in the activity
    public void buildListView() {
        values = new String[groups.size()];
        adapter = new MainActivity.GroupArrayAdapter(groups, this, values);
        listview.setAdapter(adapter);
    }

    // inner class to build the group list
    public class GroupArrayAdapter extends android.widget.ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        private ArrayList<Group> groups;

        public GroupArrayAdapter(ArrayList<Group> groups, Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
            this.groups = groups;
        }

        // create a view for each group
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.group, parent, false);
            TextView textView = rowView.findViewById(R.id.noteTitle);
            textView.setText(groups.get(position).group_name);
            Button addUserButton = rowView.findViewById(R.id.groupAddUser);
            addUserButton.setOnClickListener(v -> {
       
                session.setUserLastGroupId(groups.get(position).group_id);
                Intent i = new Intent(getApplicationContext(), AddUsersActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            });

            Button manageNotesButton = rowView.findViewById(R.id.groupManageNotes);


            manageNotesButton.setOnClickListener(v -> {

                session.setUserLastGroupId(groups.get(position).group_id);
                Intent i = new Intent(getApplicationContext(), NoteListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            });
            return rowView;
        }
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


}