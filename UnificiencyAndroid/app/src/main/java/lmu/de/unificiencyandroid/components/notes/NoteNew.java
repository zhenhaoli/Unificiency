package lmu.de.unificiencyandroid.components.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class NoteNew extends AppCompatActivity {

  @BindView(R.id.my_toolbar_note)
  Toolbar toolbar;

  @BindView(R.id.notes_new_new_topic)
  TextInputLayout topic;

  @BindView(R.id.notes_new_note_name)
  TextInputLayout name;

  @BindView(R.id.notes_new_note_content)
  TextInputLayout content;

  @BindView(R.id.notes_new_note_create)
  Button createButton;

  @BindView(R.id.spinner)
  fr.ganfra.materialspinner.MaterialSpinner spinner;

  //@BindView(R.id.photo_new_note)
  //Button addPhoto;

 // @BindView(R.id.note_photo)
 // LinearLayout note_photo;

  ArrayList<String> groupsNames= new ArrayList();
  Map<String , Integer> groupsNamesMap = new HashMap<>();
  Map<String , Integer> groupsNamesIdMap = new HashMap<>();

  //@OnClick(R.id.photo_new_note)
  //public void addPhoto(){
    //note_photo.setVisibility(View.VISIBLE);
    //Intent intent = new Intent(getApplicationContext(),ChoosePhoto.class);
    //startActivity(intent);
 // }

  @OnClick(R.id.notes_new_note_create)
  public void uploadNote(){
    Integer groupId = 0;
    String defaultGroupName = getIntent().getStringExtra("groupname");
    if(!TextUtils.isEmpty(defaultGroupName)){
      groupId = groupsNamesIdMap.get(defaultGroupName);
    }

    String name = this.name.getEditText().getText().toString();
    String content = this.content.getEditText().getText().toString();
    String topic =  this.topic.getEditText().getText().toString();

    final RequestParams params = new RequestParams();
    params.put("topic", topic);
    params.put("name", name);
    params.put("content", content);
    params.put("groupId", groupId);
    params.setUseJsonStreamer(true);

    UnificiencyClient client = new NodeAPIClient();
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    client.addHeader("Authorization", authToken);
    client.post("groups/" + groupId + "/notes/", params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Logger.json(response.toString());

        Intent intent = new Intent();
        intent.putExtra("success", "Note erfolgreich erstellt!");
        setResult(Activity.RESULT_OK,intent);
        finish();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        String errMsg = null;
        try {
          errMsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }

        Message.fail(NoteNew.this, errMsg);
      }
    });
  }

  public void setupToolbar(){
    toolbar.setTitle(R.string.notes_new_note_title);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.note_new);
    ButterKnife.bind(this);
    setupToolbar();
    chooseGroupName();
  }

  public void chooseGroupName() {

    final RequestParams params = new RequestParams();
    params.put("isMember", true);

    UnificiencyClient client = new PythonAPIClient();
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    client.addHeader("Authorization", authToken);
    client.get("groups/LMU", params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray groups) {

        Logger.d(groups.length() + " groups got");

        try {
          for(int i=0; i<groups.length(); i++){
            Integer id = groups.getJSONObject(i).getInt("id");
            String name = groups.getJSONObject(i).getString("name");
            groupsNames.add(name);
            groupsNamesMap.put(name,i);
            groupsNamesIdMap.put(name, id);
          }
        }catch (Exception e) {
          Logger.e(e, "Exception");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(NoteNew.this, android.R.layout.simple_spinner_item, groupsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setDfaultGroupName();
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        String errMsg = null;
        try {
          errMsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
        Message.fail(NoteNew.this, errMsg);
      }
    });
  }

  public void setDfaultGroupName() {
    String defaultGroupName = getIntent().getStringExtra("groupname");
    if(!TextUtils.isEmpty(defaultGroupName)){
      int index = groupsNamesMap.get(defaultGroupName);
      spinner.setSelection(index);
    }
  }

  /* restore back button functionality*/
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case android.R.id.home: {
        onBackPressed();
        return true;
      }
      default:{return super.onOptionsItemSelected(item);}

    }
  }

}
