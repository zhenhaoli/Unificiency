package lmu.de.unificiencyandroid.components.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.InputValidation.InputValidation;
import lmu.de.unificiencyandroid.InputValidation.ValidateGroupDescription;
import lmu.de.unificiencyandroid.InputValidation.ValidateGroupName;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class NewGroup extends AppCompatActivity {

  private Button createButton;
  private TextInputLayout description;
  private TextInputLayout name;
  private TextInputLayout password;
  private InputValidation nameValidator;
  private InputValidation descriptionValidator;


  public void onCreateGroup() {
    String name = this.name.getEditText().getText().toString();
    String description = this.description.getEditText().getText().toString();
    String password = this.password.getEditText().getText().toString();

    if(this.nameValidator.validate(name) && this.descriptionValidator.validate(description)){

      Log.i("GROUP_CREATION", "NO VALIDATION ERRORS");

      final RequestParams params = new RequestParams();
      params.put("topic_area", name);
      params.put("name", name);
      params.put("description", description);
      if(password!=null || password.length()>1) {
        params.put("password", password);
      }
      params.setUseJsonStreamer(true);


      UnificiencyClient client = new PythonAPIClient();
      String authToken =  SharedPref.getDefaults("authTokenPython", getApplicationContext());
      client.addHeader("Authorization", authToken);
      client.post("groups/", params, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          // If the response is JSONObject instead of expected JSONArray
          Log.d("res new group", response.toString());

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
            Log.e("json err", e.toString());
          }
          SuperActivityToast.cancelAllSuperToasts();
          SuperActivityToast.create(NewGroup.this, new Style(), Style.TYPE_STANDARD)
              .setText(errMsg)
              .setDuration(Style.DURATION_MEDIUM)
              .setFrame(Style.FRAME_KITKAT)
              .setColor(ResourcesCompat.getColor(getResources(), R.color.red_500, null))
              .setAnimations(Style.ANIMATIONS_SCALE)
              .show();
        }
      });

    } else {
      SuperActivityToast.cancelAllSuperToasts();
      SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
          .setText(getString(R.string.groups_new_group_error_create))
          .setDuration(Style.DURATION_MEDIUM)
          .setFrame(Style.FRAME_KITKAT)
          .setColor(ResourcesCompat.getColor(getResources(), R.color.red_500, null))
          .setAnimations(Style.ANIMATIONS_SCALE)
          .show();
      Log.i("NO_GROUP_CREATION_NAME", String.valueOf(this.nameValidator.validate(this.name.getEditText().toString())));
      Log.i("NO_GROUP_CREATION_DESCR", String.valueOf(this.descriptionValidator.validate(this.description.getEditText().toString())));
    }
  }

  public void setupFormValidation(){
    this.nameValidator = new ValidateGroupName(this.name,getString(R.string.groups_new_group_error_name));
    this.descriptionValidator = new ValidateGroupDescription(this.description,getString(R.string.groups_new_group_error_description));
    this.name.getEditText().addTextChangedListener(nameValidator);
    this.description.getEditText().addTextChangedListener(descriptionValidator);
  }

  public void setupViewReferences() {
    this.createButton = (Button) findViewById(R.id.groups_new_group_create);
    this.description = (TextInputLayout) findViewById(R.id.groups_new_group_description);
    this.name = (TextInputLayout) findViewById(R.id.groups_new_group_name);
    this.password = (TextInputLayout) findViewById(R.id.groups_new_group_password);
  }

  public void setupToolbar(){
    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    toolbar.setTitle(R.string.groups_new_group_title);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_group);
    setupToolbar();
    setupViewReferences();

    this.name.getEditText().setText("Group xx ");
    this.description.getEditText().setText("A very long long long description to pass this validation");
    this.password.getEditText().setText("123456");

    setupFormValidation();
    this.createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onCreateGroup();
      }
    });
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
