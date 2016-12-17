package lmu.de.unificiencyandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class Login_Activity extends AppCompatActivity {

    Button login_button;
    Button register_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button= (Button) findViewById(R.id.login);
        register_button= (Button) findViewById(R.id.login);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register_Intent= new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(register_Intent);

            }

        });

    }
}
