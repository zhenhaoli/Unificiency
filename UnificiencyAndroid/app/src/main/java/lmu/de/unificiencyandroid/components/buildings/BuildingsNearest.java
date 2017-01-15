package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import lmu.de.unificiencyandroid.R;

public class BuildingsNearest extends BuildingsFragment {


    View x;
    ListView nearestBuildingListview;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        new BuildingsNearest.HttpRequestTask().execute();
        //useful code to show list components
        x =  inflater.inflate(R.layout.buildings_nearest,null);

        return x;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, ArrayList<Building>> {
        protected ArrayList<Building> doInBackground(Void... params) {
            try {
                final String url = "http://li.mz-host.de:5048/buildings/nearest";
                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<Building[]> responseEntity = restTemplate.getForEntity(url, Building[].class);

                Building[] buildings = responseEntity.getBody();

                return new ArrayList<>(Arrays.asList(buildings));
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        protected void onPostExecute( ArrayList<Building> buildings) {
            nearestBuildingListview = (ListView) x.findViewById(R.id.nearest_building_listview);

            BuildingsAdapter adapter= new BuildingsAdapter(getContext(), android.R.layout.simple_list_item_1, buildings);
            nearestBuildingListview.setAdapter(adapter);

            nearestBuildingListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                {

                    Building building=(Building) nearestBuildingListview.getItemAtPosition(position);
                    Intent buildungDetails=new Intent(getActivity(),BuildingDetails.class);
                    buildungDetails.putExtra("address", building.getAddress());
                    buildungDetails.putExtra("city", building.getCity());
                    startActivity(buildungDetails);
                }
            });

        }

    }

}