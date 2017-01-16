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
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.adapters.GroupMemberAdapter;
import lmu.de.unificiencyandroid.components.groups.interfaces.EnterGroupPasswordListener;
import lmu.de.unificiencyandroid.components.groups.models.Group;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class GroupDetails extends AppCompatActivity implements EnterGroupPasswordListener {
  /*extras : groups_details_groupname_extra*/
  private GroupMemberAdapter adapter;
  private TextView groupName;
  private ListView memberList;
  private TextView description;
  private Button joinButton;
  private ImageView toolbar;
  private Group group;

  public Group fetchGroupDetails(final Integer groupId){
    String authToken =  SharedPref.getDefaults("authTokenPython", getApplicationContext());

    Log.d("gd Token in sharedPref", authToken);

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.get("groups/"+groupId, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject groupJSON) {
        // If the response is JSONObject instead of expected JSONArray
        String topic = null;
        String name= null;
        String description= null;
        List<String> memberNames = new ArrayList<String>();
        try {
          topic = groupJSON.getString("topic_area");
          name = groupJSON.getString("name");
          description = groupJSON.getString("description");
          JSONArray membersJSON = groupJSON.getJSONArray("members");
          memberNames = new ArrayList<String>();
          for(int j = 0; j<membersJSON.length(); j++){
            memberNames.add(membersJSON.getJSONObject(j).getString("username"));
          }
        } catch (Exception e){
          Log.e("jsonerr", e.toString());
        }

        group = new Group(groupId, name, topic, description, memberNames, null);

       /* View population/data binding and styling */
        ColorGenerator generator = ColorGenerator.MATERIAL;
        groupName.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null));
        bindGroupData();

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("groupdetailserr", errorResponse.toString());
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {

        }
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(GroupDetails.this, new Style(), Style.TYPE_STANDARD)
            .setText(errmsg)
            .setDuration(Style.DURATION_LONG)
            .setFrame(Style.FRAME_KITKAT)
            .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
            .setAnimations(Style.ANIMATIONS_SCALE)
            .show();

      }

    });
    return group;
  }

  public Integer handleIntent(){
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    Integer extra = intent.getIntExtra("groupId", 0);
    return extra;
  }

  public void setupViewReferences() {
    this.groupName = (TextView) findViewById(R.id.groups_details_hero);
    this.memberList = (ListView) findViewById(R.id.group_details_member);
    this.description = (TextView) findViewById(R.id.groups_details_description);
    this.joinButton = (Button) findViewById(R.id.groups_details_join);
    this.toolbar = (ImageView) findViewById(R.id.groups_details_backButton);
  }

  public void bindGroupData() {
    this.groupName.setText(this.group.getName() + "[id: " + this.group.getId() + "]");
    this.description.setText(this.group.getDescription());
    this.adapter = new GroupMemberAdapter(this, this.group.getMembers());
    this.memberList.setAdapter(this.adapter);
  }

  public void onBack(View view) {
    onBackPressed();
  }

  public void onJoin(View view){
    Boolean groupHasPassword = false;

    if(groupHasPassword) {
      EnterGroupPassword enterPwDialog = new EnterGroupPassword();
      enterPwDialog.show(getSupportFragmentManager(), "enter_password");

    } else {

      String authToken = SharedPref.getDefaults("authTokenPython", getApplicationContext());

      Log.d("gd Token in sharedPref", authToken);

      UnificiencyClient client = new PythonAPIClient();
      client.addHeader("Authorization", authToken);
      client.post("groups/" + group.getId() + "/join/", null, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          // If the response is JSONObject instead of expected JSONArray
          String resMessage = null;
          try {
            resMessage = response.getString("message");

          } catch (Exception e) {
            Log.e("jsonerr", e.toString());
          }

          String[] msg = resMessage.split("\\s+");
          String res = "Gruppe " + msg[msg.length - 1] + " erfolgreich beigetreten";

          SuperActivityToast.cancelAllSuperToasts();
          SuperActivityToast.create(GroupDetails.this, new Style(), Style.TYPE_STANDARD)
              .setText(res)
              .setDuration(Style.DURATION_LONG)
              .setFrame(Style.FRAME_KITKAT)
              .setColor(ResourcesCompat.getColor(getResources(), R.color.green_400, null))
              .setAnimations(Style.ANIMATIONS_SCALE)
              .show();

          fetchGroupDetails(group.getId());

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Log.e("groupdetailserr", errorResponse.toString());
          String errmsg = null;
          try {
            errmsg = errorResponse.getString("message");
          } catch (Exception e) {

          }
          SuperActivityToast.cancelAllSuperToasts();
          SuperActivityToast.create(GroupDetails.this, new Style(), Style.TYPE_STANDARD)
              .setText(errmsg)
              .setDuration(Style.DURATION_LONG)
              .setFrame(Style.FRAME_KITKAT)
              .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
              .setAnimations(Style.ANIMATIONS_SCALE)
              .show();

        }

      });

    }

  }

  @Override
  public void onPwEntered(String pw) {
    Log.i("Password retrievied: ", pw);
    //TODO POST JOIN for groups with pw
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
    Integer groupId = handleIntent();
    fetchGroupDetails(groupId);
  }
}
