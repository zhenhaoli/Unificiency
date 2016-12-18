package lmu.de.unificiencyandroid.view.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;

import static android.R.attr.data;

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

        //TODO: If received Oauth Token??
        if(validateEmail(username)&&validatePassword(password)) {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //TODO: remove this in prd
        usernameWrapper.getEditText().setText("WeAreBest@Jindoing");
        passwordWrapper.getEditText().setText("WeAreBest@Jindoing");

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
