/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.com.vipmeetings.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.Random;

import vip.com.vipmeetings.R;
import vip.com.vipmeetings.SplashScreen;
import vip.com.vipmeetings.VIPmeetings;
import vip.com.vipmeetings.evacuate.IpAddress;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    EventBus eventBus;
    private int UNIQUE_REQUEST_CODE = 1111;
    Uri defaultSoundUri;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus = EventBus.getDefault();
    }


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        eventBus.postSticky(IpAddress.UPDATE);
        IpAddress.e(TAG, "From: " + remoteMessage.getFrom());

        IpAddress.e(TAG, "DATA: " + remoteMessage.getData());

        // Check if message contains a data payload.
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }


            sendNotification(data.get("body"), data.get("title"));
        } else if (remoteMessage.getNotification() != null) {

            String message = remoteMessage.getNotification().getBody();
            IpAddress.e(TAG, "Message Notification Body: " + message);

            // remoteMessage.getData()

            if (remoteMessage.getNotification().getTitle() != null) {

                sendNotification(message, remoteMessage.getNotification().getTitle());


            } else {
                sendNotification(message, "VIPadm");
            }

            // IpAddress.e("PAYLOAD", remoteMessage.getData().toString() + "");
            //Evacuation has been ended

//            if (message != null && message.equalsIgnoreCase
//                    ("Please follow the instructions given by the Area Responsible of your area.")) {
//
//            } else {
//
//            }
        } else {
            IpAddress.e(TAG, "null");
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    private NotificationCompat.MessagingStyle buildMessageList(String message, String title) {

        //Person person=Person.Builder

        NotificationCompat.MessagingStyle messagingStyle =
                new NotificationCompat.MessagingStyle("VIPadm")
                        .setConversationTitle(title);

        messagingStyle.addMessage(message, System.currentTimeMillis(), "VIPadm");
        return messagingStyle;
    }

//    private void sendNotification(String messageBody,String title) {
//        Intent intent = new Intent(this, SplashScreen.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = getString(R.string.channel);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        //.setCategory(Notification.CATEGORY_PROMO)
//                        .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
////                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
////                                R.drawable.ic_stat_vipadm_app___android_status_bar___4))
//                        .setContentTitle(title)
//                        .setContentText(messageBody)
//                        .setDefaults(Notification.DEFAULT_ALL)
//                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                        .setAutoCancel(true)
//                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setSound(defaultSoundUri)
//                      //  .setFullScreenIntent(pendingIntent,true)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//
//
//        }
//        NotificationManagerCompat   notificationManager1 = NotificationManagerCompat.from(getApplicationContext());
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        IpAddress.e(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
//    private void sendNotification(String messageBody, String title) {
//
//        try {
//            //  Intent intent = new Intent(getApplicationContext(), EvacuationNormal2.class);
//            Intent intent = new Intent(this, SplashScreen.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra(IpAddress.EVACUATION, true);
////            PendingIntent pendingIntent = PendingIntent.getActivity(this,
////                    1 /* Request code */,
////                    intent,
////                    // PendingIntent.FLAG_ONE_SHOT,
////                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                    0 /* Request code */,
//                    intent,
//                    PendingIntent.FLAG_ONE_SHOT
//                    //, 0
//            );
//
//
////            NotificationManagerCompat notificationManager =NotificationManagerCompat.from(
////                    this);
//
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
////            Notification notification = new Notification.Builder(this, CHANNEL_ID)
////                    .setStyle(new NotificationCompat.MessagingStyle("Me")
////                            .setConversationTitle("Team lunch")
////                            .addMessage("How about lunch?", timestamp4, "Coworker"))
////                    .build();
//
//            //notificationManager.flags |= Notification.FLAG_AUTO_CANCEL;
//
//            NotificationCompat.Builder notificationBuilder2 = null;
//            int notificationId = 0;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationChannel mChannel = new NotificationChannel(IpAddress.CHANNELID,
//                        getString(R.string.app_name), importance);
//                String description = "Chanel_Description";
//                mChannel.setDescription(description);
//                mChannel.enableLights(true);
//                mChannel.enableVibration(true);
//                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//
//                mChannel.setLightColor(ContextCompat.getColor(this, R.color.bg));
//                notificationManager.createNotificationChannel(mChannel);
//
//                notificationBuilder2 =
//                        new NotificationCompat.Builder(this, IpAddress.CHANNELID)
//                                .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
//                                .setContentTitle(title)
//                                .setContentText(messageBody)
//                                .setFullScreenIntent(pendingIntent, true)
//                                .setDefaults(Notification.DEFAULT_ALL);
//
//
////                notificationBuilder = new NotificationCompat.Builder(
////                        this, getString(R.string.app_name))
////                        .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
////                       // .setContentTitle(title)
////                        .setStyle(buildMessageList(messageBody,title))
////                        .setPriority(NotificationCompat.PRIORITY_HIGH)
////                        .setAutoCancel(true)
////                        .setContentIntent(pendingIntent);
//
////                notificationBuilder = new Notification.Builder(
////                        this,IpAddress.CHANNELID)
////                        .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
////
////                      //  .setContentTitle(title)
////                      //   .setContentText(messageBody)
////                          .setStyle(new Notification.BigTextStyle()
////                                  .setBigContentTitle(title)
////                                .bigText(messageBody)
////                        )
////                        .setAutoCancel(true)
////                        //.setSound(defaultSoundUri)
////                        .setContentIntent(pendingIntent);
//                notificationManager.notify(notificationId /* ID of notification */,
//                        notificationBuilder2.build());
//            } else {
//
//                // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                notificationBuilder2 = new NotificationCompat.Builder(
//                        this, IpAddress.CHANNELID)
//                        .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
//                        .setContentTitle(title)
//                        .setFullScreenIntent(pendingIntent, true)
//                        .setContentText(messageBody)
////                        .setStyle(new NotificationCompat.BigTextStyle()
////                                .setBigContentTitle(title)
////                                .bigText(messageBody)
////                        )
//                        .setDefaults(Notification.DEFAULT_ALL)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent);
//
//                notificationManager.notify(notificationId /* ID of notification */,
//                        notificationBuilder2.build());
//            }
//
//
//        } catch (Exception e) {
//            Crashlytics.logException(e.getCause());
//        }
//    }
    private void sendNotification(String messageBody, String title) {

        if (messageBody != null && messageBody.length() > 0) {


        } else {

            messageBody = "";
        }

        if (title != null && title.length() > 0) {


        } else {
            title = "";

        }

        Intent backIntent = new Intent(getApplicationContext(), SplashScreen.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        backIntent.putExtra(IpAddress.EVACUATION, true);
       // PendingIntent pendingIntent=null;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent= PendingIntent.getActivity(
                    getApplicationContext(), UNIQUE_REQUEST_CODE++, backIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent= PendingIntent.getActivity(getApplicationContext(), UNIQUE_REQUEST_CODE++, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }*/

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity
                    (getApplicationContext(), UNIQUE_REQUEST_CODE++, backIntent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity
                    (getApplicationContext(), UNIQUE_REQUEST_CODE++, backIntent, PendingIntent.FLAG_ONE_SHOT);
        }





     /* PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                UNIQUE_REQUEST_CODE++,
                backIntent,PendingIntent.FLAG_UPDATE_CURRENT);*///Commented by raparthy
      /*  PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                UNIQUE_REQUEST_CODE++,
                backIntent, PendingIntent.FLAG_UPDATE_CURRENT );*/


        try {

            if (getApp().isForeground()) {

            } else {
                defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getPackageName() + "/" + R.raw.evacuation);

                // defaultSoundUri = Uri.parse("android.resource://"+getPackageName()+"/raw/evacuation.mp3");
                // The line below will set it as a default ring tone replace
                // RingtoneManager.TYPE_RINGTONE with RingtoneManager.TYPE_NOTIFICATION
                // to set it as a notification tone

                RingtoneManager.setActualDefaultRingtoneUri(
                        getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION,
                        defaultSoundUri);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                        defaultSoundUri);
                r.play();
            }
        } catch (Exception e) {
            IpAddress.e("noti", e.toString());
        }

        // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//         defaultSoundUri=  Uri.parse("android.resource://"
//                + getPackageName() + "/" + R.raw.evacuation);


        createNotificationChannel(title, messageBody, pendingIntent);


    }

    private void createNotificationChannel(String title, String messageBody, PendingIntent pendingIntent) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
//        NotificationManager notificationManager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager = null;
        // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = (NotificationManager)
                    getSystemService(NotificationManager.class);
        } else {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            IpAddress.e("app", getApp().isForeground() + "");
            try {

                notificationBuilder = new NotificationCompat.Builder(this,
                        IpAddress.CHANNELID)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)

                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setBigContentTitle(title)
                                .bigText(messageBody)
                        )
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{100, 200, 500, 1000})
                        .setDefaults(NotificationManagerCompat.IMPORTANCE_HIGH)
                        .setWhen(System.currentTimeMillis())
                        .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                        // .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                if (getApp().isForeground()) {
