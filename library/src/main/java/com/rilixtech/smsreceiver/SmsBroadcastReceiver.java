package com.rilixtech.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import android.util.Log;
import java.util.Arrays;
import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {
  private static final String TAG = "SmsBroadcastReceiver";
  private static final String INTENT_ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

  @Override public void onReceive(Context context, Intent intent) {
    String[] senderArray = SmsReceiverConfig.INSTANCE.getSmsSenderNumbers();
    if (intent.getAction().equals(INTENT_ACTION_SMS_RECEIVED)) {

      Bundle bundle = intent.getExtras();
      SmsMessage[] smsMessages;
      String sender = null;
      if (bundle != null) {
        //PDU = protocol data unit
        //A PDU is a “protocol data unit”, which is the industry format for an SMS message.
        //Because SMSMessage reads/writes them you shouldn't need to dissect them.
        //A large message might be broken into many, which is why it is an array of objects.
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus != null) {
          smsMessages = new SmsMessage[pdus.length];
          // If the sent message is longer than 160 characters  it will be broken down
          // in to chunks of 153 characters before being received on the device.
          // To rectify that receivedMessage is the result of appending every single
          // short message into one large one for our usage. see:
          //http://www.textanywhere.net/faq/is-there-a-maximum-sms-message-length

          for (int i = 0; i < smsMessages.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
            } else {
              smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            sender = smsMessages[i].getOriginatingAddress();
          }

          if (senderArray != null) {
            handleMessageFromSenders(senderArray, sender, smsMessages);
          } else {
            handleMessage(sender, smsMessages);
          }
          abortBroadcast();
        }
      }
    }
  }

  private void handleMessageFromSenders(String[] senderArray, String sender, SmsMessage[] smsMessages) {
    Log.d(TAG, "handleMessageFromSenders() is called");
    List<String> smsSenderNumbers = Arrays.asList(senderArray);
    if (!TextUtils.isEmpty(sender) && smsSenderNumbers.contains(sender)) {
      StringBuilder receivedMessage = new StringBuilder();
      for (SmsMessage smsMessage : smsMessages) {
        receivedMessage.append(smsMessage.getMessageBody());
      }
      notifyObserverForSMS(sender, receivedMessage.toString());
    } else {
      notifyObserverForSMS("", "");
    }
  }

  private void handleMessage(String sender, SmsMessage[] smsMessages) {
    Log.d(TAG, "handleMessage() is called");
    StringBuilder receivedMessage = new StringBuilder();
    for (SmsMessage smsMessage : smsMessages) {
      receivedMessage.append(smsMessage.getMessageBody());
    }
    notifyObserverForSMS(sender, receivedMessage.toString());
  }

  private void notifyObservers(Sms sms) {
    SmsObservable.getInstance().updateValue(sms);
  }

  private void notifyObserverForSMS(String from, String message) {
    String beginIndexSingleton = SmsReceiverConfig.INSTANCE.getBeginIndex();
    String endIndexSingleton = SmsReceiverConfig.INSTANCE.getEndIndex();

    if (beginIndexSingleton != null && endIndexSingleton != null) {
      int startIndex = message.indexOf(beginIndexSingleton);
      int endIndex;
      if(endIndexSingleton.isEmpty()) {
        endIndex = message.length() -1;
      } else {
        endIndex = message.indexOf(endIndexSingleton);
      }

      String msg = message.substring(startIndex, endIndex).replace(beginIndexSingleton, "").trim();
      if (!msg.isEmpty()) {
        notifyObservers(new Sms(from, msg));
      }
    } else {
      notifyObservers(new Sms(from, message));
    }
  }

  //private String getSmsCode(String message) {
  //  String beginIndexSingleton = SmsReceiverConfig.INSTANCE.getBeginIndex();
  //  String endIndexSingleton = SmsReceiverConfig.INSTANCE.getEndIndex();
  //
  //  if (beginIndexSingleton != null && endIndexSingleton != null) {
  //    int startIndex = message.indexOf(beginIndexSingleton);
  //    int endIndex = message.indexOf(endIndexSingleton);
  //
  //    return message.substring(startIndex, endIndex).replace(beginIndexSingleton, "").trim();
  //  } else {
  //    return message;
  //  }
  //}
}