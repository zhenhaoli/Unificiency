package lmu.de.unificiencyandroid.components.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.ImageUtils;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;
import uk.co.senab.photoview.PhotoViewAttacher;


public class NoteDetails extends AppCompatActivity {

  @BindView(R.id.note_detail_course)
  TextView noteTopic;

  @BindView(R.id.note_detail_title)
  TextView noteTitle;

  @BindView(R.id.note_detail_creator)
  TextView noteCreator;

  @BindView(R.id.note_detail_content)
  TextView noteContent;

  @BindView(R.id.note_detail_rating)
  TextView noteRating;

  @BindView(R.id.my_toolbar_noteDetials)
  Toolbar toolbar;

  @BindView(R.id.edit)
  Button editNote;

  @BindView(R.id.delete)
  Button deleteNote;

  @BindView(R.id.fav)
  ToggleButton favNote;

  @BindView(R.id.imageView)
  ImageView imageView;

  @OnClick(R.id.imageView)
  public void fullscreenImage(){
    Intent intent = new Intent(NoteDetails.this, NoteImage.class);
    intent.putExtra("imageUrl", imageUrl);
    startActivity(intent);
  }

  PhotoViewAttacher mAttacher;

  String imageUrl;
  Note note;


  @OnClick(R.id.edit)
  public void editNote(){

    Intent intent = new Intent(this, NoteEdit.class);
    intent.putExtra("noteId", note.getNoteId());
    intent.putExtra("groupId", getIntent().getExtras().getInt("groupId"));

    startActivityForResult(intent, 1);
  }

  @OnClick(R.id.fav)
  public void facNote(){
    Integer noteId = getIntent().getIntExtra("noteId", -1);
    UnificiencyClient client = new PythonAPIClient();
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());
    client.addHeader("Authorization", authToken);

    if(favNote.isChecked()){

      client.post("notes/" + noteId + "/favor/", null, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          favNote.setBackgroundColor(getResources().getColor(R.color.yellow_600));
          Message.success(NoteDetails.this, "Note " + note.getTitle() + " zum favorites hinzugefügt");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          String errMsg = null;
          try {
            errMsg = errorResponse.getString("message");
          } catch (Exception e) {
            Logger.e(e, "Exception");
          }
          Message.fail(NoteDetails.this, errMsg);
        }
      });

    } else {
      //TODO: wait for backend to del fav
      client.delete("notes/" + noteId + "/favor/", null, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          favNote.setBackgroundColor(getResources().getColor(R.color.yellow_600));
          Message.success(NoteDetails.this, "Note " + note.getTitle() + " von favorites gelöscht");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          String errMsg = null;
          try {
            errMsg = errorResponse.getString("message");
          } catch (Exception e) {
            Logger.e(e, "Exception");
          }
          Message.fail(NoteDetails.this, errMsg);
        }
      });

      favNote.setBackgroundColor(getResources().getColor(R.color.dark_purple_200));
    }
  }

  @OnClick(R.id.delete)
  public void deleteNote(){
    Integer noteId = getIntent().getIntExtra("noteId", -1);

    UnificiencyClient client = new PythonAPIClient();
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    client.addHeader("Authorization", authToken);
    client.delete("notes/" + noteId + "/", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Intent intent = new Intent();
        intent.putExtra("success", "Note erfolgreich gelöscht!");
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
        Message.fail(NoteDetails.this, errMsg);
      }
    });
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.note_details);
    ButterKnife.bind(this);

    setupToolbar();

    getNoteById(getIntent().getIntExtra("noteId", -1));
  }

  public void setupToolbar(){
    toolbar.setTitle(R.string.note_detail);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeButtonEnabled(true);
    ab.setDisplayHomeAsUpEnabled(true);
  }

  public void getNoteById(Integer noteId) {
    final String authToken = SharedPref.getDefaults("authToken", getApplicationContext());
    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("notes/" + noteId, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject noteJSON) {
        Logger.json(noteJSON.toString());
        try {
          Integer id = noteJSON.getInt("id");
          String topic = noteJSON.getString("topic");
          String name = noteJSON.getString("name");
          String content = noteJSON.getString("content");
          String createdBy = noteJSON.getJSONObject("creator").getString("username");
          Boolean hasImage = noteJSON.getBoolean("has_image");

          note = new Note(id, topic,name, content, createdBy, null);

          noteTopic.setText("Vorlesung: " + topic);
          noteTitle.setText("Titel: " + name);
          noteCreator.setText("Ersteller: " + createdBy);
          noteRating.setText("Rank: " + 0);
          noteContent.setText("Content: \n" + content);

          if(hasImage){
            imageUrl = "notes/" + id + "?image=true";
            ImageUtils.downloadToImageView(NoteDetails.this, imageUrl, imageView);

            Logger.d("download note image " + imageUrl);
          }

        } catch (Exception e){
          Logger.e(e, "Exception");
          Message.fail(NoteDetails.this, e.toString());
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
        Message.fail(NoteDetails.this, errmsg);
      }
    });

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String message;

        //TODO: get id from note, need backend for this
        if (extras != null) {
          getNoteById(extras.getInt("noteId"));
          message = extras.getString("saveSuccess");
          Message.success(this, message);
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Logger.d("user canceled editing note");
      }
    }
  }//onActivityResult


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
