package lmu.de.unificiencyandroid.components.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.LoadingUtils;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.Validate;

public class RegisterActivity extends AuthActivity {

  String[] majors ={"Medizin", "Informatik", "Medieninformatik", "Mensch-Maschine-Interaktion", "Physik", "Mathematik", "Statistik", "Jura", "Betriebswirtschaft"};

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

  @BindView(R.id.major)
  AutoCompleteTextView actv;

  @BindView(R.id.layout)
  View layout;

  @BindView(R.id.google_progress)
  GoogleProgressBar googleProgressBar;

  @OnTextChanged(R.id.username)
  public void validateUsername(){
    Validate.email(usernameWrapper, this);
  }

  @OnTextChanged(R.id.nickname)
  public void validateNickname(){
    Validate.required(nicknameWrapper, getString(R.string.validation_nickname));
  }

  @OnTextChanged(R.id.password)
  public void validatePassword(){
    Validate.password(passwordWrapper, this);
  }

  @OnTextChanged(R.id.passwordConfirm)
  public void validatePasswordConfirm(){
    Validate.password(passwordConfirmWrapper, this);
  }

  @OnClick(R.id.register)
  void register() {

    if(!isNetworkAvailable()){
      Message.fail(RegisterActivity.this, "Keine Internet Verbindung! Bitte stelle sicher, das Gerät mit dem Internet zu verbinden!");
      return;
    }

    hideKeyboard();

    String username = usernameWrapper.getEditText().getText().toString();
    String nickname = nicknameWrapper.getEditText().getText().toString();
    String password = passwordWrapper.getEditText().getText().toString();
    String passwordConfirm = passwordConfirmWrapper.getEditText().getText().toString();
    String major = majorWrapper.getEditText().getText().toString();

    boolean emailOk = Validate.email(usernameWrapper, this);
    boolean passwordOk = Validate.password(passwordWrapper, this);
    boolean passwordsOk = passwordsEqual(password, passwordConfirm);
    boolean nicknameOk =  Validate.required(nicknameWrapper, getString(R.string.validation_nickname));

    if(!passwordsOk) {
      passwordWrapper.setError(getString(R.string.validation_passwords_equal));
    }

    if(!nicknameOk){
      nicknameWrapper.setError(getString(R.string.validation_nickname));
    }

    if(emailOk && passwordOk && passwordsOk && nicknameOk) {
      doPost(username, nickname, password, major);
    } else {
      Message.fail(RegisterActivity.this, getString(R.string.invalid_input));
    }
  }

  private void doPost(String username, String nickname, String password, String major) {

    googleProgressBar.setVisibility(View.GONE);
    LoadingUtils.enableView(layout, false);

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
        Logger.json(response.toString());

        usernameWrapper.setErrorEnabled(false);
        passwordWrapper.setErrorEnabled(false);
        passwordConfirmWrapper.setErrorEnabled(false);

        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);

        Intent returnToLoginIntent = new Intent();
        returnToLoginIntent.putExtra("registerSuccess", "Registrierung erfolgreich");
        setResult(Activity.RESULT_OK,returnToLoginIntent);
        finish();

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Logger.e(errorResponse.toString());

        String failMsg = errorResponse.toString();
        if(failMsg.contains("username")){
          failMsg = "Nickname bereits vergeben, bitte einen anderen eingeben";
        } else if(failMsg.contains("email")){
          failMsg = "Dieses Email wird bereits verwendet, bitte ein anderes eingeben!";
        }
        Message.fail(RegisterActivity.this, failMsg);

        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);
      }
    });

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_register);

    ButterKnife.bind(this);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>
        (this,android.R.layout.select_dialog_item,majors);
    actv.setThreshold(1);
    actv.setAdapter(adapter);

    //TODO remove this in prd

    usernameWrapper.getEditText().setText("Unificiency2017@LMU.de");
    passwordWrapper.getEditText().setText("123456");
    passwordConfirmWrapper.getEditText().setText("123456");
    nicknameWrapper.getEditText().setText("Praktikant bei MSP");

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
