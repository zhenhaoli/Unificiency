package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class Profile extends Fragment {


  TextView name_Account, email_Account, major_Account;
  TextView stars_nummber, notes_nummber;
  RoundImageView roundImageView;
  FloatingActionButton edit_btn;

  //   ListView listView;

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

          name_Account.setText(nickName);
          major_Account.setText(majorName);
          email_Account.setText(email);


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

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view =  inflater.inflate(R.layout.account,null);

    name_Account= (TextView) view.findViewById(R.id.name_account);
    email_Account= (TextView) view.findViewById(R.id.email_account);
    major_Account= (TextView) view.findViewById(R.id.major_account);

    stars_nummber=(TextView) view.findViewById(R.id.stars_nummber);
    notes_nummber=(TextView) view.findViewById(R.id.notes_nummber);

    edit_btn=(FloatingActionButton) view.findViewById(R.id.edit_floating_button);

    edit_btn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent intent= new Intent(getContext(), ProfileEdit.class);
        startActivityForResult(intent, 1);
      }
    });

    //      listView=(ListView) view.findViewById(R.id.group_list_account);
    //       name_Account.setText("ostdong");
    //       email_Account.setText("ostdong@gmail.com");
    //      major_Account.setText("informatik");

    stars_nummber.setText("18");
    notes_nummber.setText("6");

    getUserInfo();

    //fake data
  /*      ArrayList<String> GroupMember =new ArrayList<String>(){{
            add("Zhenhao");
            add("Robert");
            add("Jindong");
        }};


        List<Group> groups = Arrays.asList(
            new Group(1,"Group_Name_1", "MSP", "description", GroupMember, false),
            new Group(2, "Group_Name_2", "GPS", "description", GroupMember,false),
            new Group(3, "Group_Name_3", "REST","description",  GroupMember, false),
            new Group(4, "Group_Name_4", "MSP","description",  GroupMember, false),
            new Group(5, "Group_Name_5", "GPS", "description", GroupMember,false),
            new Group(6, "Group_Name_6", "REST","description",  GroupMember, false)
        );

        ProfileAdapter adapter= new ProfileAdapter(getContext(), android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(adapter);

*/
    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String registeredMsg;

        getUserInfo();

        if (extras != null) {
          registeredMsg = extras.getString("saveSuccess");
          SuperActivityToast.cancelAllSuperToasts();
          SuperActivityToast.create(getContext(), new Style(), Style.TYPE_STANDARD)
              .setText(registeredMsg)
              .setDuration(Style.DURATION_LONG)
              .setFrame(Style.FRAME_KITKAT)
              .setColor(ResourcesCompat.getColor(getResources(), R.color.green_400, null))
              .setAnimations(Style.ANIMATIONS_SCALE)
              .show();


        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        //Write your code if there's no result
      }
    }
  }//onActivityResult
}
