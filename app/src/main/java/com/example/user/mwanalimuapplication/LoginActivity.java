package com.example.user.mwanalimuapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    EditText studentNumbertxt, studentPasswordtxt;
    ProgressDialog pdia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        studentNumbertxt = (EditText) findViewById(R.id.studentNumberTxt);
        studentPasswordtxt = (EditText) findViewById(R.id.studentPasswordTxt);
    }

    public void LoginUser(View view)
    {
        /*Intent studentDashboardIntent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
        LoginActivity.this.startActivity(studentDashboardIntent);*/
        String number = studentNumbertxt.getText().toString();
        String password = studentPasswordtxt.getText().toString();
        String type = "login";
//
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);

        backgroundWorker.execute(type, number, password);

        /*try {
            final String number = studentNumbertxt.getText().toString();
            final String password = studentPasswordtxt.getText().toString();

            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Logging you in...");
            pdia.show();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        Boolean type = jsonResponse.getBoolean("type");
                        String message = jsonResponse.getString("message");

                        pdia.dismiss();

                        if (type == true) {
                            Intent studentDashboardIntent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
                            LoginActivity.this.startActivity(studentDashboardIntent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Authentication Failed")
                                    .setMessage(message)
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            LoginRequest loginRequest = new LoginRequest(number, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }
        catch (Exception e){
            Toast.makeText(LoginActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }*/
    }
}
