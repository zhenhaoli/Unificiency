package lmu.de.unificiencyandroid.view.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lmu.de.unificiencyandroid.R;

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

        //TODO: validate nickname and major not being empty

        //TODO: If received Oauth Token??
        if(validateEmail(username)&&validatePassword(password)&&passwordsEqual(password, passwordConfirm)) {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            passwordConfirmWrapper.setErrorEnabled(false);

            Intent returnToLoginIntent = new Intent();
            returnToLoginIntent.putExtra("registerSuccess", "Registrierung erfolgreich");
            setResult(Activity.RESULT_OK,returnToLoginIntent);
            finish();
        }
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
        usernameWrapper.getEditText().setText("asd@sdss");
        passwordWrapper.getEditText().setText("asd@sdss");
        passwordConfirmWrapper.getEditText().setText("asd@sdss");

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
