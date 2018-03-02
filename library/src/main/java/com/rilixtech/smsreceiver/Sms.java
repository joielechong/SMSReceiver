package com.rilixtech.smsreceiver;

/**
 * Created by joielechong on 3/2/18.
 */

class Sms {
  private String sender;
  private String message;

  Sms(String sender, String message) {
    this.sender = sender;
    this.message = message;
  }

  public String getSender() {
    return sender;
  }

  public String getMessage() {
    return message;
  }
}
