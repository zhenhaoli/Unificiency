package lmu.de.unificiencyandroid.view.buildings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.model.Building;


public class BuildungDetails extends AppCompatActivity {


    @BindView(R.id.img_details)
    ImageView imgView;

    @BindView(R.id.text_details)
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildung_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Building building= (Building) Parcels.unwrap(getIntent().getParcelableExtra("building"));
        textView.setText(building.toString());



    }
}
