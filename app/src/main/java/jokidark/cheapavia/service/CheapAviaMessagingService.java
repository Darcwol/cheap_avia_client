package jokidark.cheapavia.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class  CheapAviaMessagingService extends FirebaseMessagingService {
    public static final String TAG = "TestFbseInstIdSvc";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getFrom() != null) {
            int id = Integer.valueOf(remoteMessage.getFrom().replace("/topics/ticket_n", ""));
            Log.d(TAG, "Ticket #" + id);
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


}
