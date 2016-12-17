package lmu.de.unificiencyandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button login_button;
    private Button register_button;

    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button= (Button) findViewById(R.id.login);
        register_button= (Button) findViewById(R.id.register);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        usernameWrapper.setHint(getString(R.string.username));
        passwordWrapper.setHint(getString(R.string.password));

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                //TODO: If received Oauth Token??
                if(validateEmail(username)&&validatePassword(password)) {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                }
            }

        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register_Intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register_Intent);

            }

        });

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        Log.d("pw length: ", ""+password.length());
        return password.length() > 5;
    }


}
