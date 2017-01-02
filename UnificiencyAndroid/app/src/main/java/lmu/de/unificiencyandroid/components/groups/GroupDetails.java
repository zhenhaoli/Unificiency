package lmu.de.unificiencyandroid.components.groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.adapters.GroupMemberAdapter;
import lmu.de.unificiencyandroid.components.groups.interfaces.EnterGroupPasswordListener;
import lmu.de.unificiencyandroid.components.groups.models.Group;

public class GroupDetails extends AppCompatActivity implements EnterGroupPasswordListener {
    /*extras : groups_details_groupname_extra*/
    private GroupMemberAdapter adapter;
    private TextView hero;
    private ListView memberList;
    private TextView description;
    private Button joinButton;
    private ImageView toolbar;
    private Group group;


    public Group fetchGroupDetails(String groupName){
        /*later: http request with name as parameter*/
        ArrayList<String> member = new ArrayList<String>();
        member.addAll(Arrays.asList("Rob", "Jin", "Zhen"));
        String description = "Wir sind eine Gruppe ohne Namen. Eine Beschreibung müssen wir uns noch ausdenken ...";
        Group mockGroup = new Group(groupName,description, member);
        this.group = mockGroup;
        return this.group;
    }

    public String handleIntent(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String extra = intent.getStringExtra(getResources().getString(R.string.groups_details_groupname_extra));
        return extra;
    }

    public void setupViewReferences() {
        this.hero = (TextView) findViewById(R.id.groups_details_hero);
        this.memberList = (ListView) findViewById(R.id.group_details_member);
        this.description = (TextView) findViewById(R.id.groups_details_description);
        this.joinButton = (Button) findViewById(R.id.groups_details_join);
        this.toolbar = (ImageView) findViewById(R.id.groups_details_backButton);
    }

    public void bindGroupData() {
        this.hero.setText(this.group.getName());
        this.description.setText(this.group.getDescription());
        this.adapter = new GroupMemberAdapter(this, this.group.getMember());
        this.memberList.setAdapter(this.adapter);
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onJoin(View view){
        EnterGroupPassword enterPwDialog = new EnterGroupPassword();
        enterPwDialog.show(getSupportFragmentManager(), "enter_password");
    }

    @Override
    public void onPwEntered(String pw) {
        Log.i("Password retrievied: ", pw);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** View setup **/
        setContentView(R.layout.activity_group_details);
        setupViewReferences();
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(v);
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJoin(v);
            }
        });
        /** Data handling **/
        String groupNameExtra = handleIntent();
        fetchGroupDetails(groupNameExtra); //this is where we will dispatch a http request to our server
       /* View population/data binding and styling */
        ColorGenerator generator = ColorGenerator.MATERIAL;
        this.hero.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null));
        bindGroupData();
        }
}
