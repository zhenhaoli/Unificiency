package lmu.de.unificiencyandroid.view.buildings;
import lmu.de.unificiencyandroid.model.Building;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import lmu.de.unificiencyandroid.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by dev on 17.12.2016.
 */

public class BuildingsAll extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        List<String> data = new ArrayList<String>();
        //just to fake data, it useless
        Resources res = this.getResources();
        Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a17str)).getBitmap();
        Bitmap imga7astr = ((BitmapDrawable) res.getDrawable(R.drawable.a7astr)).getBitmap();
        Bitmap imgo67str = ((BitmapDrawable) res.getDrawable(R.drawable.o67str)).getBitmap();
        Bitmap imgg1str = ((BitmapDrawable) res.getDrawable(R.drawable.g1str)).getBitmap();

        for (int i=0; i<20;i++){
        data.add(new Building("Amalienstr. 17","München",imga17str).toString());
        data.add(new Building("Amalienstr. 73a","München",imga7astr).toString());
        data.add(new Building("Öttingenstr. 67","München",imgo67str).toString());
        data.add(new Building("Geschwister-Scholl-Platz 1","München",imgg1str).toString());
        }


        //useful code to show list view
        View x =  inflater.inflate(R.layout.buildings_all,null);
        ListView all_building_listview = (ListView) x.findViewById(R.id.all_building_listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,data);
        all_building_listview.setAdapter(adapter);

        all_building_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        return x;
    }
}
/*
public class MyAdapter extends ArrayAdapter {

    public MyAdapter(Context context, int resource, T[] objects){

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

}*/