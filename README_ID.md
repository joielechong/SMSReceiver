# SMSReceiver

English version documentation [README.md](https://github.com/joielechong/SMSReceiver/blob/master/README.md)

**Library sederhana untuk menerima sms. Dapat dipergunakan untuk menangani [OTP-SMS](https://en.wikipedia.org/wiki/One-time_password).**

Mendukung API Level >= 9


OTP-SMS umumnya digunakan untuk melakukan verifikasi pengguna saat hendak menggunakan aplikasi Android.
Library in menyederhanakan proses penerimaan registrasi via SMS sehingga developer tidak perlu lagi mengimplementasikan penerima SMS untuk menerima pesan broadcast oleh Android.

Hanya membutuhkan kira-kira 7 baris kode untuk dapat menerima sms!

### Cara menggunakan

Langkah 1. tambahkan repositori JitPack di root build.gradle:

    allprojects {
            repositories {
                   ...
                   maven { url "https://jitpack.io" }
            }
    }

Langkah 2. Tambahkan dependency

    implementation 'com.github.joielechong:smsreceiver:0.9'

Langkah 3. Tambahkan receiver

     // mulai menerima sms
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

Langkah 4. Menghentikan penerimaan sms
Kamu dapat menghentikan penerimaan sms kapanpun. Saat di Activity, kamu dapat menghentikan penerimaan sms saat Activity dihentikan atau diselesaikan:


     @Override
     protected void onDestroy() {
         super.onDestroy();
         SmsReceiver.getInstance().unregister();
     }

### Konfigurasi untuk menerima SMS

- Untuk menerima semua sms kamu hanya perlu menggunakan kode berikut:

         SmsReceiver.getInstance().initialize();

- Untuk menerima sms hany dari nomor tertentu, kamu dapat menggunakan:

         SmsReceiver.getInstance().initialize("082368636477");

         // Atau
         SmsReceiver.getInstance().initialize("082368636477", "082368636478");

   kamu dapat menentukan sebanyak apapun nomor pengirimnya. Tidak ada batasan untuk jumlah nomor pengirim.

- Untuk menerima sms dari nomor apapun tetapi dengan batasan teks tertentu, sebagai contoh, saat menunggu OTP-SMS dengan format berikut:

       Application activation code is GA-12345.

  dimana kode aktivasi adalah GA-12345. Kamu dapat menggunakan:

           SmsReceiver.getInstance().initialize("GA", "", "12345");

- Kamu juga dapat menerima aktivasi kode seperti sebelumnya tanpa mempedulikan nomor pengirim dengan:

           SmsReceiver.getInstance().initialize("GA", "");


# Dikembangkan oleh

 * [Joielechong](http://www.github.com/joielechong)


# Lisensi

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
