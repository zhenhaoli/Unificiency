package lmu.de.unificiencyandroid.components.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.models.Group;

/**
 * Created by ostdong on 10/01/2017.
 */

public class Account extends Fragment {


    TextView name_Account, email_Account, major_Account;
    TextView stars_nummber, notes_nummber;
    ImageView image_Account;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.account,null);

        name_Account= (TextView) view.findViewById(R.id.name_account);
        email_Account= (TextView) view.findViewById(R.id.email_account);
        major_Account= (TextView) view.findViewById(R.id.major_account);

        stars_nummber=(TextView) view.findViewById(R.id.stars_nummber);
        notes_nummber=(TextView) view.findViewById(R.id.notes_nummber);

        image_Account=(ImageView) view.findViewById(R.id.img_account);
        listView=(ListView) view.findViewById(R.id.group_list_account);

        name_Account.setText("ostdong");
        email_Account.setText("ostdong@gmail.com");
        major_Account.setText("informatik");

        stars_nummber.setText("18");
        notes_nummber.setText("6");

        image_Account.setImageResource(R.drawable.logo);

        //fake data
        ArrayList<String> GroupMember =new ArrayList<String>(){{
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

        AccountAdapter adapter= new AccountAdapter(getContext(), android.R.layout.simple_list_item_1, groups);
        listView.setAdapter(adapter);


        return view;
    }
}
