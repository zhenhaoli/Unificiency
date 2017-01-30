package lmu.de.unificiencyandroid.components.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.LoadingUtils;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;
import lmu.de.unificiencyandroid.utils.Validate;

public class LoginActivity extends AuthActivity {

  @BindView(R.id.usernameWrapper)
  TextInputLayout usernameWrapper;

  @BindView(R.id.passwordWrapper)
  TextInputLayout passwordWrapper;

  @BindView(R.id.username)
  EditText usernameEditText;

  @BindView(R.id.password)
  EditText passwordEditText;

  @BindView(R.id.google_progress)
  GoogleProgressBar googleProgressBar;

  @BindView(R.id.login_layout)
  View layout;

  @OnTextChanged(R.id.username)
  public void validateUsername(){
    Validate.email(usernameWrapper, this);
  }

  @OnTextChanged(R.id.password)
  public void validatePassword(){
    Validate.password(passwordWrapper, this);
  }

 // @OnClick(R.id.chat)
 // public void chat() {
 //   startActivity(new Intent(this, Chat.class));
 // }

  @OnClick(R.id.register)
  public void register() {
    Intent register_Intent = new Intent(LoginActivity.this, RegisterActivity.class);
    startActivityForResult(register_Intent, 1);
  }

  @OnClick(R.id.login)
  public void login() {

    if(!isNetworkAvailable()){
      Message.fail(LoginActivity.this, "Keine Internet Verbindung! Bitte stelle sicher, das Gerät mit dem Internet zu verbinden!");
      return;
    }

    hideKeyboard();

    String username = usernameWrapper.getEditText().getText().toString();
    String password = passwordWrapper.getEditText().getText().toString();

    boolean validInput = (Validate.email(usernameWrapper, this)&&Validate.password(passwordWrapper, this));

    if(validInput) {

      googleProgressBar.setVisibility(View.VISIBLE);
      LoadingUtils.enableView(layout, false);

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

          finish();

          Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
          startActivity(loginIntent);

          googleProgressBar.setVisibility(View.INVISIBLE);
          LoadingUtils.enableView(layout, true);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          String errMsg =  errorResponse.toString();
          if(errMsg.contains("user")||errMsg.contains("password")){
            errMsg = "Email oder Passwort falsch!";
          }
          Message.fail(LoginActivity.this, errMsg);
          LoadingUtils.enableView(layout, true);
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
    usernameWrapper.getEditText().setText("Unificiency2017@LMU.de");
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
