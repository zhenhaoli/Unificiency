package lmu.de.unificiencyandroid.components.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
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
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.LoadingUtils;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;
import lmu.de.unificiencyandroid.utils.Validate;

public class GroupNew extends AppCompatActivity {

  @BindView(R.id.groups_new_group_create)
  Button createButton;

  @BindView(R.id.groups_new_group_topic)
  TextInputLayout inputLayoutTopic;

  @BindView(R.id.groups_new_group_description)
  TextInputLayout inputLayoutDesc;

  @BindView(R.id.groups_new_group_name)
  TextInputLayout inputLayoutName;

  @BindView(R.id.groups_new_group_password)
  TextInputLayout password;

  @BindView(R.id.name)
  TextInputEditText nameEditText;

  @BindView(R.id.layout)
  View layout;

  @BindView(R.id.google_progress_newGroup)
  GoogleProgressBar googleProgressBar;

  @BindView(R.id.desc)
  TextInputEditText descEditText;

  @OnTextChanged(R.id.topic)
  public void validateGroupTopic(){
    Validate.requiredMinLength(inputLayoutTopic, 2, getString(R.string.there_letters_min));
  }

  @OnTextChanged(R.id.name)
  public void validateGroupName(){
    Validate.requiredMinLength(inputLayoutName, 2, getString(R.string.there_letters_min));
  }

  @OnTextChanged(R.id.desc)
  public void validateGroupDescription(){
    Validate.requiredMinLength(inputLayoutDesc, 9, getString(R.string.groups_new_group_error_description));
  }

  @OnClick(R.id.groups_new_group_create)
  public void onCreateGroup() {
    String name = this.inputLayoutName.getEditText().getText().toString();
    String description = this.inputLayoutDesc.getEditText().getText().toString();
    String password = this.password.getEditText().getText().toString();
    String topic =  this.inputLayoutTopic.getEditText().getText().toString();

    boolean groupTopicOk = Validate.requiredMinLength(inputLayoutTopic, 2, getString(R.string.there_letters_min));
    boolean groupNameOk = Validate.requiredMinLength(inputLayoutName, 2, getString(R.string.there_letters_min));
    boolean groupDescOk = Validate.requiredMinLength(inputLayoutDesc, 9, getString(R.string.groups_new_group_error_description));

    if(groupTopicOk && groupNameOk && groupDescOk){

      final RequestParams params = new RequestParams();
      params.put("topic_area", topic);
      params.put("name", name);
      params.put("description", description);
      if(password!=null || password.length()>1) {
        params.put("password", password);
      }
      params.setUseJsonStreamer(true);

      showLoad(true);
      UnificiencyClient client = new PythonAPIClient();
      String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

      client.addHeader("Authorization", authToken);
      client.post("groups/", params, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          Logger.json(response.toString());

          Intent intent = new Intent();
          intent.putExtra("createGroupSuccess", "Gruppe erfolgreich erstellt!");
          setResult(Activity.RESULT_OK,intent);
          finish();
          showLoad(false);
          tellMiddleAboutNewGroup();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          String errMsg = null;
          try {
            errMsg = errorResponse.getString("message");
          } catch (Exception e) {
            Logger.e(e, "Exception");
          }

          Message.fail(GroupNew.this, errMsg);
          showLoad(false);
        }
      });

    } else {
      Message.fail(GroupNew.this, getString(R.string.invalid_input));
      showLoad(false);
    }
  }

  public void tellMiddleAboutNewGroup(){
    UnificiencyClient client = new NodeAPIClient();
    client.post("groups/", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Logger.json(response.toString());
        showLoad(false);
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_new);
    ButterKnife.bind(this);
    setupToolbar();

    //TODO: remove this in prd
   // this.inputLayoutName.getEditText().setText("My Crew <3");
   // this.inputLayoutDesc.getEditText().setText("A very long long long inputLayoutDesc to pass this validation");

    this.createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onCreateGroup();
      }
    });
  }

  public void setupToolbar(){
    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    toolbar.setTitle(R.string.groups_new_group_title);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
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

  public void showLoad(boolean show){
    googleProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    LoadingUtils.enableView(layout, !show);
  }
}
