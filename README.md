# SMSReceiver

Dokumentasi versi Bahasa Indonesia [README_ID.md](https://github.com/joielechong/SMSReceiver/blob/master/README_ID.md)

**A Simple library to receive sms. Can be used for [OTP-SMS](https://en.wikipedia.org/wiki/One-time_password) handling.**

Support API Level >= 9

OTP-SMS is commonly used to verify user registration in Android application.
This library simplify the process to receive the SMS registration so developer don't need to implement SMS receiver to receive the broadcast message by the Android.

Only need 5 lines of codes to receive the sms!


### How to use

Step 1. Add the JitPack repository in your root build.gradle at the end of repositories:

    allprojects {
            repositories {
                   ...
                   maven { url "https://jitpack.io" }
            }
    }

Step 2. Add the dependency

    implementation 'com.github.joielechong:smsreceiver:0.9'

Step 3. Add the receiver

     // Start to receive the sms
     SmsReceiver.getInstance().register();
     SmsReceiver.getInstance().initialize();

     SmsReceiver.getInstance().setResultListener(new SmsReceiver.ResultListener() {
        @Override
        public void onSmsReceived(String sender, String message) {
             Toast.makeText(getBaseContext(),
                             "Sender = " + sender + ", " + "Message = " + message,
                             Toast.LENGTH_SHORT).show();
        }
      });

Step 4. Stop receiving the sms
You can stop receiving sms anytime. When in an Activity, you can stop receiving sms when the Activity is stopped or destroyed:


     @Override
     protected void onDestroy() {
         super.onDestroy();
         SmsReceiver.getInstance().unregister();
     }

### Configuration to receive SMS

- To receive all sms you only need to use the following:

         SmsReceiver.getInstance().initialize();

- To receive sms only from specific numbers, you can use the following:

         SmsReceiver.getInstance().initialize("082368636477");

         // Or
         SmsReceiver.getInstance().initialize("082368636477", "082368636478");

   you can specify any number as you want. There is no limitation of total number arguments.

- To receive sms from any number but with specific text constraint, for example, when waiting for OTP-SMS with the following format:

       Application activation code is GA-12345.

  where the activation code is GA-12345. You can use the following:

           SmsReceiver.getInstance().initialize("GA", "", "12345");

- You can also receive the activation code as the above regardless of the sender number with:

           SmsReceiver.getInstance().initialize("GA", "");


# Developed By

 * [Joielechong](http://www.github.com/joielechong)


# License

    Copyright 2018 Joielechong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
