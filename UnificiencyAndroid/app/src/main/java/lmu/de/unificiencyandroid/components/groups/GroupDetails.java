package lmu.de.unificiencyandroid.components.groups;

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
import com.loopj.android.http.RequestParams;

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
import lmu.de.unificiencyandroid.utils.SharedPref;

public class GroupDetails extends AppCompatActivity implements GroupPasswordEnterListener {

  /*extras : groups_details_groupname_extra*/
  GroupMemberAdapter adapter;
  Group group;
  Boolean isMemberInGroup;

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

  @BindView(R.id.groups_details_backButton)
  ImageView toolbar;

  @OnClick(R.id.groups_details_join)
  void joinGroup() {
    Boolean groupHasPassword = group.getHasPassword();

    if(groupHasPassword) {
      GroupPasswordEnter enterPwDialog = new GroupPasswordEnter();
      enterPwDialog.show(getSupportFragmentManager(), "enter_password");

    } else {

      String authToken = SharedPref.getDefaults("authTokenPython", getApplicationContext());

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

  @OnClick(R.id.groups_details_leave)
  void leaveGroup() {
    String authToken = SharedPref.getDefaults("authTokenPython", getApplicationContext());

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.post("groups/" + group.getId() + "/leave/", null, new JsonHttpResponseHandler() {
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
        String res = "Gruppe " + msg[msg.length - 1] + " erfolgreich verlassen";

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


  @OnClick(R.id.groups_details_backButton)
  void goBack() {
    onBackPressed();
  }


  public Group fetchGroupDetails(final Integer groupId){
    String authToken =  SharedPref.getDefaults("authTokenPython", getApplicationContext());

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.get("groups/"+groupId, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject groupJSON) {
        String topic = null;
        String name= null;
        String description= null;
        Boolean hasPassword = null;
        Boolean isMember = null;
        List<String> memberNames = new ArrayList<String>();
        try {
          topic = groupJSON.getString("topic_area");
          name = groupJSON.getString("title");
          description = groupJSON.getString("description");
          hasPassword = groupJSON.getBoolean("protected");
          JSONArray membersJSON = groupJSON.getJSONArray("members");
          isMember = groupJSON.getBoolean("im_a_member");
          memberNames = new ArrayList<String>();
          for(int j = 0; j<membersJSON.length(); j++){
            memberNames.add(membersJSON.getJSONObject(j).getString("username"));
          }
        } catch (Exception e){
          Log.e("jsonerr", e.toString());
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


  public void bindGroupData() {
    if(isMemberInGroup) {
      leaveButton.setVisibility(View.VISIBLE);
      joinButton.setVisibility(View.INVISIBLE);
    } else {
      joinButton.setVisibility(View.VISIBLE);
      leaveButton.setVisibility(View.INVISIBLE);
    }

    this.groupName.setText(this.group.getName() + "[id: " + this.group.getId() + "]");
    this.description.setText(this.group.getDescription());
    this.adapter = new GroupMemberAdapter(this, this.group.getMembers());
    this.memberList.setAdapter(this.adapter);
  }

  @Override
  public void onPwEntered(String pw) {
    Log.i("Password retrievied: ", pw);
    String authToken = SharedPref.getDefaults("authTokenPython", getApplicationContext());

    Log.d("gd Token in sharedPref", authToken);

    final RequestParams params = new RequestParams();

    params.put("password", pw);
    params.setUseJsonStreamer(true);
    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.post("groups/" + group.getId() + "/join/", params, new JsonHttpResponseHandler() {
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

        if(errmsg.contains("protected")){
          // errmsg = "Password falsch oder nicht eingegeben, bitte nochmal versuchen!";
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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /** View setup **/
    setContentView(R.layout.group_details);

    ButterKnife.bind(this);

    /** Data handling **/
    Integer groupId = getIntent().getIntExtra("groupId", 0);
    fetchGroupDetails(groupId);
  }
}
