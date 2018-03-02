package com.rilixtech.smsreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class SmsReceiver {

  private static SmsReceiver instance;

  private SmsReceiver() {
  }

  public static SmsReceiver getInstance() {
    if (instance == null) {
      synchronized (SmsReceiver.class) {
        if (instance == null) {
          instance = new SmsReceiver();
        }
      }
    }
    return instance;
  }

  @NonNull private LocalBroadcastManager localBroadcastManager;

  @NonNull private ResultListener resultListener;

  public interface ResultListener {
    void onSmsReceived(String sender, String message);
  }

  private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction() == null) {
        Log.e("SmsReceiver", "Something wrong with the SmsReceiver!");
        return;
      }
      if (intent.getAction().equals(SmsBroadcastReceiver.INTENT_ACTION_SMS)) {
        String receivedSender = intent.getStringExtra(SmsBroadcastReceiver.KEY_SMS_SENDER);
        String receivedMessage = intent.getStringExtra(SmsBroadcastReceiver.KEY_SMS_MESSAGE);
        resultListener.onSmsReceived(receivedSender != null ? receivedSender : "",
            receivedMessage != null ? receivedMessage : "");
      }
    }
  };

  public void registerReceiver(Activity activity, ResultListener listener) {
    localBroadcastManager = LocalBroadcastManager.getInstance(activity);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(SmsBroadcastReceiver.INTENT_ACTION_SMS);
    localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    resultListener = listener;
  }

  public void unRegisterReceiver() {
    localBroadcastManager.unregisterReceiver(broadcastReceiver);
  }

  public void initialize(@Nullable String beginIndex, @Nullable String endIndex,
      @Nullable String... smsSenderNumbers) {
    SmsReceiverConfig.INSTANCE.initializeSmsConfig(beginIndex, endIndex, smsSenderNumbers);
  }

  /**
   * Initialize sms receiver to receive all sms from any number.
   * @param beginIndex start of text to recognize.
   * @param endIndex end of text to recognize.
   */
  public void initialize(@Nullable String beginIndex, @Nullable String endIndex) {
    SmsReceiverConfig.INSTANCE.initializeSmsConfig(beginIndex, endIndex);
  }

  /**
   * Initialize sms receiver to receive all sms from any number as it is.
   */
  public void initialize() {
    SmsReceiverConfig.INSTANCE.initializeSmsConfig(null, null, null);
  }
}
