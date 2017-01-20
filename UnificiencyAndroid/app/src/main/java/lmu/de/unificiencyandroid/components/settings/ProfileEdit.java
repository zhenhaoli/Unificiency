package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class ProfileEdit extends AppCompatActivity {

  Button save;

  TextInputEditText nickName_editor, majorName_editor,text_email;

  public void getUserInfo() {
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("users/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {

          String nickName = response.getString("username");
          String majorName = response.getString("major");
          String email = response.getString("email");

          nickName_editor.setText(nickName);
          majorName_editor.setText(majorName);
          text_email.setText(email);




        } catch (Exception e) {
          Log.e("getUserInfo", e.toString());
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, String errmsg, Throwable throwable) {
        Log.e("getUserInfo", errmsg);

      }
    });
  }

  public void setUserInfo() {

    String nickName = this.nickName_editor.getText().toString();
    String majorName = this.majorName_editor.getText().toString();
    String email = this.text_email.getText().toString();

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();
    params.put("username",nickName);
    params.put("email",email);
    params.put("major",majorName);
    params.setUseJsonStreamer(true);

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.put("users/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // Pull out the first event on the public timeline
        try {

          Intent intent = new Intent();
          intent.putExtra("saveSuccess", "Speicherung erfolgreich");
          setResult(Activity.RESULT_OK,intent);
          finish();

        } catch (Exception e) {
          Log.e("modifyUserInfo", "success");
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("modifyUserInfo", errorResponse.toString());
        String errmsg = null;
        try {
          errmsg = errorResponse.getString("message");
        } catch (Exception e) {

        }

        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(getApplicationContext(), new Style(), Style.TYPE_STANDARD)
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
    setContentView(R.layout.activity_edit_profile);
    setupToolbar();

    save = (Button) findViewById(R.id.save_edit);

    nickName_editor= (TextInputEditText) findViewById(R.id.edit_nickname);
    majorName_editor= (TextInputEditText) findViewById(R.id.edit_major);
    text_email= (TextInputEditText) findViewById(R.id.edit_email);

    getUserInfo();


    save.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        setUserInfo();
      }
    });



  }

  public void setupToolbar(){
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
    toolbar.setTitle(R.string.toolbar_edit_profile);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
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
