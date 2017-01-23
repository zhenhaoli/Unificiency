package lmu.de.unificiencyandroid.components.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;
import lmu.de.unificiencyandroid.utils.Validate;


public class GroupEdit extends AppCompatActivity {

  @BindView(R.id.save_edit)
  Button save;

  @BindView(R.id.group_description)
  TextInputLayout descriptionTextInput;

  @BindView(R.id.group_topic)
  TextInputLayout topicTextInput;

  @BindView(R.id.my_toolbar)
  Toolbar toolbar;

  @OnTextChanged(R.id.desc)
  public void validateGroupDescription(){
    Validate.requiredMinLength(descriptionTextInput, 9, getString(R.string.groups_new_group_error_description));
  }

  @OnClick(R.id.save_edit)
  public void setGroupInfo() {
    boolean groupDescOk = Validate.requiredMinLength(descriptionTextInput, 9, getString(R.string.groups_new_group_error_description));
    if(!groupDescOk) {
      Message.fail(GroupEdit.this, getString(R.string.invalid_input));
      return;
    }

    String topic = topicTextInput.getEditText().getText().toString();
    String description = descriptionTextInput.getEditText().getText().toString();

    final Integer groupId = getIntent().getExtras().getInt("groupId");

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();
    params.put("topic_area", topic);
    params.put("description", description);
    params.setUseJsonStreamer(true);

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.put("groups/" + groupId + "/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {

          Intent intent = new Intent();
          intent.putExtra("saveSuccess", "Speicherung erfolgreich");
          intent.putExtra("groupId", groupId);
          setResult(Activity.RESULT_OK,intent);
          finish();

        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
        Message.fail(GroupEdit.this, errmsg);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        Message.fail(GroupEdit.this, responseString);
        Logger.e(responseString);
      }
    });
  }

  public void getGroupInfo() {

    Integer groupId = getIntent().getExtras().getInt("groupId");
    if (groupId == null) {
      Message.fail(this, "Gruppe konnte nicht gefunden werden, bitte nochmal versuchen");
      return;
    }

    String authToken = SharedPref.getDefaults("authToken", getApplicationContext());
    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);

    client.get("groups/" + groupId, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject groupJSON) {
        String topic = null;
        String description = null;
        try {
          topic = groupJSON.getString("topic_area");
          description = groupJSON.getString("description");
        } catch (Exception e) {
          Logger.e(e, "Exception");
          Message.fail(GroupEdit.this, e.toString());
        }
        topicTextInput.getEditText().setText(topic);
        descriptionTextInput.getEditText().setText(description);
      }
    });
  }



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.group_details_edit);
    ButterKnife.bind(this);

    setupToolbar();
    getGroupInfo();
  }

  public void setupToolbar(){
    toolbar.setTitle(R.string.toolbar_edit_group);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
  }

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
