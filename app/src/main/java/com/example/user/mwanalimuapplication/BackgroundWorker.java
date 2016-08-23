package com.example.user.mwanalimuapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by USER on 8/7/2016.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    ProgressDialog pdia;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "UserPref";

    BackgroundWorker(Context ctx)
    {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://192.168.0.27/mwanalimu/public/User/login";
        if (type.equals("login"))
        {
            try {
                String number = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("number", "UTF-8") + "=" + URLEncoder.encode(number, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();

                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
        pdia = new ProgressDialog(context);
        pdia.setMessage("Logging you in...");
        pdia.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pdia.dismiss();
        try {
            JSONObject resultJson = new JSONObject(result);

            boolean type = resultJson.getBoolean("type");

            pdia.hide();
            if(type == true)
            {
                JSONObject userDetails = resultJson.getJSONObject("result");
                int student_id = userDetails.getInt("student_id");
                String name = userDetails.getString("name");
                String email = userDetails.getString("email");
                String image = userDetails.getString("photo");

                sharedpreferences = this.context.getSharedPreferences(MyPREFERENCES, this.context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putInt("ID", student_id);
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("image", image);

                editor.commit();

                Intent intent = new Intent(this.context, StudentDashboardActivity.class);
                this.context.startActivity(intent);
            }
            else
            {
                alertDialog.setMessage(resultJson.getString("message"));
                alertDialog.show();
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            Log.e("Exception",e.getMessage());
            android.support.v7.app.AlertDialog alertDialog = null;
            alertDialog.setTitle("Error");
            alertDialog.setMessage(e.getMessage());


            return null;
        }
    }
}