//                    CharSequence name = IpAddress.CHANNELID;
//                    String description = getString(R.string.channel_description);
//                    int importance = NotificationManager.IMPORTANCE_LOW;
//                    NotificationChannel channel = new NotificationChannel(IpAddress.CHANNELID, name, importance);
//                    channel.setDescription(description);
//                    channel.enableVibration(true);
//                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
//                            .build();
//                    channel.setSound(defaultSoundUri, audioAttributes);
//                    channel.setImportance(NotificationManager.IMPORTANCE_LOW);
//                    channel.enableLights(true);
//                  //  channel.canShowBadge();
//                   // channel.setShowBadge(true);
                    notificationManager.deleteNotificationChannel(IpAddress.CHANNELID);
                    //  notificationManager.createNotificationChannel(channel);
                } else {

                    if (notificationManager != null) {
                        List<NotificationChannel> channelList =
                                notificationManager.getNotificationChannels();

                        for (int i = 0; channelList != null && i < channelList.size(); i++) {
                            notificationManager.deleteNotificationChannel(channelList.get(i).getId());
                        }
                    }
                    CharSequence name = IpAddress.CHANNELID;
                    String description = getString(R.string.channel_description);
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel(IpAddress.CHANNELID, name, importance);

                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            // .setFlags(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();


                    channel.setDescription(description);
                    channel.enableVibration(true);

//                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
//                            .build();
                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    channel.setSound(defaultSoundUri, attributes);
                    // channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                    channel.enableLights(true);
//                    channel.canShowBadge();
//                    channel.setShowBadge(true);
                    //  notificationManager.deleteNotificationChannel(IpAddress.CHANNELID);
                    notificationManager.createNotificationChannel(channel);
                }


            } catch (Exception e) {

            }
            notificationManager.notify(generateRandom(), notificationBuilder.build());
        } else {

            notificationBuilder = new NotificationCompat.Builder(this,
                    IpAddress.CHANNELID)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setSmallIcon(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .setBigContentTitle(title)
                            .bigText(messageBody)
                    )
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setVibrate(new long[]{500, 500, 500, 500, 500})
                    .setSound(defaultSoundUri)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setBadgeIconType(R.drawable.ic_stat_vipadm_app___android_status_bar___4)
                    .setContentIntent(pendingIntent);

            // NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
            //       .from(this);

            // notificationManagerCompat.notify(generateRandom() /* ID of notification */, notificationBuilder.build());


            notificationManager.notify(generateRandom(), notificationBuilder.build());
        }
    }

    private VIPmeetings getApp() {

        return (VIPmeetings) getApplicationContext();
    }

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}