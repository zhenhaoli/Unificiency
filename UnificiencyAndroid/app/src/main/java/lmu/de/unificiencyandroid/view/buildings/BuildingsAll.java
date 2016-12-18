package lmu.de.unificiencyandroid.view.buildings;
import lmu.de.unificiencyandroid.adapters.BuildingsAdapter;
import lmu.de.unificiencyandroid.model.Building;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.model.Room;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by dev on 17.12.2016.
 */

public class BuildingsAll extends Fragment {


    List<Building> data = new ArrayList<Building>();
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //just to fake data, it useless
        Resources res = this.getResources();
        Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a17str)).getBitmap();
        Bitmap imga7astr = ((BitmapDrawable) res.getDrawable(R.drawable.a7astr)).getBitmap();
        Bitmap imgo67str = ((BitmapDrawable) res.getDrawable(R.drawable.o67str)).getBitmap();
        Bitmap imgg1str = ((BitmapDrawable) res.getDrawable(R.drawable.g1str)).getBitmap();

        InputStream is = getResources().openRawResource(R.raw.buildings);

        Reader in = new InputStreamReader(is);
        try {
            Iterable<CSVRecord> records = CSVFormat.newFormat(';').withQuote('"').withHeader().parse(in);

            for (CSVRecord record : records) {
                String address = record.get("address");
                Double lat = Double.parseDouble(record.get("lat"));
                Double lng = Double.parseDouble(record.get("lng"));
                String city = record.get("city");
                data.add(new Building(address, city, imga7astr, lat, lng, null));
            }
        } catch (Exception e){
            Log.e("failed to parse csv", e.toString());
        }

        //useful code to show list view
        final View x =  inflater.inflate(R.layout.buildings_all,null);
        final ListView all_building_listview = (ListView) x.findViewById(R.id.all_building_listview);

        BuildingsAdapter adapter= new BuildingsAdapter(getContext(), android.R.layout.simple_list_item_1,data);
        all_building_listview.setAdapter(adapter);

        all_building_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                Intent buildungDetails=new Intent(getActivity(),BuildungDetails.class);
                buildungDetails.putExtra("building", position);
                startActivity(buildungDetails);
            }
        });

        return x;
    }
}




