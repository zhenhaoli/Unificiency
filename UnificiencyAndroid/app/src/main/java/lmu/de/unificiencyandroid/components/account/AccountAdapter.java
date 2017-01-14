package lmu.de.unificiencyandroid.components.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.buildings.Building;
import lmu.de.unificiencyandroid.components.groups.models.Group;

/**
 * Created by ostdong on 11/01/2017.
 */

public class AccountAdapter extends ArrayAdapter {

    public AccountAdapter(Context context, int textViewResourceId,
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
