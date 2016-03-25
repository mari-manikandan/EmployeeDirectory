package com.himebaugh.employeedirectory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class DetailActivity extends AppCompatActivity implements OnClickListener {
    // XML node keys
    static final String KEY_ID = "empID";
    static final String KEY_NAME = "name";
    static final String KEY_TITLE = "title";
    static final String KEY_DEPARTMENT = "department";
    static final String KEY_CITY = "city";
    static final String KEY_OFFICEPHONE = "officePhone";
    static final String KEY_MOBILEPHONE = "mobilePhone";
    static final String KEY_EMAIL = "email";
    static final String KEY_PICTURE = "picture";
    public TextView mName;
    public TextView mTitle;
    public TextView mDepartment;
    public TextView mCity;
    private TextView mOfficePhone;
    private TextView mMobilePhone;
    private TextView mSms;
    private TextView mEmail;
    private ImageView mPicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // implement Up Navigation with caret in front of App icon in the Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // getting intent data
        Intent intent = getIntent();

        // Update values on the screen with XML values from previous intent
        mName = (TextView) findViewById(R.id.activity_detail_name);
        mTitle = (TextView) findViewById(R.id.activity_detail_title);
        mDepartment = (TextView) findViewById(R.id.activity_detail_department);
        mCity = (TextView) findViewById(R.id.activity_detail_city);
        mOfficePhone = (TextView) findViewById(R.id.activity_detail_office_phone);
        mMobilePhone = (TextView) findViewById(R.id.activity_detail_mobile_phone);
        mSms = (TextView) findViewById(R.id.activity_detail_sms);
        mEmail = (TextView) findViewById(R.id.activity_detail_email);
        mPicture = (ImageView) findViewById(R.id.activity_detail_picture);

        mName.setText(intent.getStringExtra(KEY_NAME));
        mTitle.setText(intent.getStringExtra(KEY_TITLE));
        mDepartment.setText(intent.getStringExtra(KEY_DEPARTMENT));
        mCity.setText(intent.getStringExtra(KEY_CITY));
        mOfficePhone.setText(intent.getStringExtra(KEY_OFFICEPHONE));
        mMobilePhone.setText(intent.getStringExtra(KEY_MOBILEPHONE));
        mSms.setText(intent.getStringExtra(KEY_MOBILEPHONE));
        mEmail.setText(intent.getStringExtra(KEY_EMAIL));

        // listen for button clicks
        findViewById(R.id.activity_detail_call_office_button).setOnClickListener(this);
        findViewById(R.id.activity_detail_office_phone).setOnClickListener(this);
        findViewById(R.id.activity_detail_call_mobile_button).setOnClickListener(this);
        findViewById(R.id.activity_detail_mobile_phone).setOnClickListener(this);
        findViewById(R.id.activity_detail_send_sms_button).setOnClickListener(this);
        findViewById(R.id.activity_detail_sms).setOnClickListener(this);
        findViewById(R.id.activity_detail_send_email_button).setOnClickListener(this);
        findViewById(R.id.activity_detail_email).setOnClickListener(this);

        // add PhoneStateListener FROM http://www.mkyong.com/android/how-to-make-a-phone-call-in-android/
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        InputStream is;
        try {
            is = getAssets().open("pics/" + intent.getStringExtra(KEY_PICTURE));
            Bitmap bit = BitmapFactory.decodeStream(is);
            mPicture.setImageBitmap(bit);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            // This is called when the Home (Up) button is pressed in the Action Bar.
            Intent parentActivityIntent = new Intent(this, MainActivity.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    // @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.activity_detail_call_office_button:
            case R.id.activity_detail_office_phone: // clicking on textView will have same action as the button

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mOfficePhone.getText().toString()));
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
                break;

            case R.id.activity_detail_call_mobile_button:
            case R.id.activity_detail_mobile_phone: // clicking on textView will have same action as the button

                Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                callIntent2.setData(Uri.parse("tel:" + mMobilePhone.getText().toString()));
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent2);
                }
                break;

            case R.id.activity_detail_send_sms_button:
            case R.id.activity_detail_sms: // clicking on textView will have same action as the button

                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + mMobilePhone.getText().toString()));
                // smsIntent.putExtra("sms_body", "Hello " + mName.getText().toString() );
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(smsIntent);
                }
                break;

            case R.id.activity_detail_send_email_button:
            case R.id.activity_detail_email: // clicking on textView will have same action as the button

                String to = mEmail.getText().toString();
                String subject = "textSubject";
                String message = "textMessage";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                // prompts email apps
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;

        }

    }

    // monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        String LOG_TAG = "LANGDON";
        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

}
