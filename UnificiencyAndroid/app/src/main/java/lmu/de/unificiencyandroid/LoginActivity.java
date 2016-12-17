package lmu.de.unificiencyandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    Button login_button;
    Button register_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button= (Button) findViewById(R.id.login);
        register_button= (Button) findViewById(R.id.register);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent= new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
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
}
