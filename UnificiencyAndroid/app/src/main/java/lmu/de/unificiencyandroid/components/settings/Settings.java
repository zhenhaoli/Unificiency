package lmu.de.unificiencyandroid.components.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.GroupNew;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

import static android.widget.Toast.*;
import static android.widget.Toast.LENGTH_SHORT;

public class Settings extends Fragment {

    Button groupManagement, notesManagement;
    Button save,exit;

    TextInputLayout nickName_editor, majorName_editor,text_email;

    List<String> userInfo=null;

    public void getUserInfo() {
        String authToken =  SharedPref.getDefaults("authTokenPython", getContext());

        final RequestParams params = new RequestParams();

        UnificiencyClient client = new PythonAPIClient();

        client.addHeader("Authorization", authToken);
        client.get("users/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                try {

                        String nickName = response.getString("username");
                        String majorName = response.getString("major");
                        String email = response.getString("email");
                   //     String university_id = response.getString("university_id");
                   //     String password = response.getString("password");

                  //      userInfo.add(password);
                  //      userInfo.add(university_id);

                        nickName_editor.setHint(nickName);
                        majorName_editor.setHint(majorName);
                        text_email.setHint(email);


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

    public void setUserInfo() {

        String nickName = this.nickName_editor.getEditText().getText().toString();
        String majorName = this.majorName_editor.getEditText().getText().toString();
        String email = this.text_email.getEditText().getText().toString();

        String authToken =  SharedPref.getDefaults("authTokenPython", getContext());

        final RequestParams params = new RequestParams();
        params.put("username",nickName);
        params.put("email",email);
        params.put("major",majorName);
   //     params.put("password",userInfo.get(0));
   //     params.put("university_id",userInfo.get(1));

        UnificiencyClient client = new PythonAPIClient();

        client.addHeader("Authorization", authToken);
        client.post("users/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Pull out the first event on the public timeline
                try {

                } catch (Exception e) {
                    Log.e("modifyUserInfo", "success");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("modifyUserInfo", errorResponse.toString());
                String errmsg = null;
                try {
                    errmsg = errorResponse.getString("message");
                } catch (Exception e) {

                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.setting,null);

        groupManagement = (Button) view.findViewById(R.id.groups_management);
        notesManagement = (Button) view.findViewById(R.id.notes_management);
        save = (Button) view.findViewById(R.id.save_setting);
        exit = (Button) view.findViewById(R.id.exit_setting);

        nickName_editor= (TextInputLayout) view.findViewById(R.id.text_nickname);
        majorName_editor= (TextInputLayout) view.findViewById(R.id.text_major);
        text_email= (TextInputLayout) view.findViewById(R.id.text_email);

        List<String> userInfo=null;
        getUserInfo();




        groupManagement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingGroups=new Intent(getActivity(),SettingsGroups.class);
                startActivity(settingGroups);

            }
        });
        notesManagement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingNotes=new Intent(getActivity(),SettingsNotes.class);
                startActivity(settingNotes);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setUserInfo();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        return view;

    }
}
