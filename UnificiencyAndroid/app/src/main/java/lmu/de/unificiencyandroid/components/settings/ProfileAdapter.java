package lmu.de.unificiencyandroid.components.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.Group;


public class ProfileAdapter extends ArrayAdapter {

    public ProfileAdapter(Context context, int textViewResourceId,
                          List<Group> groups) {
        super(context, textViewResourceId, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Group group = (Group) getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.account_group_item, null);

        TextView groupName=(TextView) view.findViewById(R.id.group_name_account);
        groupName.setText(group.getName());


        return view;
    }
}