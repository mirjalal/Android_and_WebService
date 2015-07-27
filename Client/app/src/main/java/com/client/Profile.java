package com.client;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class Profile extends ActionBarActivity {
    int gun, ay, il;
    Button update, clear;
    TextView birthday;
    EditText name, surname, graduated_from, graduated_in, born_place;
    ImageView _picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int id;
        String name_value;
        String surname_value;
        String graduated_from_value;
        String graduated_in_value;
        String born_place_value;
        String birthday_value;
        String profile_pic_value;

        /***************** get values from view elements ******************/
        update = (Button) findViewById(R.id.update);
        clear = (Button) findViewById(R.id.clear);
        name = (EditText) findViewById(R.id.profile_name);
        surname = (EditText) findViewById(R.id.profile_surname);
        graduated_from = (EditText) findViewById(R.id.profile_graduated_from);
        graduated_in = (EditText) findViewById(R.id.profile_graduated_in);
        born_place = (EditText) findViewById(R.id.profile_born_place);
        birthday = (TextView) findViewById(R.id.profile_birthday);
        _picture = (ImageView) findViewById(R.id.profile_imageView);
        /***************** get values from view elements ******************/

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("_id");
            name_value = extras.getString("_name");
            surname_value = extras.getString("_surname");
            graduated_from_value = extras.getString("_graduated_from");
            graduated_in_value = extras.getString("_graduated_in");
            born_place_value = extras.getString("_born_place");
            birthday_value = extras.getString("_birthday");
            profile_pic_value = extras.getString("_picture");

            /***************** set values to view elements ******************/
            name.setText(name_value.trim());
            surname.setText(surname_value.trim());
            graduated_from.setText(graduated_from_value.trim());
            graduated_in.setText(graduated_in_value.trim());
            born_place.setText(born_place_value.trim());
            birthday.setText(birthday_value.trim());


            byte[] decodedByte = Base64.decode(profile_pic_value, Base64.DEFAULT);
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
//            byte[] decodedString = Base64.decode(profile_pic_value, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
            _picture.setImageBitmap(bmp);
            /***************** set values to view elements ******************/
        }

        /***************** set calendar to current date ******************/
        final Calendar calendar = Calendar.getInstance();
        gun = calendar.get(Calendar.DATE);
        ay = calendar.get(Calendar.MONTH);
        il = calendar.get(Calendar.YEAR);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        /***************** set calendar to current date ******************/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ECLAIR && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Info");
            alertbox.setMessage("Do you want to quit?");

            alertbox.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                            Intent intent = new Intent(Profile.this, Main.class);
                            startActivity(intent);
                        }
                    });

            alertbox.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            alertbox.show();

            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
