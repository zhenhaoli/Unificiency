package lmu.de.unificiencyandroid.network;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;

public class UnificiencyFCMService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {

    Logger.d("From: " + remoteMessage.getFrom());

    if (remoteMessage.getData().size() > 0) {
      Logger.d("Message data payload: " + remoteMessage.getData());
      broadcastMessage(remoteMessage.getData().toString());
    }

    if (remoteMessage.getNotification() != null) {
      Logger.d("Message Notification Body: " + remoteMessage.getNotification().getBody());
    }

  }

  private void broadcastMessage(String message) {
    Intent intent = new Intent("ServerUpdates");
    intent.putExtra("Message", message);
    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
  }

}
