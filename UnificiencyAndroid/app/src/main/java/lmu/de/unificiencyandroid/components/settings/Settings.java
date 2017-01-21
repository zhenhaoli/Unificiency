package lmu.de.unificiencyandroid.components.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.login.LoginActivity;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class Settings extends Fragment {

  Button save,exit;

  TextInputEditText nickName_editor, majorName_editor,text_email;

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

          nickName_editor.setText(nickName);
          majorName_editor.setText(majorName);
          text_email.setText(email);

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

  public void setUserInfo() {

    String nickName = this.nickName_editor.getText().toString();
    String majorName = this.majorName_editor.getText().toString();
    String email = this.text_email.getText().toString();

    String authToken =  SharedPref.getDefaults("authToken", getContext());

    final RequestParams params = new RequestParams();
    params.put("username",nickName);
    params.put("email",email);
    params.put("major",majorName);
    params.setUseJsonStreamer(true);

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.put("users/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          Message.success(getContext(), response.toString());

        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Logger.e(errorResponse.toString());
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
        Message.fail(getContext(), errmsg);
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view =  inflater.inflate(R.layout.setting,null);

    save = (Button) view.findViewById(R.id.save_setting);
    exit = (Button) view.findViewById(R.id.exit_setting);

    nickName_editor= (TextInputEditText) view.findViewById(R.id.text_nickname);
    majorName_editor= (TextInputEditText) view.findViewById(R.id.text_major);
    text_email= (TextInputEditText) view.findViewById(R.id.text_email);

    getUserInfo();

    save.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        setUserInfo();
      }
    });
    exit.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Intent intent= new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
      }
    });

    return view;

  }
}
