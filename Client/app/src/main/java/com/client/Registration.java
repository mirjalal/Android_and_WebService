package com.client;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.EXTRA_INITIAL_INTENTS;
import static android.content.Intent.createChooser;
import static android.widget.Toast.LENGTH_LONG;


public class Registration extends ActionBarActivity {

    private static int RESULT_LOAD_IMG = 1;

    // local variables
    int gun, ay, il;
    Button submit, clear;
    TextView pick_image, birthday;
    EditText username, password, name, surname, graduated_from, graduated_in, born_place;
    ImageView profile_pic;
    ProgressDialog prgDialog;
    RequestParams params = new RequestParams();
    String _username, _password, _name, _surname, _graduated_from, _graduated_in, _born_place, _birthday, imgPath, fileName, encodedString;
    Bitmap bitmap;


    private DatePickerDialog.OnDateSetListener datepickerlistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            il = year;
            ay = monthOfYear + 1;
            gun = dayOfMonth;

            birthday.setText(gun + "." + ay + "." + il);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        prgDialog = new ProgressDialog(this);
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        // find view elements
        submit = (Button) findViewById(R.id.submit);
        clear = (Button) findViewById(R.id.clear);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        graduated_from = (EditText) findViewById(R.id.graduated_from);
        graduated_in = (EditText) findViewById(R.id.graduated_in);
        born_place = (EditText) findViewById(R.id.born_place);
        birthday = (TextView) findViewById(R.id.birthday);
        pick_image = (TextView) findViewById(R.id.pick_image);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);


        // set calendar to current date
        final Calendar calendar = Calendar.getInstance();
        gun = calendar.get(Calendar.DATE);
        ay = calendar.get(Calendar.MONTH);
        il = calendar.get(Calendar.YEAR);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);


        View.OnClickListener button_click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.submit: // "Submit" button
                        _name = name.getText().toString().trim();
                        _surname = surname.getText().toString().trim();
                        _graduated_from = graduated_from.getText().toString().trim();
                        _graduated_in = graduated_in.getText().toString().trim();
                        _born_place = born_place.getText().toString().trim();

                        if (birthday.getText().equals("Birthday"))
                            _birthday = "";
                        else
                            _birthday = birthday.getText().toString();

//                        if (_username.equals("") || _password.equals(""))
//                            Toast.makeText(getApplicationContext(), "Username and password required.", LENGTH_LONG).show();
//                        else {
                            _username = username.getText().toString().trim();
                            _password = password.getText().toString(); // don't trim this value; user should be enter whitespace as password

                            if (imgPath != null && !imgPath.isEmpty()) {
                                postData();
                            } else {
                                Toast.makeText(getApplicationContext(), "You must select image from gallery before you try to upload", LENGTH_LONG).show();
//                            }
                        }

                        break;
                    case R.id.clear:  // "Clear" button
                        ViewGroup group = (ViewGroup) findViewById(R.id.form);
                        clearForm(group);
                        break;
                    case R.id.birthday: // "birthday" TextView
                        showDialog(0);
                        break;
                    case R.id.pick_image: // "profile_pic" TextView
                        pickImage();
                        break;
                }
            }
        };

        submit.setOnClickListener(button_click);
        clear.setOnClickListener(button_click);
        birthday.setOnClickListener(button_click);
        pick_image.setOnClickListener(button_click);
    }

    private void postData() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            new AsyncTask<Void, Void, String>() {

                protected void onPreExecute() {

                }

                @Override
                protected String doInBackground(Void... params) {
                    BitmapFactory.Options options;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    bitmap = BitmapFactory.decodeFile(imgPath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    // Must compress the Image to reduce image size to make upload easy
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byte_arr = stream.toByteArray();

                    //calculate how many bytes our image consists of.
//                    int bytes = bitmap.getByteCount();
//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
//int bytes = b.getWidth()*b.getHeight()*4;

//                    ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//                    bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

//                    byte[] byte_arr = buffer.array(); //Get the underlying array containing the data.
                    // Encode Image to String
                    encodedString = Base64.encodeToString(byte_arr, 0);

                    return "";
                }

                @Override
                protected void onPostExecute(String msg) {
                    prgDialog.setMessage("Collecting data...");
                    // Put converted Image string into Async Http Post param
                    params.add("_username", _username);
                    params.add("_password", _password);
                    params.add("_name", _name);
                    params.add("_surname", _surname);
                    params.add("_graduated_from", _graduated_from);
                    params.add("_graduated_in", _graduated_in);
                    params.add("_born_place", _born_place);
                    params.add("_birthday", _birthday);
                    params.add("image", encodedString);

                    makeHTTPCall();
                }
            }.execute(null, null, null);
        } else
            Toast.makeText(getApplicationContext(), "Device is not connected to network", LENGTH_LONG).show();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Collecting data...");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://45.35.4.29/w/registration.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.cancel();
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Success!", LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.cancel();
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", LENGTH_LONG).show();
                    }
                });
    }


    // get value from DatePicker & set to TextView
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0)
            return new DatePickerDialog(this, datepickerlistener, il, ay, gun);

        return null;
    }


    // clear form
    void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            if (group.getChildAt(i) instanceof EditText)
                ((EditText) group.getChildAt(i)).setText("");

            if (group.getChildAt(i) instanceof ViewGroup && (((ViewGroup) group.getChildAt(i)).getChildCount() > 0))
                clearForm((ViewGroup) group.getChildAt(i));
        }

        birthday.setText("Birthday");
        pick_image.setText("Pick image");
    }


    // pick image
    private void pickImage() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // for Camera app
        Intent getIntent = new Intent(ACTION_GET_CONTENT); // for other image base apps (locations)
        getIntent.setType("image/*");
//        Intent pickIntent = new Intent(ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // for "Android System" option
//        pickIntent.setType("image/*");
        Intent chooserIntent = createChooser(getIntent, "Select Image");

        chooserIntent.putExtra(EXTRA_INITIAL_INTENTS, new Intent[]{takePicture});
        startActivityForResult(chooserIntent, RESULT_LOAD_IMG);
    }


    // When image is selected
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // if an image picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();

                profile_pic.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                fileName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

                // copy file
                copyFile(new File(imgPath), fileName);

                params.add("fileName", fileName);
            } else
                Toast.makeText(this, "You haven't picked Image", LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", LENGTH_LONG).show();
        }
    }


    // copy file to specific folder in device. In login operation program'll check this dir for user profile_pic. If found image'll load from this dir, else it'll be download from server.
    private void copyFile(File sourceLocation, String fileName) throws IOException {
        File targetLocation = new File("/storage/emulated/0/Account operations/" + fileName);

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                //throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
                Toast.makeText(getApplicationContext(), "Cannot create directory: " + targetLocation.getAbsolutePath(), LENGTH_LONG).show();
            }

            String[] children = sourceLocation.list();
            for (String aChildren : children)
                copyFile(new File(sourceLocation, aChildren), fileName);
        } else {
            // make sure the directory we plan to store the recording in exists
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
//                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
                Toast.makeText(getApplicationContext(), "Cannot create directory: " + directory.getAbsolutePath(), LENGTH_LONG).show();
            }

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
