package lmu.de.unificiencyandroid.view.groups;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import lmu.de.unificiencyandroid.R;

import static java.security.AccessController.getContext;

public class GroupDetails extends AppCompatActivity {

    private TextView hero;
    private ListView memberList;
    private TextView description;
    private Button joinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        String groupName = intent.getStringExtra("group_name");
        // Inflate the layout for this fragment
        //View view = getLayoutInflater().inflate(R.layout.activity_group_details, null);
        this.hero = (TextView) findViewById(R.id.groups_details_hero);
        this.memberList = (ListView) findViewById(R.id.group_details_member);
        this.description = (TextView) findViewById(R.id.groups_details_description);
        this.joinButton = (Button) findViewById(R.id.groups_details_join);
        this.hero.setText(groupName);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        this.hero.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null));
        }



}
