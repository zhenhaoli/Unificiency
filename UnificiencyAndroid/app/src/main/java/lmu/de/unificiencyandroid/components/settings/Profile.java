package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;

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

public class Profile extends Fragment {

  @BindView(R.id.name_account)
  TextView nameAccount;

  @BindView(R.id.email_account)
  TextView emailAccount;

  @BindView(R.id.major_account)
  TextView majorAccount;

  @BindView(R.id.stars_nummber)
  TextView starsNummber;

  @BindView(R.id.notes_nummber)
  TextView notesNummber;

  @BindView(R.id.profile_image)
  CircleImageView profileImage;

  @OnClick(R.id.edit_floating_button)
  public void startEditProfile() {
    Intent intent= new Intent(getContext(), ProfileEdit.class);
    startActivityForResult(intent, 1);
  }

  public void getUserInfo() {
    String authToken =  SharedPref.getDefaults("authToken", getContext());

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
          Integer groupsCount = response.getInt("groups_count");
          Integer notesCount = response.getInt("notes_count");
          Integer favCount = response.getInt("favorite_notes_count");

          nameAccount.setText(nickName);
          majorAccount.setText(majorName);
          emailAccount.setText(email);
          starsNummber.setText(groupsCount.toString());
          notesNummber.setText(notesCount.toString());
          getProfilePicture();

        } catch (Exception e) {
          Logger.e(e, "Exception");
          Message.fail(getContext(), e.toString());
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Message.fail(getContext(), errorResponse.toString());
        Logger.e(errorResponse.toString());
      }
    });
  }

  public void getProfilePicture() {
    String authToken = SharedPref.getDefaults("authToken", getContext());

    final RequestParams params = new RequestParams();

    UnificiencyClient client = new PythonAPIClient();

    client.addHeader("Authorization", authToken);
    client.get("users/images/", params, new FileAsyncHttpResponseHandler(getContext()) {
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
        Logger.e(throwable.toString());
        Message.fail(getContext(), throwable.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, File file) {
        Logger.d(file.toString());
        profileImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view =  inflater.inflate(R.layout.settings_profile,null);
    ButterKnife.bind(this, view);

    getUserInfo();

    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String message;

        getUserInfo();

        if (extras != null) {
          message = extras.getString("saveSuccess");
          Message.success(getContext(), message);
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Logger.d("user canceled editing profile");
      }
    }
  }//onActivityResult
}
