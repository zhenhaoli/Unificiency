package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import lmu.de.unificiencyandroid.R;

public class BuildingsAll extends BuildingsFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        //useful code to show list components
        final View x =  inflater.inflate(R.layout.buildings_all,null);
        final ListView all_building_listview = (ListView) x.findViewById(R.id.all_building_listview);

        BuildingsAdapter adapter= new BuildingsAdapter(getContext(), android.R.layout.simple_list_item_1, buildings);
        all_building_listview.setAdapter(adapter);

        all_building_listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

                Building building=(Building) all_building_listview.getItemAtPosition(position);
                Intent buildungDetails=new Intent(getActivity(),BuildingDetails.class);
                buildungDetails.putExtra("address", building.getAddress());
                buildungDetails.putExtra("city", building.getCity());
                startActivity(buildungDetails);
            }
        });

        return x;
    }
}




