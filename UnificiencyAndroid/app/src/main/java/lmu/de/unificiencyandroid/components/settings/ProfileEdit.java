package lmu.de.unificiencyandroid.components.settings;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

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
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.ImageUtils;
import lmu.de.unificiencyandroid.utils.LoadingUtils;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;
import lmu.de.unificiencyandroid.utils.Validate;

import static lmu.de.unificiencyandroid.R.id.passwordConfirm;

public class ProfileEdit extends AppCompatActivity {

  @BindView(R.id.profileEditDesc)
  TextView profileEditDesc;

  @BindView(R.id.profile_image)
  CircleImageView profileImage;

  @BindView(R.id.camera_album)
  ImageView camera_album;

  @BindView(R.id.edit_major)
  AutoCompleteTextView majorEditText;

  @BindView(R.id.passwordWrapper)
  TextInputLayout passwordWrapper;

  @BindView(R.id.passwordConfirmWrapper)
  TextInputLayout passwordConfirmWrapper;

  @OnTextChanged(R.id.password)
  public void validatePassword(){
    checkPassword();
  }

  @OnTextChanged(passwordConfirm)
  public void validatePasswordConfirm(){
    checkPasswordConfirm();
  }

  public boolean checkPassword() {
    return Validate.password(passwordWrapper, this);
  }

  public boolean checkPasswordConfirm() {
    return Validate.password(passwordConfirmWrapper, this);
  }

  @BindView(R.id.toolbar_edit_profile)
  Toolbar toolbar;

  @BindView(R.id.layout)
  View layout;

  @BindView(R.id.google_progress)
  GoogleProgressBar googleProgressBar;

  Bitmap profileBitmap;

  String[] majors ={"Medizin", "Informatik", "Medieninformatik", "Mensch-Maschine-Interaktion", "Physik", "Mathematik", "Statistik", "Jura", "Betriebswirtschaft"};

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
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    showLoad(true);

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

          profileEditDesc.setText("Hallo " + nickName + " " + getString(R.string.edit_hint) );
          majorEditText.setText(majorName);

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

        showLoad(false);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, File file) {
        Logger.d(file.toString());
        profileImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

        showLoad(false);
      }
    });
  }

  public void setUserInfo() {

    if(!checkPassword() ||!checkPasswordConfirm()){
      Message.fail(ProfileEdit.this, getString(R.string.invalid_input));
      return;
    }

    String majorName = majorEditText.getText().toString();
    String password = passwordWrapper.getEditText().getText().toString();
    String passwordConfirm = passwordConfirmWrapper.getEditText().getText().toString();

    Logger.d("password " + password);
    Logger.d("passwordConfirm  " + passwordConfirm);

    if(!password.equals(passwordConfirm)){
      passwordWrapper.setError(getString(R.string.validation_passwords_equal));
      return;
    }

    showLoad(true);

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();

    params.put("major",majorName);
    if(!TextUtils.isBlank(password) && !TextUtils.isEmpty(password)){
      params.put("password",password);
    }
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
            showLoad(false);
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
        showLoad(false);
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

    ArrayAdapter<String> adapter = new ArrayAdapter<String>
        (this,android.R.layout.select_dialog_item,majors);
    majorEditText.setThreshold(1);
    majorEditText.setAdapter(adapter);


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

  public void showLoad(boolean show){
    googleProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    LoadingUtils.enableView(layout, !show);
  }



}
