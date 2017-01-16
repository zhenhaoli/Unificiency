package lmu.de.unificiencyandroid.components.buildings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import lmu.de.unificiencyandroid.R;

import static java.security.AccessController.getContext;

public class BuildingsAdapter extends BaseAdapter {

    List<Building> buildings;
    private Context mContext;

    public BuildingsAdapter(Context context,
                       List<Building> buildings) {
        super();
        this.buildings = buildings;
        this.mContext =context;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public Object getItem(int i) {
        return buildings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Building building = (Building) getItem(position);
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.building_listview_item, null);
            //TextView textView = (TextView) grid.findViewById(R.id.txitem);
            ImageView imageView = (ImageView)grid.findViewById(R.id.imgitem);
            //textView.setText(buildings.get(position).toString());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            String address = buildings.get(position).address;
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(42)
                    .bold()
                    .endConfig()
                    .buildRect(address, generator.getColor(address));

            imageView.setImageDrawable(drawable);
            //imageView.setImageResource(R.drawable.o67str);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
