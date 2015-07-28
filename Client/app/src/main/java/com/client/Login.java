package com.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import static android.widget.Toast.LENGTH_LONG;


public class Login extends ActionBarActivity {

    Button login;
    EditText username, password;
    String _username = "", _password = "";
    ProgressDialog prgDialog;
    RequestParams params = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prgDialog = new ProgressDialog(this);
        // Set Cancelable as true
        prgDialog.setCancelable(true);

        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login:
                        _username = username.getText().toString().trim();
                        _password = password.getText().toString().trim();

                        if (_username.equals("") || _password.equals(""))
                            Toast.makeText(getApplicationContext(), "Please enter username and password", LENGTH_LONG).show();
                        else
                            getData();

                        break;
                }
            }
        };

        login.setOnClickListener(clickListener);

        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login.performClick();
                    return true;
                }
                return false;
            }
        });
    }


    private void getData() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected void onPreExecute() {
                        prgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface arg0) {
                                prgDialog.cancel();
                                prgDialog.hide();
                            }
                        });
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        _username = username.getText().toString().trim();
                        _password = password.getText().toString().trim();

                        return "";
                    }

                    @Override
                    protected void onPostExecute(String v) {
                        prgDialog.setMessage("Getting data...");
                        prgDialog.show();

                        params.add("_username", _username);
                        params.add("_password", _password);

                        callService();
                    }
                }.execute(null, null, null);
            } catch (Exception e) {
                Log.e("ERRROORRR: ", e.toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Can't connect right now.", LENGTH_LONG).show();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Device is not connected to internet. Do you want to open Settings?").setTitle("Info");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setInverseBackgroundForced(false);

            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(settingsIntent);
                        }
                    });

            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            alertDialog.show();
        }
    }


    private void callService() {
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.get("http://45.35.4.29/w/login.php",
                params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);

                        if (!(responseBody.toString().equals("Username or password is incorrect"))) {
                            int json_id;
                            String json_name;
                            String json_surname;
                            String json_birthday;
                            String json_born_place;
                            String json_graduated_in;
                            String json_graduated_from;
                            String json_picture;

                            //          Toast.makeText(getApplicationContext(), response, LENGTH_LONG).show();

                            //parse JSON data
                            try {
                                Log.d("JSON result: ", responseBody.toString());

//                                JSONArray jsonArray = new JSONArray(responseBody);
//                                JSONObject jsonObject = new JSONObject(Arrays.toString(responseBody));
                                if (responseBody.length() != 0) {
                                    Toast.makeText(getApplicationContext(), "Logged in", LENGTH_LONG).show();
//                                    prgDialog.setMessage("Getting data...");
//                                    prgDialog.show();
//
////                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
////                                    json_id = jsonObject.getInt("_id");
//                                    json_name = jsonObject.getString("_name");
//                                    json_surname = jsonObject.getString("_surname");
//                                    json_graduated_from = jsonObject.getString("_graduated_from");
//                                    json_graduated_in = jsonObject.getString("_graduated_in");
//                                    json_born_place = jsonObject.getString("_born_place");
//                                    json_birthday = jsonObject.getString("_birthday");
////                                    json_picture = jsonObject.getString("_profile_pic");
//
//
//                                    Intent intent = new Intent(getBaseContext(), Profile.class);
//                                    /********** set extra values to send them to Profile activity **********/
////                                    intent.putExtra("_id", json_id);
//                                    intent.putExtra("_name", json_name);
//                                    intent.putExtra("_surname", json_surname);
//                                    intent.putExtra("_graduated_from", json_graduated_from);
//                                    intent.putExtra("_graduated_in", json_graduated_in);
//                                    intent.putExtra("_born_place", json_born_place);
//                                    intent.putExtra("_birthday", json_birthday);
////                                    intent.putExtra("_picture", json_picture);
//                                    /********** set extra values to send them to Profile activity **********/
//                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username or password is incorrect", LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                prgDialog.cancel();
                                prgDialog.hide();
                                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", LENGTH_LONG).show();
                                Log.w("Picture error; ", e.toString());
                            }
                        } else {
                            prgDialog.cancel();
                            prgDialog.hide();
                            Toast.makeText(getApplicationContext(), responseBody.toString(), LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        prgDialog.cancel();
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", LENGTH_LONG).show();
                        Log.e("FAILURE: ", throwable.getMessage() + "\nStatus code: " + statusCode);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
