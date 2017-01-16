package lmu.de.unificiencyandroid.components.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;

public class RegisterActivity extends AuthActivity {
  //TODO: Load this from CSV OR DB, instead matching begin only match anyword in between too
  String[] majors ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};

  @BindView(R.id.register)
  Button createAccountButton;

  @BindView(R.id.usernameWrapper)
  TextInputLayout usernameWrapper;

  @BindView(R.id.nicknameWrapper)
  TextInputLayout nicknameWrapper;

  @BindView(R.id.passwordWrapper)
  TextInputLayout passwordWrapper;

  @BindView(R.id.passwordConfirmWrapper)
  TextInputLayout passwordConfirmWrapper;

  @BindView(R.id.majorWrapper)
  TextInputLayout majorWrapper;

  @OnClick(R.id.register)
  void register() {
    hideKeyboard();

    String username = usernameWrapper.getEditText().getText().toString();
    String nickname = nicknameWrapper.getEditText().getText().toString();
    String password = passwordWrapper.getEditText().getText().toString();
    String passwordConfirm = passwordConfirmWrapper.getEditText().getText().toString();
    String major = majorWrapper.getEditText().getText().toString();

    usernameWrapper.setError(null);
    usernameWrapper.setErrorEnabled(false);
    nicknameWrapper.setError(null);
    nicknameWrapper.setErrorEnabled(false);
    passwordWrapper.setError(null);
    passwordWrapper.setErrorEnabled(false);
    passwordConfirmWrapper.setError(null);
    passwordConfirmWrapper.setErrorEnabled(false);
    majorWrapper.setError(null);
    majorWrapper.setErrorEnabled(false);

    if(!validateEmail(username)){
      usernameWrapper.setError(getString(R.string.validation_email));
    }

    if(passwordsEqual(password, passwordConfirm)) {
      if (!validatePassword(password)) {
        passwordWrapper.setError(getString(R.string.validation_password));
      }

      if (!validatePassword(passwordConfirm)) {
        passwordConfirmWrapper.setError(getString(R.string.validation_password));
      }
    } else {
      passwordWrapper.setError(getString(R.string.validation_passwords_equal));
    }

    if(!validateNickname(nickname)){
      nicknameWrapper.setError(getString(R.string.validation_nickname));
    }

    if(validateEmail(username)&&validatePassword(password)&&passwordsEqual(password, passwordConfirm) && validateNickname(nickname)) {
      doPost(username, nickname, password, major);
    }

  }

  private void doPost(String username, String nickname, String password, String major) {
    final RequestParams params = new RequestParams();
    params.put("email", username);
    params.put("username", nickname);
    params.put("password", password);
    params.put("major", major);
    params.put("university_id", 1);
    params.setUseJsonStreamer(true);


    UnificiencyClient pythonClient = new PythonAPIClient();
    pythonClient.post("users/", params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // If the response is JSONObject instead of expected JSONArray
        Log.d("res", response.toString());

        UnificiencyClient client = new NodeAPIClient();
        client.post("users/register", params, new JsonHttpResponseHandler() {

          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // If the response is JSONObject instead of expected JSONArray
            Log.d("res", response.toString());

            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            passwordConfirmWrapper.setErrorEnabled(false);

            Intent returnToLoginIntent = new Intent();
            returnToLoginIntent.putExtra("registerSuccess", "Registrierung erfolgreich");
            setResult(Activity.RESULT_OK,returnToLoginIntent);
            finish();

          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            String failedLogin = "Dieses Email wird bereits verwendet, bitte ein anderes angeben!";
            SuperActivityToast.cancelAllSuperToasts();
            SuperActivityToast.create(RegisterActivity.this, new Style(), Style.TYPE_STANDARD)
                .setText(responseString)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_KITKAT)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
                .setAnimations(Style.ANIMATIONS_SCALE)
                .show();
          }
        });


      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        Log.d("res str", responseString.toString());
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        Log.e("res err str", responseString.toString());
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(RegisterActivity.this, new Style(), Style.TYPE_STANDARD)
            .setText(responseString)
            .setDuration(Style.DURATION_LONG)
            .setFrame(Style.FRAME_KITKAT)
            .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
            .setAnimations(Style.ANIMATIONS_SCALE)
            .show();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Log.e("res err js", errorResponse.toString());

        String failMsg = errorResponse.toString();
        if(failMsg.contains("username")){
          failMsg = "Nickname bereits vergeben, bitte einen anderen eingeben";
        } else if(failMsg.contains("email")){
          failMsg = "Dieses Email wird bereits verwendet, bitte ein anderes eingeben!";
        }
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(RegisterActivity.this, new Style(), Style.TYPE_STANDARD)
            .setText(failMsg)
            .setDuration(Style.DURATION_LONG)
            .setFrame(Style.FRAME_KITKAT)
            .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
            .setAnimations(Style.ANIMATIONS_SCALE)
            .show();
      }
    });




  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    ButterKnife.bind(this);

    /** auto complete major input based on defined list */
    ArrayAdapter<String> adapter = new ArrayAdapter<String>
        (this,android.R.layout.select_dialog_item,majors);
    //Getting the instance of AutoCompleteTextView
    AutoCompleteTextView actv= (AutoCompleteTextView)findViewById(R.id.major);
    actv.setThreshold(1);//will start working from first character
    actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

    //TODO remove this in prd
    usernameWrapper.getEditText().setText("NewUser@LMU.de");
    passwordWrapper.getEditText().setText("123456");
    passwordConfirmWrapper.getEditText().setText("123456");
    nicknameWrapper.getEditText().setText("MSPPraktikant");

  }

  @Override
  public void onBackPressed() {
    Intent returnToLoginIntent = new Intent();
    setResult(Activity.RESULT_CANCELED, returnToLoginIntent);
    finish();
  }

  boolean passwordsEqual(String password, String passwordConfirm) {
    return password.equals(passwordConfirm);
  }

}
