package lmu.de.unificiencyandroid.components.settings;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mvc.imagepicker.ImagePicker;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.ImageUtils;
import lmu.de.unificiencyandroid.utils.LoadingUtils;
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
  
  @BindView(R.id.layout)
  View layout;

  @BindView(R.id.google_progress)
  GoogleProgressBar googleProgressBar;

  Bitmap profileBitmap;

  String[] filePathColumn;
  Uri selectedImage;

  final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=2;

  @OnClick(R.id.save_edit)
  public void save(){
    setUserInfo();
  }

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

  @OnClick(R.id.camera_album)
  public void camera_album(){
    if(CheckCameraPermission())ImagePicker.pickImage(this, "Select your image:");
  }


  public void getUserInfo() {

    googleProgressBar.setVisibility(View.VISIBLE);
    LoadingUtils.enableView(layout, false);
    
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

          getProfilePicture();

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

  public void getProfilePicture() {
    String authToken = SharedPref.getDefaults("authToken", this);

    final RequestParams params = new RequestParams();

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("users/images/", params, new FileAsyncHttpResponseHandler(this) {
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
        Logger.e(throwable.toString());
        Message.fail(ProfileEdit.this, throwable.toString());

        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, File file) {
        Logger.d(file.toString());
        profileImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);
      }
    });
  }

  public void setUserInfo() {

    googleProgressBar.setVisibility(View.VISIBLE);
    LoadingUtils.enableView(layout, false);

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
            LoadingUtils.enableView(layout, true);
            googleProgressBar.setVisibility(View.INVISIBLE);
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
        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);
        Message.fail(ProfileEdit.this, errmsg);
      }
    });

  }

  public void setUserProfileImage(){

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();

    File profilePic = ImageUtils.bitmapToFile(this, profileBitmap);
    File imageToUpload = Compressor.getDefault(this).compressToFile(profilePic);

    Logger.d("Profile Pic File: " + imageToUpload);
    try {
      params.put("file", imageToUpload);
    } catch(FileNotFoundException e) {
      Logger.e(e, "File not found: ");
    }

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.post("users/images/", params, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Logger.json(response.toString());
        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);
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
        LoadingUtils.enableView(layout, true);
        googleProgressBar.setVisibility(View.INVISIBLE);
        Message.fail(ProfileEdit.this, errmsg);
      }
    });
  }

  public void backToProfileAfterSuccess(){
    Intent intent = new Intent();
    intent.putExtra("saveSuccess", "Speichern erfolgreich");
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


}
