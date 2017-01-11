package lmu.de.unificiencyandroid.components.groups.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import lmu.de.unificiencyandroid.R;

public class GroupMemberAdapter extends ArrayAdapter<String> {
    public GroupMemberAdapter(Context context, ArrayList<String> member) {
        super(context, 0, member);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String member = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_details_member,parent, false);
        }
        TextView memberViewItem = (TextView) convertView.findViewById(R.id.group_details_member_item);
        ImageView leftImg = (ImageView) convertView.findViewById(R.id.group_name_first_chars);
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable circle = TextDrawable.builder()
                .buildRound(member.substring(0,1), generator.getColor(member));
        leftImg.setImageDrawable(circle);
        memberViewItem.setText(member);
        return convertView;

    }
}