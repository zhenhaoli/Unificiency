package lmu.de.unificiencyandroid.network;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.logger.Logger;


public class UnificiencyFCMIDService extends FirebaseInstanceIdService {
  @Override
  public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Logger.i("Refreshed token: " + refreshedToken);

    sendRegistrationToServer(refreshedToken);
  }

  // TODO: Implement this method to send any registration to your app's servers.
  public void sendRegistrationToServer(String token){

  }
}




