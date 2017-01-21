package lmu.de.unificiencyandroid.components.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.InputValidation.InputValidation;
import lmu.de.unificiencyandroid.InputValidation.ValidateGroupDescription;
import lmu.de.unificiencyandroid.InputValidation.ValidateGroupName;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class GroupNew extends AppCompatActivity {

  @BindView(R.id.groups_new_group_create)
  Button createButton;

  @BindView(R.id.groups_new_group_topic)
  TextInputLayout topic;

  @BindView(R.id.groups_new_group_description)
  TextInputLayout description;

  @BindView(R.id.groups_new_group_name)
  TextInputLayout name;

  @BindView(R.id.groups_new_group_password)
  TextInputLayout password;

  InputValidation nameValidator;
  InputValidation descriptionValidator;

  @OnClick(R.id.groups_new_group_create)
  public void onCreateGroup() {
    String name = this.name.getEditText().getText().toString();
    String description = this.description.getEditText().getText().toString();
    String password = this.password.getEditText().getText().toString();
    String topic =  this.topic.getEditText().getText().toString();

    if(this.nameValidator.validate(name) && this.descriptionValidator.validate(description)){

      final RequestParams params = new RequestParams();
      params.put("topic_area", topic);
      params.put("name", name);
      params.put("description", description);
      if(password!=null || password.length()>1) {
        params.put("password", password);
      }
      params.setUseJsonStreamer(true);

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
        }
      });

    } else {
      Message.fail(GroupNew.this, getString(R.string.groups_new_group_error_create));
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_new);
    ButterKnife.bind(this);
    setupToolbar();

    //TODO: remove this in prd
    this.name.getEditText().setText("My Cool Group 1");
    this.description.getEditText().setText("A very long long long description to pass this validation");

    setupFormValidation();
    this.createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onCreateGroup();
      }
    });
  }

  public void setupFormValidation(){
    this.nameValidator = new ValidateGroupName(this.name,getString(R.string.groups_new_group_error_name));
    this.descriptionValidator = new ValidateGroupDescription(this.description,getString(R.string.groups_new_group_error_description));
    this.name.getEditText().addTextChangedListener(nameValidator);
    this.description.getEditText().addTextChangedListener(descriptionValidator);
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
}
