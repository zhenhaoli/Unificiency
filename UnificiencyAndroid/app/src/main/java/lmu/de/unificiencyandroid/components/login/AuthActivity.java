package lmu.de.unificiencyandroid.components.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AuthActivity extends AppCompatActivity {
  private static final String EMAIL_PATTERN = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  private Matcher matcher;

  public boolean validateEmail(String email) {
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validatePassword(String password) {
    return password.length() > 5;
  }

  public boolean validateNickname(String name) {
    return name != null && name.length() > 0;
  }

  protected void hideKeyboard() {
    View view = getCurrentFocus();
    if (view != null) {
      ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
          hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }
}
