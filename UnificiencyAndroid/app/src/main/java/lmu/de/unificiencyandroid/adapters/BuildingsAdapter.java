package lmu.de.unificiencyandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.model.Building;

public class BuildingsAdapter extends ArrayAdapter {


    public BuildingsAdapter(Context context, int textViewResourceId,
                       List<Building> buildings) {
        super(context, textViewResourceId, buildings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Building building = (Building) getItem(position);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.building_listview_item, null);
        TextView address=(TextView) view.findViewById(R.id.txitem);
        address.setText(building.toString());

        ImageView imgitem=(ImageView) view.findViewById(R.id.imgitem);
        imgitem.setImageBitmap( building.getImg());

        return view;
    }
}
