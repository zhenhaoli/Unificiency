package lmu.de.unificiencyandroid.components.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class GroupDetails extends AppCompatActivity implements GroupPasswordEnterListener {

  @BindView(R.id.group_details_name)
  TextView groupName;

  @BindView(R.id.group_details_member)
  ListView memberList;

  @BindView(R.id.groups_details_description)
  TextView description;

  @BindView(R.id.groups_details_join)
  Button joinButton;

  @BindView(R.id.groups_details_leave)
  Button leaveButton;

  @BindView(R.id.groups_details_modify)
  Button modifyButton;

  @BindView(R.id.groups_details_backButton)
  ImageView toolbar;

  GroupMemberAdapter adapter;
  Group group;
  Boolean isMemberInGroup;

  @OnClick(R.id.groups_details_join)
  public void tryToJoinGroup() {
    if(group.isHasPassword()) {
      GroupPasswordEnter enterPwDialog = new GroupPasswordEnter();
      enterPwDialog.show(getSupportFragmentManager(), "enter_password");
      return;
    }
    joinGroup(null);
  }

  @Override
  public void onPwEntered(String password) {
    joinGroup(password);
  }

  @OnClick(R.id.groups_details_leave)
  public void leaveGroup() {
    String authToken = SharedPref.getDefaults("authToken", getApplicationContext());

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.post("groups/" + group.getId() + "/leave/", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        String res = null;
        try {
          res = response.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }

        String[] msg = res.split("\\s+");
        String message = "Gruppe " + msg[msg.length - 1] + " erfolgreich verlassen";

        FirebaseMessaging.getInstance().unsubscribeFromTopic("group" + group.getId());
        Logger.i("Unsubscribed from " + "group" + group.getId());

        Message.success(GroupDetails.this, message);

        fetchGroupDetails(group.getId());
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Logger.e(errorResponse.toString());
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
        Message.fail(GroupDetails.this, errmsg);
      }

    });
  }


  @OnClick(R.id.groups_details_modify)
  public void editGroupInformation(){
    Intent intent= new Intent(this, GroupEdit.class);
    intent.putExtra("groupId", group.getId());
    startActivityForResult(intent, 1);
  }

  @OnClick(R.id.groups_details_backButton)
  public void goBack() {
    onBackPressed();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_details);
    ButterKnife.bind(this);

    Integer groupId = getIntent().getIntExtra("groupId", 0);
    fetchGroupDetails(groupId);
  }

  public void joinGroup(String password){
    RequestParams params = null;

    if(password!= null){
      params = new RequestParams();
      params.put("password", password);
      params.setUseJsonStreamer(true);
    }
    String authToken = SharedPref.getDefaults("authToken", getApplicationContext());

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.post("groups/" + group.getId() + "/join/", params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

        String res = null;
        try {
          res = response.getString("message");

        } catch (Exception e) {
          Logger.e(e, "Exception");
        }

        String[] msg = res.split("\\s+");
        String message = "Gruppe " + msg[msg.length - 1] + " erfolgreich beigetreten";

        FirebaseMessaging.getInstance().subscribeToTopic("group" + group.getId());
        Logger.i("Subscribed to " + "group" + group.getId());

        Message.success(GroupDetails.this, message);

        fetchGroupDetails(group.getId());

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Logger.e(errorResponse.toString());
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
        Message.fail(GroupDetails.this, errmsg);
      }

    });
  }

  public Group fetchGroupDetails(final Integer groupId){

    String authToken = SharedPref.getDefaults("authToken", getApplicationContext());
    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("groups/" + groupId, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject groupJSON) {
        String topic = null;
        String name = null;
        String description = null;
        Boolean hasPassword = null;
        Boolean isMember = null;
        List<String> memberNames = new ArrayList<String>();
        try {
          topic = groupJSON.getString("topic_area");
          name = groupJSON.getString("name");
          description = groupJSON.getString("description");
          hasPassword = groupJSON.getBoolean("protected");
          JSONArray membersJSON = groupJSON.getJSONArray("members");
          isMember = groupJSON.getBoolean("im_a_member");
          for(int j = 0; j<membersJSON.length(); j++){
            memberNames.add(membersJSON.getJSONObject(j).getString("username"));
          }
        } catch (Exception e){
          Logger.e(e, "Exception");
          Message.fail(GroupDetails.this, e.toString());
        }

        isMemberInGroup = isMember;
        group = new Group(groupId, name, topic, description, memberNames, hasPassword);

       /* View population/data binding and styling */
        ColorGenerator generator = ColorGenerator.MATERIAL;
        groupName.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null));
        bindGroupData();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Logger.e(errorResponse.toString());
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
        Message.fail(GroupDetails.this, errmsg);
      }
    });
    return group;
  }

  public void bindGroupData() {
    if(isMemberInGroup) {
      leaveButton.setVisibility(View.VISIBLE);
      joinButton.setVisibility(View.INVISIBLE);
      modifyButton.setVisibility(View.VISIBLE);
    } else {
      joinButton.setVisibility(View.VISIBLE);
      leaveButton.setVisibility(View.INVISIBLE);
      modifyButton.setVisibility(View.INVISIBLE);
    }

    this.groupName.setText(this.group.getName() + "[id: " + this.group.getId() + "]");
    this.description.setText(this.group.getDescription());
    this.adapter = new GroupMemberAdapter(this, this.group.getMembers());
    this.memberList.setAdapter(this.adapter);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String editedGroupMsg;

        if (extras != null) {
          fetchGroupDetails(extras.getInt("groupId"));
          editedGroupMsg = extras.getString("saveSuccess");
          Message.success(this, editedGroupMsg);
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Logger.d("User canceled group edit");
      }
    }
  }//onActivityResult

}
