package lmu.de.unificiencyandroid.components.buildings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import lmu.de.unificiencyandroid.R;

public class BuildingsAllAdapter extends BaseAdapter {

    List<Building> buildings;
    private Context mContext;

    public BuildingsAllAdapter(Context context,
                               List<Building> buildings) {
        super();
        this.buildings = buildings;
        this.mContext = context;
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
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.building_listview_item, null);
        } else {
            grid = convertView;
        }
            ImageView imageView = (ImageView)grid.findViewById(R.id.imgitem);
            //textView.setText(buildings.get(position).toString());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            String address = buildings.get(position).address;
            TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(40)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(address, generator.getColor(address));

            imageView.setImageDrawable(drawable);
            //imageView.setImageResource(R.drawable.o67str);


        return grid;
    }
}
