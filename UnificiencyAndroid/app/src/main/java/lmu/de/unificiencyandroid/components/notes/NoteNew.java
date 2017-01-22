package lmu.de.unificiencyandroid.components.notes;

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
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
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

  @OnClick(R.id.notes_new_note_create)
  public void uploadNote(){

    //TODO: read from form field!!
    Integer groupId = 0;

    String name = this.name.getEditText().getText().toString();
    String content = this.content.getEditText().getText().toString();
    String topic =  this.topic.getEditText().getText().toString();

    final RequestParams params = new RequestParams();
    params.put("topic", topic);
    params.put("name", name);
    params.put("content", content);
    params.setUseJsonStreamer(true);

    UnificiencyClient client = new PythonAPIClient();
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
