package com.billpotter.groupnote;

// class for interacting with the database by sending queries to php scripts and processing
// the returned data

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static android.content.ContentValues.TAG;

public class Database extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    Context mContext;
    ProgressDialog pdDialog;
    String URL="http://192.168.0.12/web/groupNote/"; // URL of php script folder on the groupNote server
    HttpURLConnection conn;
    String URL_RESPONSE="";
    @SuppressLint("StaticFieldLeak")
    Activity mProvidedActivity;
    String mPage;
    String mQuery;
    public AsyncResponse delegate;

    // run waiting for retrieved data on a different thread to prevent freezing the phone
    public interface AsyncResponse {
        void processFinish(JSONArray output) throws JSONException;
    }

    public Database(AsyncResponse delegate, Context context, Activity providedActivity, String page, String query) {

        this.delegate = delegate;
        mContext = context;
        mProvidedActivity = providedActivity;
        mPage = page;
        mQuery = query;
    }


    // show message to user that the action is happening
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        pdDialog = new ProgressDialog(mContext);
        pdDialog.setMessage("Registering..");
        pdDialog.setCancelable(false);
        pdDialog.show();
    }

    // main database action method, creating the URL page with query inserted as POST. Running
    // the URL PHP script and processing the data retrieved.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... arg0) {

        try {
            URL url = new URL(URL+mPage);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(mQuery);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int response_code = conn.getResponseCode();

            if (response_code == HttpURLConnection.HTTP_OK) {

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return (result.toString());
            } else {
                return ("unsuccessful");
            }
        } catch (IOException a) {
            Log.d(TAG, "IOE response " + a.toString());
            URL_RESPONSE = "ERROR";
        }
        return null;
    }

    // put data retrieved into an array
    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (pdDialog.isShowing())
            pdDialog.dismiss();
        try {
            delegate.processFinish(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
