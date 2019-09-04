package com.betwin69.fcm;


import android.util.Log;

import com.betwin69.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//import com.classtune.ndc.utils.MyApplication;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage message) {


        Log.d("TAG", "From: " + message.getData());

        // Check if message contains a data payload.
//        if (message.getData().size() > 0) {
//            Log.d("TAG", "Message data payload: " + message.getData());
//
//        }
        //sendMyNotification(message.getData().get("order_id"), message.getData().get("title"), message.getData().get("body"));
       // message.getData().get("order_id");

        //setAlarm();
        if(message.getData().get("subject")!=null)
        MyApplication.sendMyNotification(message.getData().get("subject"),message.getData().get("message"), message.getData().get("target_type"), message.getData().get("target_id"), message.getData().get("target_view"));

    }


}
