package com.rilixtech.smsreceiver;

import java.util.Observable;
import java.util.Observer;

public class SmsReceiver implements Observer {

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

  private ResultListener resultListener;

  public interface ResultListener {
    void onSmsReceived(String sender, String message);
  }

  @Override public void update(Observable o, Object arg) {
    if (arg instanceof Sms) {
      Sms sms = (Sms) arg;

      resultListener.onSmsReceived(sms.getSender(), sms.getMessage());
    }
  }

  public void register() {
    SmsObservable.getInstance().addObserver(this);
  }

  public void setResultListener(ResultListener listener) {
    resultListener = listener;
  }

  public void unregister() {
    SmsObservable.getInstance().deleteObserver(this);
  }

  public void initialize(String beginIndex, String endIndex, String... smsSenderNumbers) {
    SmsReceiverConfig.INSTANCE.initializeSmsConfig(beginIndex, endIndex, smsSenderNumbers);
  }

  /**
   * Initialize sms receiver to receive all sms from any number.
   * @param beginIndex start of text to recognize can be null.
   * @param endIndex end of text to recognize can be null.
   */
  public void initialize(String beginIndex, String endIndex) {
    SmsReceiverConfig.INSTANCE.initializeSmsConfig(beginIndex, endIndex);
  }

  /**
   * Initialize sms receiver to receive all sms from any number as it is.
   */
  public void initialize() {
    SmsReceiverConfig.INSTANCE.initializeSmsConfig(null, null, null);
  }
}
