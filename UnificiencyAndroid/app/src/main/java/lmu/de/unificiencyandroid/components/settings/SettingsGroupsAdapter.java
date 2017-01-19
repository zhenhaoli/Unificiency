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


public class SettingsGroupsAdapter extends ArrayAdapter {

    public SettingsGroupsAdapter(Context context, int textViewResourceId,
                                 List<Group> groups) {
        super(context, textViewResourceId, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Group group = (Group) getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.setting_group_list_item, null);

        TextView group_name=(TextView) view.findViewById(R.id.settinggroup_name);
        group_name.setText(group.getName());

        TextView group_description=(TextView) view.findViewById(R.id.settinggroup_description);
        group_description.setText(group.getDescription());


        return view;
    }
}
