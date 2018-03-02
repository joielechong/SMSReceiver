package com.rilixtech.smsreceiver;

public enum SmsReceiverConfig {
  INSTANCE;

  private String beginIndex;
  private String endIndex;
  private String[] smsSenderNumbers;

  /**
   * initialize sms receiver to get the message.
   *
   * @param beginIndex start of text message to get
   * @param endIndex end of text message to get
   * @param smsSenderNumbers numbers of sender. Can be null to receive all message from any sender.
   */
  void initializeSmsConfig(String beginIndex, String endIndex, String... smsSenderNumbers) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
    this.smsSenderNumbers = smsSenderNumbers;
  }

  public String getBeginIndex() {
    return beginIndex;
  }

  public String getEndIndex() {
    return endIndex;
  }

  public String[] getSmsSenderNumbers() {
    return smsSenderNumbers;
  }
}
