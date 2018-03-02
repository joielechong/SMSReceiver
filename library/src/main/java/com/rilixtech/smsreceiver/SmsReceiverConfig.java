package com.rilixtech.smsreceiver;

import android.support.annotation.Nullable;

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
  void initializeSmsConfig(@Nullable String beginIndex, @Nullable String endIndex,
      @Nullable String... smsSenderNumbers) {
    this.beginIndex = beginIndex;
    this.endIndex = endIndex;
    this.smsSenderNumbers = smsSenderNumbers;
  }

  @Nullable public String getBeginIndex() {
    return beginIndex;
  }

  @Nullable public String getEndIndex() {
    return endIndex;
  }

  @Nullable public String[] getSmsSenderNumbers() {
    return smsSenderNumbers;
  }
}
