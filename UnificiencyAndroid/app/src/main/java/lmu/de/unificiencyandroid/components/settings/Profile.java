package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class Profile extends Fragment {
  TextView name_Account, email_Account, major_Account;
  TextView stars_nummber, notes_nummber;
  RoundImageView roundImageView;
  FloatingActionButton edit_btn;

  public void getUserInfo() {
    String authToken =  SharedPref.getDefaults("authToken", getContext());

    final RequestParams params = new RequestParams();

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("users/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {

          String nickName = response.getString("username");
          String majorName = response.getString("major");
          String email = response.getString("email");

          name_Account.setText(nickName);
          major_Account.setText(majorName);
          email_Account.setText(email);

        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, String errmsg, Throwable throwable) {
        Logger.e(errmsg);
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view =  inflater.inflate(R.layout.account,null);

    name_Account= (TextView) view.findViewById(R.id.name_account);
    email_Account= (TextView) view.findViewById(R.id.email_account);
    major_Account= (TextView) view.findViewById(R.id.major_account);

    stars_nummber=(TextView) view.findViewById(R.id.stars_nummber);
    notes_nummber=(TextView) view.findViewById(R.id.notes_nummber);

    edit_btn=(FloatingActionButton) view.findViewById(R.id.edit_floating_button);

    edit_btn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent intent= new Intent(getContext(), ProfileEdit.class);
        startActivityForResult(intent, 1);
      }
    });

    stars_nummber.setText("18");
    notes_nummber.setText("6");

    getUserInfo();

    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String message;

        getUserInfo();

        if (extras != null) {
          message = extras.getString("saveSuccess");
          Message.success(getContext(), message);
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Logger.d("user canceled editing profile");
      }
    }
  }//onActivityResult
}
