package lmu.de.unificiencyandroid.view.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;

public class RegisterActivity extends AuthActivity {
    //TODO: Load this from CSV OR DB, instead matching begin only match anyword in between too
    private String[] majors ={"C","C++","Java",".NET","iPhone","Android","ASP.NET","PHP"};

    private Button createAccountButton;

    private TextInputLayout usernameWrapper;
    private TextInputLayout nicknameWrapper;
    private TextInputLayout passwordWrapper;
    private TextInputLayout passwordConfirmWrapper;
    private TextInputLayout majorWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton= (Button) findViewById(R.id.register);

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        nicknameWrapper = (TextInputLayout) findViewById(R.id.nicknameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        passwordConfirmWrapper = (TextInputLayout) findViewById(R.id.passwordConfirmWrapper);
        majorWrapper = (TextInputLayout) findViewById(R.id.majorWrapper);


        /** auto complete major input based on defined list */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,majors);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv= (AutoCompleteTextView)findViewById(R.id.major);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                //TODO: If received Oauth Token??
                if(validateEmail(username)&&validatePassword(password)&&passwordsEqual(password, passwordConfirm)) {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    passwordConfirmWrapper.setErrorEnabled(false);

                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    loginIntent.putExtra("registerSuccess", "Registrierung erfolgreich");

                    startActivity(loginIntent);
                }
            }

        });

    }

    private boolean passwordsEqual(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}
