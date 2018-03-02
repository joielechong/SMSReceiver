package com.rilixtech.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

public class SmsBroadcastReceiver extends BroadcastReceiver {
  private static final String INTENT_ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

  @Override public void onReceive(Context context, Intent intent) {
    String[] senderArray = SmsReceiverConfig.INSTANCE.getSmsSenderNumbers();
    if (intent.getAction().equals(INTENT_ACTION_SMS_RECEIVED)) {

      List<String> smsSenderNumbers = null;
      if (senderArray != null) {
        smsSenderNumbers = Arrays.asList(senderArray);
      }

      Bundle bundle = intent.getExtras();
      SmsMessage[] smsMessages;
      String messageFrom = null;
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
            messageFrom = smsMessages[i].getOriginatingAddress();
          }

          if (smsSenderNumbers != null) {
            if (!TextUtils.isEmpty(messageFrom) && smsSenderNumbers.contains(messageFrom)) {
              StringBuilder receivedMessage = new StringBuilder();
              for (SmsMessage smsMessage : smsMessages) {
                receivedMessage.append(smsMessage.getMessageBody());
              }
              notifyObservers(new Sms(messageFrom, getSmsCode(receivedMessage.toString())));
            } else {
              notifyObservers(new Sms("", ""));
            }
          } else {
            StringBuilder receivedMessage = new StringBuilder();
            for (SmsMessage smsMessage : smsMessages) {
              receivedMessage.append(smsMessage.getMessageBody());
            }
            notifyObservers(new Sms(messageFrom, getSmsCode(receivedMessage.toString())));
          }

          abortBroadcast();
        }
      }
    }
  }

  private void notifyObservers(Sms sms) {
    SmsObservable.getInstance().updateValue(sms);
  }

  private String getSmsCode(String message) {
    String beginIndexSingleton = SmsReceiverConfig.INSTANCE.getBeginIndex();
    String endIndexSingleton = SmsReceiverConfig.INSTANCE.getEndIndex();

    if (beginIndexSingleton != null && endIndexSingleton != null) {
      int startIndex = message.indexOf(beginIndexSingleton);
      int endIndex = message.indexOf(endIndexSingleton);

      return message.substring(startIndex, endIndex).replace(beginIndexSingleton, "").trim();
    } else {
      return message;
    }
  }
}