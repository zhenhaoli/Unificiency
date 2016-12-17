package lmu.de.unificiencyandroid.view.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;

import static android.R.attr.data;

public class LoginActivity extends AuthActivity {

    private Button loginButton;
    private Button registerButton;

    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        String registeredMsg;

        if (extras != null) {
            registeredMsg = extras.getString("registerSuccess");
            SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
                    .setText(registeredMsg)
                    .setDuration(Style.DURATION_LONG)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null))
                    .setAnimations(Style.ANIMATIONS_FADE)
                    .show();
        }

        loginButton= (Button) findViewById(R.id.login);
        registerButton= (Button) findViewById(R.id.register);
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        usernameWrapper.setHint(getString(R.string.username));
        passwordWrapper.setHint(getString(R.string.password));

        //TODO: remove this in prd
        usernameWrapper.getEditText().setText("WeAreBest@Jindoing");
        passwordWrapper.getEditText().setText("WeAreBest@Jindoing");

        loginButton.setOnClickListener(new View.OnClickListener() {
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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register_Intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register_Intent);

            }

        });

    }






}
