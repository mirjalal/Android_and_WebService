package com.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    ProgressDialog progressDialog = new ProgressDialog(Login.this);

                    @Override
                    protected void onPreExecute() {
                        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface arg0) {
                                prgDialog.cancel();
                                prgDialog.dismiss();
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
                        params.put("_username", _username);
                        params.put("_password", _password);

                        callService();
                    }
                }.execute(null, null, null);
            } catch (Exception e) {
                Log.e("ERRROORRR: ", e.toString());
            }
        } else
            Toast.makeText(getApplicationContext(), "Device is not connected to network", LENGTH_LONG).show();
    }

    private void callService() {
        prgDialog.hide();
//        prgDialog.setMessage("Calling WebService");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://192.168.0.100:81/Android_and_WebService/WebService/dbMethods/login.php",
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        int json_id;
                        String json_name;
                        String json_surname;
                        String json_birthday;
                        String json_born_place;
                        String json_graduated_in;
                        String json_graduated_from;

                        //          Toast.makeText(getApplicationContext(), response, LENGTH_LONG).show();

                        //parse JSON data
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() != 0) {
                                prgDialog.setMessage("Getting data...");
                                prgDialog.show();

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                json_id = jsonObject.getInt("_id");
                                json_name = jsonObject.getString("_name");
                                json_surname = jsonObject.getString("_surname");
                                json_graduated_from = jsonObject.getString("_graduated_from");
                                json_graduated_in = jsonObject.getString("_graduated_in");
                                json_born_place = jsonObject.getString("_born_place");
                                json_birthday = jsonObject.getString("_birthday");

                                Intent intent = new Intent(getBaseContext(), Profile.class);
                                /********** set extra values to send them to Profile activity **********/
                                intent.putExtra("_id", json_id);
                                intent.putExtra("_name", json_name);
                                intent.putExtra("_surname", json_surname);
                                intent.putExtra("_graduated_from", json_graduated_from);
                                intent.putExtra("_graduated_in", json_graduated_in);
                                intent.putExtra("_born_place", json_born_place);
                                intent.putExtra("_birthday", json_birthday);
                                /********** set extra values to send them to Profile activity **********/
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Username or password is incorrect", LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog
                        prgDialog.cancel();
                        // When Http response code is '404'
                        if (statusCode == 404)
                            Toast.makeText(getApplicationContext(), "Requested resource not found", LENGTH_LONG).show();
                            // When Http response code is '500'
                        else if (statusCode == 500)
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", LENGTH_LONG).show();
                            // When Http response code other than 404, 500
                        else
                            Toast.makeText(getApplicationContext(), "Error occured\nMost common errors:\n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\nHTTP Status code: " + statusCode, LENGTH_LONG).show();
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
