package com.billpotter.groupnote;

// session class holds all data of current logged in user
// mobile device retains this information to enable automatic log in

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.HashMap;

public class UserSession {

    private static final String TAG = "UserSession";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // All Shared Preferences Keys
    private static final String IS_USER_PROFILE_COMPLETE = "IsUserProfileComplete";

    // Username (make variable public to access from outside)
    public static final String USER_USERID = "user_id";

    // Username (make variable public to access from outside)
    public static final String USER_USERNAME = "username";

    // Email address (make variable public to access from outside)
    public static final String USER_EMAIL = "email";

    // Username (make variable public to access from outside)
    public static final String USER_FIRSTNAME = "firstname";

    // Username (make variable public to access from outside)
    public static final String USER_LASTNAME = "lastname";

    // Email address (make variable public to access from outside)
    public static final String USER_TELEPHONE = "telephone";

    // Email address (make variable public to access from outside)
    public static final String USER_GROUPID = "group_id";

    // Email address (make variable public to access from outside)
    public static final String USER_NOTEID = "note_id";

    // Email address (make variable public to access from outside)
    public static final String USER_NOTE_TITLE = "note_title";

    // Email address (make variable public to access from outside)
    public static final String USER_NOTE_TEXT = "note_text";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public UserSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String user_id, String username, String email, String firstname, String lastname, String telephone){

        Log.d(TAG, "createUserLoginSession1: STARTS");
        Log.d(TAG, "createUserLoginSession2: SETTING USER ID AS = " + user_id);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putBoolean(IS_USER_PROFILE_COMPLETE, true);
        editor.putString(USER_USERID, user_id);
        editor.putString(USER_USERNAME, username);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_FIRSTNAME, firstname);
        editor.putString(USER_LASTNAME, lastname);
        editor.putString(USER_TELEPHONE, telephone);
        editor.commit();
    }

    public void createUserLoginSession(String user_id, String username, String email){

        Log.d(TAG, "createUserLoginSession2: STARTS");
        Log.d(TAG, "createUserLoginSession2: SETTING USER ID AS = " + user_id);

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(USER_USERID, user_id);
        editor.putString(USER_USERNAME, username);
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    // if session is in a logge dout state go to login activity
    public boolean checkLogin(){

        Log.d(TAG, "checkLogin: LOGIN BEING CHECKED");

        if(!this.isUserLoggedIn()){

            Log.d(TAG, "checkLogin: USER NOT LOGGED IN OPENING LOGIN ACTIVITY");
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }

        Log.d(TAG, "checkLogin: USER LOGGED IN AS " + pref.getString(USER_USERNAME, null) );

        if (!this.isUserProfileCreated()) {

            Log.d(TAG, "checkLogin: USER HAS NO PROFILE OPENING PROFILE ACTIVITY");
            Log.d(TAG, "checkLogin: STATIC VARIABLE SET TO " + isUserProfileCreated());
            Intent i = new Intent(_context, ProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    // Get stored session data
    public HashMap<String, String> getUserDetails(){

        HashMap<String, String> user = new HashMap<>();
        user.put(USER_USERID, pref.getString(USER_USERID, null));
        user.put(USER_USERNAME, pref.getString(USER_USERNAME, null));
        user.put(USER_EMAIL, pref.getString(USER_EMAIL, null));
        user.put(USER_FIRSTNAME, pref.getString(USER_FIRSTNAME, null));
        user.put(USER_LASTNAME, pref.getString(USER_LASTNAME, null));
        user.put(USER_TELEPHONE, pref.getString(USER_TELEPHONE, null));
        user.put(USER_GROUPID, pref.getString(USER_GROUPID, null));
        user.put(USER_NOTEID, pref.getString(USER_NOTEID, null));
        user.put(USER_NOTE_TEXT, pref.getString(USER_NOTE_TEXT, null));
        user.put(USER_NOTE_TITLE, pref.getString(USER_NOTE_TITLE, null));

        return user;
    }


    // Clear session details

    public void logoutUser(){

        Log.d(TAG, "logoutUser: logging user out");
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    // check user is logged in
    public boolean isUserLoggedIn(){

        Log.d(TAG, "isUserLoggedIn: checking user logged in");
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    // check user has created a profile
    public boolean isUserProfileCreated(){

        Log.d(TAG, "isUserProfileCreated: checking user profile created");
        return pref.getBoolean(IS_USER_PROFILE_COMPLETE, false);
    }

    // update user profile session
    public void createUserProfileSession(String firstname, String lastname, String telephone){

        editor.putBoolean(IS_USER_PROFILE_COMPLETE, true);
        editor.putString(USER_FIRSTNAME, firstname);
        editor.putString(USER_LASTNAME, lastname);
        editor.putString(USER_TELEPHONE, telephone);
        editor.commit();

    }

    // set current active group
    public void setUserLastGroupId(String id) {

        editor.putString(USER_GROUPID, id);
        editor.commit();
    }

    // set current active note
    public void setUserNoteDetails(String id, String title, String text) {

        editor.putString(USER_NOTEID, id);
        editor.putString(USER_NOTE_TITLE, title);
        editor.putString(USER_NOTE_TEXT, text);
        editor.commit();
    }
}
