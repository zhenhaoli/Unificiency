package lmu.de.unificiencyandroid.network;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.google.android.gms.internal.zzs.TAG;


public class UnificiencyFCMIDService extends FirebaseInstanceIdService {
  @Override
  public void onTokenRefresh() {
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);


    sendRegistrationToServer(refreshedToken);
  }

  // TODO: Implement this method to send any registration to your app's servers.
  public void sendRegistrationToServer(String token){

  }
}




