package lmu.de.unificiencyandroid.view.buildings;
import lmu.de.unificiencyandroid.adapters.BuildingsAdapter;
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

        List<Building> data = new ArrayList<Building>();
        //just to fake data, it useless
        Resources res = this.getResources();
        Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a17str)).getBitmap();
        Bitmap imga7astr = ((BitmapDrawable) res.getDrawable(R.drawable.a7astr)).getBitmap();
        Bitmap imgo67str = ((BitmapDrawable) res.getDrawable(R.drawable.o67str)).getBitmap();
        Bitmap imgg1str = ((BitmapDrawable) res.getDrawable(R.drawable.g1str)).getBitmap();

        for (int i=0; i<20;i++){
            data.add(new Building("Amalienstr. 17","München",imga17str));
            data.add(new Building("Amalienstr. 73a","München",imga7astr));
            data.add(new Building("Öttingenstr. 67","München",imgo67str));
            data.add(new Building("Geschwister-Scholl-Platz 1","München",imgg1str));
        }


        //useful code to show list view
        View x =  inflater.inflate(R.layout.buildings_all,null);
        ListView all_building_listview = (ListView) x.findViewById(R.id.all_building_listview);

        BuildingsAdapter adapter= new BuildingsAdapter(getContext(), android.R.layout.simple_list_item_1,data);
        all_building_listview.setAdapter(adapter);

        return x;
    }
}




