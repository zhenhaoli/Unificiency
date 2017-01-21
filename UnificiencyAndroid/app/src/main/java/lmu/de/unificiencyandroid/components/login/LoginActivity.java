package lmu.de.unificiencyandroid.components.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class LoginActivity extends AuthActivity {

  @BindView(R.id.usernameWrapper)
  TextInputLayout usernameWrapper;

  @BindView(R.id.passwordWrapper)
  TextInputLayout passwordWrapper;

  @OnClick(R.id.register)
  void register() {
    Intent register_Intent= new Intent(LoginActivity.this, RegisterActivity.class);
    startActivityForResult(register_Intent, 1);
  }

  @OnClick(R.id.login)
  void login() {

    if(!isNetworkAvailable()){
      Message.fail(LoginActivity.this, "Keine Internet Verbindung! Bitte stelle sicher, das Gerät mit dem Internet zu verbinden!");
      return;
    }

    hideKeyboard();

    String username = usernameWrapper.getEditText().getText().toString();
    String password = passwordWrapper.getEditText().getText().toString();

    usernameWrapper.setError(null);
    usernameWrapper.setErrorEnabled(false);
    passwordWrapper.setError(null);
    passwordWrapper.setErrorEnabled(false);

    if(!validateEmail(username)){
      usernameWrapper.setError(getString(R.string.validation_email));
    }
    if(!validatePassword(password)){
      passwordWrapper.setError(getString(R.string.validation_password));
    }

    boolean validInput = (validateEmail(username)&&validatePassword(password));

    if(validInput) {


      final RequestParams params = new RequestParams();
      params.put("email", username);
      params.put("password", password);
      params.setUseJsonStreamer(true);

      UnificiencyClient pythonClient = new PythonAPIClient();
      pythonClient.post("login/", params, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

          Logger.json(response.toString());

          String authToken = null;
          try {
            authToken = response.getString("token");
          } catch (Exception e) {
            Logger.e(e, "Exception");
          }

          SharedPref.setDefaults("authToken", authToken, getApplicationContext());
          Logger.i("authToken :" + authToken);
          usernameWrapper.setErrorEnabled(false);
          passwordWrapper.setErrorEnabled(false);

          Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
          startActivity(loginIntent);
          finish();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          String errMsg =  errorResponse.toString();
          if(errMsg.contains("user")||errMsg.contains("password")){
            errMsg = "Email oder Passwort falsch!";
          }
          Message.fail(LoginActivity.this, errMsg);
        }
      });
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_login);
    ButterKnife.bind(this);

    //TODO: remove this in prd
    usernameWrapper.getEditText().setText("testuser88@lmu.de");
    passwordWrapper.getEditText().setText("123456");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String registeredMsg;

        if (extras != null) {
          registeredMsg = extras.getString("registerSuccess");
          Message.success(LoginActivity.this, registeredMsg);
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Logger.d("User canceled registeration");
      }
    }
  }//onActivityResult

}
