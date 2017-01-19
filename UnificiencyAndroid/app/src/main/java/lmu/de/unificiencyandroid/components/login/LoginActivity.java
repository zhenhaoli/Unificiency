package lmu.de.unificiencyandroid.components.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
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

            Log.d("res python", response.toString());

            String tokenPython = null;
            try {
              tokenPython = response.getString("token");
            } catch (Exception e) {
              Log.e("JSON EER", e.toString());
            }

            SharedPref.setDefaults("authTokenPython", tokenPython, getApplicationContext());

            UnificiencyClient client = new NodeAPIClient();
            client.post("users/login", params, new JsonHttpResponseHandler() {
              @Override
              public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("res node", response.toString());

                String token = null;
                try {
                  token = response.getString("id_token");
                } catch (Exception e) {
                  Log.e("JSON EER", e.toString());
                }

                usernameWrapper.setErrorEnabled(false);
                passwordWrapper.setErrorEnabled(false);

                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                SharedPref.setDefaults("authToken", token, getApplicationContext());
                startActivity(loginIntent);
                finish();
              }

              @Override
              public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                String failedLogin = "Server Fehler, bitte später nochmal versuchen!";
                SuperActivityToast.cancelAllSuperToasts();
                SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                    .setText(failedLogin)
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_KITKAT)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
                    .setAnimations(Style.ANIMATIONS_SCALE)
                    .show();

              }

              @Override
              public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
              }

              @Override
              public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                String failedLogin = "Email oder Passwort falsch!";
                SuperActivityToast.cancelAllSuperToasts();
                SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                    .setText(failedLogin)
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_KITKAT)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
                    .setAnimations(Style.ANIMATIONS_SCALE)
                    .show();
              }
            });
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            String errMsg =  errorResponse.toString();
            if(errMsg.contains("user")||errMsg.contains("password")){
               errMsg = "Email oder Passwort falsch!";
            }
            SuperActivityToast.create(LoginActivity.this, new Style(), Style.TYPE_STANDARD)
                .setText(errMsg)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_KITKAT)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
                .setAnimations(Style.ANIMATIONS_SCALE)
                .show();
          }
        });


      }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    //TODO: remove this in prd
    usernameWrapper.getEditText().setText("unificiency2@lmu.de");
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
          SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
              .setText(registeredMsg)
              .setDuration(Style.DURATION_LONG)
              .setFrame(Style.FRAME_KITKAT)
              .setColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null))
              .setAnimations(Style.ANIMATIONS_SCALE)
              .show();
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        //Write your code if there's no result
      }
    }
  }//onActivityResult

}
