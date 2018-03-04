package com.rilixtech.sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.rilixtech.smsreceiver.SmsReceiver;

public class MainActivity extends AppCompatActivity {
  private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
  private TextView mTvSms;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mTvSms = findViewById(R.id.sms_tv);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      getPermissionToReadSms();
    } else {
      startSmsReceiver();
    }
  }

  private void startSmsReceiver() {
    SmsReceiver.getInstance().register();
    SmsReceiver.getInstance().initialize("myIM3", "");
    SmsReceiver.getInstance().setResultListener(new SmsReceiver.ResultListener() {
      @Override public void onSmsReceived(String sender, String message) {
        String text = mTvSms.getText().toString() + "\n" +
            "Sender = " + sender + ", " + "Message = " + message;
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        mTvSms.setText(text);
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
        SmsReceiver.getInstance().unregister();
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.M)
  public void getPermissionToReadSms() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        != PackageManager.PERMISSION_GRANTED) {

      if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
      }

      requestPermissions(new String[] { Manifest.permission.RECEIVE_SMS }, READ_CONTACTS_PERMISSIONS_REQUEST);
    } else {
      startSmsReceiver();
    }
  }

  @TargetApi(Build.VERSION_CODES.M)
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
      if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        startSmsReceiver();
      } else {
        boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS);

        if (showRationale) {
        } else {
          Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
        }
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }
}
