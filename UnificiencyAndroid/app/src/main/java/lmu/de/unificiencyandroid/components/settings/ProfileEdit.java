package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.custome.RoundImageView;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class ProfileEdit extends AppCompatActivity {

  static final int REQUEST_IMAGE_CAPTURE = 1;

  @BindView(R.id.save_edit)
  Button save;

  @BindView(R.id.Edit_roundImageView)
  RoundImageView edit_roundImageView;

  @BindView(R.id.edit_nickname)
  TextInputEditText nicknameEditText;

  @BindView(R.id.edit_email)
  TextInputEditText emailEditText;

  @BindView(R.id.edit_major)
  TextInputEditText majorEditText;

  @BindView(R.id.toolbar_edit_profile)
  Toolbar toolbar;

  String[] filePathColumn;
  Uri selectedImage;

  final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=2;

  @OnClick(R.id.save_edit)
  public void save(){
    setUserInfo();
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

          Intent intent = new Intent();
          intent.putExtra("saveSuccess", "Speichern erfolgreich");
          setResult(Activity.RESULT_OK,intent);
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
        Message.fail(ProfileEdit.this, errmsg);
      }
    });
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.settings_profile_edit);
    ButterKnife.bind(this);

    setupToolbar();
    getUserInfo();

    changePhoto();
    resetphoto();
  }

  public void resetphoto(){

    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    if (bundle != null && bundle.containsKey("data")){
      Bitmap imageBitmap = (Bitmap) bundle.get("data");
      edit_roundImageView.setImageBitmap(imageBitmap);
    }

    if (intent.getData() != null){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                  WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        selectedImage = intent.getData();
        String[] filePathColumn_temp = { MediaStore.Images.Media.DATA };
        filePathColumn=filePathColumn_temp;
        Cursor cursor = getContentResolver().query(selectedImage,
        filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        edit_roundImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission Granted
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        edit_roundImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
      } else {
        // Permission Denied
      }
    }
  }


  public void changePhoto(){

    edit_roundImageView.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(),ChoosePhoto.class);
        startActivity(intent);
      }
    });
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

}
