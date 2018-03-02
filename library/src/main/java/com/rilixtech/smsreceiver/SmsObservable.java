package com.rilixtech.smsreceiver;

import java.util.Observable;

class SmsObservable extends Observable {

  private static SmsObservable instance;

  private SmsObservable() {
  }

  static SmsObservable getInstance() {
    if (instance == null) {
      synchronized (SmsReceiver.class) {
        if (instance == null) {
          instance = new SmsObservable();
        }
      }
    }
    return instance;
  }

  void updateValue(Object data) {
    synchronized (this) {
      setChanged();
      notifyObservers(data);
    }
  }
}
