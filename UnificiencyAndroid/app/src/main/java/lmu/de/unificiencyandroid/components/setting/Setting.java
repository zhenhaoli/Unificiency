package lmu.de.unificiencyandroid.components.setting;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lmu.de.unificiencyandroid.R;

/**
 * Created by ostdong on 10/01/2017.
 */

public class Setting extends Fragment {

    Button groupManagement, notesManagement;
    Button save,exit;

    TextInputLayout nickName_editor, majorName_editor;

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


        groupManagement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingGroups=new Intent(getActivity(),SettingGroups.class);
                startActivity(settingGroups);

            }
        });
        notesManagement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent settingNotes=new Intent(getActivity(),SettingNotes.class);
                startActivity(settingNotes);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });


        return view;

    }
}
