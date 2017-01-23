package lmu.de.unificiencyandroid.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

import java.util.regex.Pattern;

import lmu.de.unificiencyandroid.R;

public final class Validate {
  private static final String EMAIL_PATTERN = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)";
  private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

  private Validate() {}

  public static Boolean email(TextInputLayout textInputLayout, Context context) {
    String input = (textInputLayout.getEditText().getText().toString());
    boolean validEmail = !TextUtils.isEmpty(input) && pattern.matcher(input).matches();
    String errorMessage = context.getString(R.string.validation_email);

    updateUI(textInputLayout, validEmail, errorMessage);

    return validEmail;
  }

  public static Boolean password(TextInputLayout textInputLayout, Context context) {
    String input = (textInputLayout.getEditText().getText().toString());
    boolean validPassword =  !TextUtils.isEmpty(input) && input.length() > 5;
    String errorMessage = context.getString(R.string.validation_password);

    updateUI(textInputLayout, validPassword, errorMessage);

    return validPassword;
  }

  public static Boolean required(TextInputLayout textInputLayout, String errorMessage) {
    String input = (textInputLayout.getEditText().getText().toString());
    boolean fieldFilled =  !TextUtils.isEmpty(input);

    updateUI(textInputLayout, fieldFilled, errorMessage);

    return fieldFilled;
  }

  public static Boolean topic(TextInputLayout textInputLayout, Context context) {
    String input = (textInputLayout.getEditText().getText().toString());
    boolean validTopic =  !TextUtils.isEmpty(input) || input.length() > 3;
    String errorMessage = context.getString(R.string.validation_password);

    updateUI(textInputLayout, validTopic, errorMessage);

    return validTopic;
  }


  private static void updateUI(TextInputLayout textInputLayout, boolean validInput, String errorMessage){
    if(!validInput){
      textInputLayout.setError(errorMessage);
    } else {
      textInputLayout.setError(null);
      textInputLayout.setErrorEnabled(false);
    }
  }

}
