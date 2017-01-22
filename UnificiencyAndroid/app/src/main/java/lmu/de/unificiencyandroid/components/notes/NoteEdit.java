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
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class NoteEdit extends AppCompatActivity {

  @BindView(R.id.topic)
  TextInputLayout topicTextInput;

  @BindView(R.id.name)
  TextInputLayout nameTextInput;

  @BindView(R.id.content)
  TextInputLayout contentTextInput;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.save)
  Button save;

  Note note;

  @OnClick(R.id.save)
  public void updateNote() {

    String topic = topicTextInput.getEditText().getText().toString();
    String name = nameTextInput.getEditText().getText().toString();
    String content = contentTextInput.getEditText().getText().toString();

    Integer noteId = note.getNoteId();
    Integer groupId = getIntent().getExtras().getInt("groupId");

    String authToken = SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();
    params.put("topic", topic);
    params.put("name", name);
    params.put("content", content);
    params.put("groupId", groupId);
    params.setUseJsonStreamer(true);

    UnificiencyClient client = new NodeAPIClient();

    client.addHeader("Authorization", authToken);
    client.put("notes/" + noteId + "/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {

          Intent intent = new Intent();
          intent.putExtra("saveSuccess", "Speicherung erfolgreich");
          intent.putExtra("noteId", note.getNoteId());
          setResult(Activity.RESULT_OK, intent);
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
        Message.fail(NoteEdit.this, errmsg);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        Message.fail(NoteEdit.this, responseString);
        Logger.e(responseString);
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.note_edit);
    ButterKnife.bind(this);
    setupToolbar();

    getNoteById(getIntent().getIntExtra("noteId", -1));

  }

  public void getNoteById(Integer noteId) {
    String authToken = SharedPref.getDefaults("authToken", getApplicationContext());
    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("notes/" + noteId, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject noteJSON) {

        try {
          Integer id = noteJSON.getInt("id");
          String topic = noteJSON.getString("topic");
          String name = noteJSON.getString("name");
          String content = noteJSON.getString("content");
          String createdBy = noteJSON.getJSONObject("creator").getString("username");

          note = new Note(id, topic,name, content, createdBy, null);

          topicTextInput.getEditText().setText(topic);
          nameTextInput.getEditText().setText(name);
          contentTextInput.getEditText().setText(content);

        } catch (Exception e){
          Logger.e(e, "Exception");
          Message.fail(NoteEdit.this, e.toString());
        }
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
        Message.fail(NoteEdit.this, errmsg);
      }
    });

  }

  public void setupToolbar() {
    toolbar.setTitle(R.string.toolbar_edit_note);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home: {
        onBackPressed();
        return true;
      }
      default: {
        return super.onOptionsItemSelected(item);
      }
    }
  }

}
