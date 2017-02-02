package lmu.de.unificiencyandroid.components.notes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mvc.imagepicker.ImagePicker;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;
import id.zelory.compressor.Compressor;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.ImageUtils;
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

  @BindView(R.id.photo_new_note)
  Button addPhoto;

  @BindView(R.id.note_photo)
  LinearLayout note_photo_layout;

  @BindView(R.id.note_Image)
  ImageView note_Image;

  Bitmap noteImage;

  ArrayList<String> groupsNames= new ArrayList();
  Map<String , Integer> groupsNamesMap = new HashMap<>();
  Map<String , Integer> groupsNamesIdMap = new HashMap<>();

  private static final int REQUEST_PERMISSION_CAMERA_CODE = 1;
  @TargetApi(Build.VERSION_CODES.M)
  public boolean CheckCameraPermission() {
    int permissionCheckRead = ContextCompat.checkSelfPermission(getApplicationContext(),
        android.Manifest.permission.CAMERA);
    if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          android.Manifest.permission.CAMERA)) {
        ActivityCompat.requestPermissions(this,
            new String[]{android.Manifest.permission.CAMERA},
            REQUEST_PERMISSION_CAMERA_CODE);
      } else {
        ActivityCompat.requestPermissions(this,
            new String[]{android.Manifest.permission.CAMERA},
            REQUEST_PERMISSION_CAMERA_CODE);
      }
      return false;
    } else
      return true;
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode,
      String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_PERMISSION_CAMERA_CODE:
        ImagePicker.pickImage(this, "Select your image:");
    }
  }

  @OnClick(R.id.photo_new_note)
  public void addPhoto(){
    note_photo_layout.setVisibility(View.VISIBLE);
    if(CheckCameraPermission())ImagePicker.pickImage(this, "Select your image:");
  }

  @OnClick(R.id.notes_new_note_create)
  public void uploadNote(){
    Integer groupId = groupsNamesIdMap.get(spinner.getSelectedItem());

    String name = this.name.getEditText().getText().toString();
    String content = this.content.getEditText().getText().toString();
    String topic =  this.topic.getEditText().getText().toString();

    final RequestParams params = new RequestParams();
    params.put("topic", topic);
    params.put("name", name);
    params.put("content", content);
    params.put("groupId", groupId);

    if(noteImage != null){
      File noteImageFile = ImageUtils.bitmapToFile(this, noteImage);
      File imageToUpload = Compressor.getDefault(this).compressToFile(noteImageFile);
      Logger.d("Note Image File: " + imageToUpload);
      try {
        params.put("file", imageToUpload);
      } catch(FileNotFoundException e) {
        Logger.e(e, "File not found: ");
      }
    }

    UnificiencyClient client = new PythonAPIClient();
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    client.addHeader("Authorization", authToken);
    client.post("groups/" + groupId + "/notes/", params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Logger.json(response.toString());

        notifyGroupMembersAboutNewNote();

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

  public void notifyGroupMembersAboutNewNote(){

    Integer groupId = groupsNamesIdMap.get(spinner.getSelectedItem());

    String name = this.name.getEditText().getText().toString();

    final RequestParams params = new RequestParams();
    params.put("name", name);

    UnificiencyClient client = new NodeAPIClient();
    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    client.addHeader("Authorization", authToken);
    client.post("groups/" + groupId + "/notes/", params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Logger.json(response.toString());
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Logger.e(errorResponse.toString());
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
    ImagePicker.setMinQuality(600, 600);
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

            if(name.equals("public"))
              name = "Öffentlich";

            groupsNames.add(name);
            groupsNamesMap.put(name,i);
            groupsNamesIdMap.put(name, id);
            Logger.i(groupsNamesIdMap.toString());
          }
        }catch (Exception e) {
          Logger.e(e, "Exception");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(NoteNew.this, android.R.layout.simple_spinner_item, groupsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setDefaultGroupNameToSpinner();
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

  public void setDefaultGroupNameToSpinner() {
    String defaultGroupName = getIntent().getStringExtra("groupname");

    if(defaultGroupName == null) {
      spinner.setSelection(0);
      return;
    }

    if(defaultGroupName.equals("public")){
      defaultGroupName = "Öffentlich";
    }
    if(!TextUtils.isEmpty(defaultGroupName)){
      int index = groupsNamesMap.get(defaultGroupName);
      spinner.setSelection(index);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    noteImage = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
    note_Image.setImageBitmap(noteImage);
    // TODO do something with the bitmap
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
