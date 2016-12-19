package lmu.de.unificiencyandroid.view.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import lmu.de.unificiencyandroid.R;

public class GroupDetails extends AppCompatActivity {

    private TextView hero;
    private ListView memberList;
    private TextView description;
    private Button joinButton;
    private ImageView toolbar;


    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String groupName = intent.getStringExtra(getResources().getString(R.string.groups_extras_details_groupname_extra));
        // Inflate the layout for this fragment
        //View view = getLayoutInflater().inflate(R.layout.activity_group_details, null);
        this.hero = (TextView) findViewById(R.id.groups_details_hero);
        this.memberList = (ListView) findViewById(R.id.group_details_member);
        this.description = (TextView) findViewById(R.id.groups_details_description);
        this.joinButton = (Button) findViewById(R.id.groups_details_join);
        this.hero.setText(groupName);
        this.toolbar = (ImageView) findViewById(R.id.groups_details_backButton);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(v);
            }
        });
        ColorGenerator generator = ColorGenerator.MATERIAL;
        this.hero.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null));
        }


}
