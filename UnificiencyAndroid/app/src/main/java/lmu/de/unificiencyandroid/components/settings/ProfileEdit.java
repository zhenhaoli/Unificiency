package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mvc.imagepicker.ImagePicker;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class ProfileEdit extends AppCompatActivity {

  @BindView(R.id.save_edit)
  Button save;

  @BindView(R.id.profile_image)
  CircleImageView profileImage;

  @BindView(R.id.camera_album)
  ImageView camera_album;

  @BindView(R.id.edit_nickname)
  TextInputEditText nicknameEditText;

  @BindView(R.id.edit_email)
  TextInputEditText emailEditText;

  @BindView(R.id.edit_major)
  TextInputEditText majorEditText;

  @BindView(R.id.toolbar_edit_profile)
  Toolbar toolbar;

  Bitmap profileBitmap;

  String[] filePathColumn;
  Uri selectedImage;

  final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=2;

  @OnClick(R.id.save_edit)
  public void save(){
    setUserInfo();
  }

  @OnClick(R.id.camera_album)
  public void camera_album(){
    ImagePicker.pickImage(this, "Select your image:");
  }

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

          nicknameEditText.setText(nickName);
          majorEditText.setText(majorName);
          emailEditText.setText(email);

        } catch (Exception e) {
          Logger.e(e, "Exception");
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, String errmsg, Throwable throwable) {
        Logger.e(errmsg);
      }
    });
  }

  public void setUserInfo() {

    String nickName = nicknameEditText.getText().toString();
    String majorName = majorEditText.getText().toString();
    String email = emailEditText.getText().toString();

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
        try {
          if(profileBitmap != null) {
            setUserProfileImage();
          } else {
            backToProfileAfterSuccess();
          }
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
        Message.fail(ProfileEdit.this, errmsg);
      }
    });

  }

  public void setUserProfileImage(){

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();

    File profilePic = bitmapToFile(profileBitmap);
    Logger.d("Profile Pic File: " + profilePic);
    try {
      params.put("file", profilePic);
    } catch(FileNotFoundException e) {
      Logger.e(e, "File not found: ");
    }

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.post("users/images/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Logger.json(response.toString());
        backToProfileAfterSuccess();
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
        Message.fail(ProfileEdit.this, errmsg);
      }
    });
  }

  public void backToProfileAfterSuccess(){
    Intent intent = new Intent();
    intent.putExtra("saveSuccess", "Speicherung erfolgreich");
    setResult(Activity.RESULT_OK,intent);
    finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.settings_profile_edit);
    ButterKnife.bind(this);

    setupToolbar();
    getUserInfo();

    ImagePicker.setMinQuality(600, 600);
  }


  public void setupToolbar(){
    toolbar.setTitle(R.string.toolbar_edit_profile);
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
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    profileBitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
    profileImage.setImageBitmap(profileBitmap);
  }

  private File bitmapToFile(Bitmap bitmap){
    try {
      File f = new File(this.getCacheDir(), "profile.png");
      f.createNewFile();

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
      byte[] bitmapdata = bos.toByteArray();

      FileOutputStream fos = new FileOutputStream(f);
      fos.write(bitmapdata);
      fos.flush();
      fos.close();
      return f;
    } catch (Exception e){
      Logger.e(e, "Exception during Bitmap to File: ");
      return null;
    }
  }

}
