package lmu.de.unificiencyandroid.view.buildings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.model.Building;


public class BuildungDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildung_details);

        Intent intent = getIntent();

    }
}
