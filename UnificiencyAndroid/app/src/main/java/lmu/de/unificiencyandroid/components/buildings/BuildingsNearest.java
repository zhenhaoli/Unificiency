package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.untils.SharedPref;

public class BuildingsNearest extends BuildingsFragment {


  View x;
  ListView nearestBuildingListview;

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    super.onCreateView(inflater, container, savedInstanceState);

    String authToken =  SharedPref.getDefaults("authToken", getContext());

    Log.d("bA Token in sharedPref", authToken);

    UnificiencyClient client = new UnificiencyClient();
    client.addHeader("Authorization", "Bearer " + authToken);
    client.get("buildings/nearest", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // If the response is JSONObject instead of expected JSONArray
      }
      public void onFailure(int statusCode, byte[] errorResponse, Throwable e){
        Log.e("status", statusCode + "" );
        Log.e("e", e.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray buildings) {
        // Pull out the first event on the public timeline
        try {
          Log.d("buildings", buildings.length()+"");

          List<Building> buildingsFromServer = new ArrayList<>();
          for(int i=0; i<buildings.length(); i++){
            String address = buildings.getJSONObject(i).getString("address");
            String city = buildings.getJSONObject(i).getString("city");
            Double lat = buildings.getJSONObject(i).getDouble("lat");
            Double lng = buildings.getJSONObject(i).getDouble("lng");
            String distanceText = buildings.getJSONObject(i).getString("distanceText");
            String durationText = buildings.getJSONObject(i).getString("durationText");
            Integer distance = buildings.getJSONObject(i).getInt("distance");
            Integer duration = buildings.getJSONObject(i).getInt("duration");
            buildingsFromServer.add(new Building(address, city, lat, lng, distanceText, durationText, distance, duration, null, null));
          }

          nearestBuildingListview = (ListView) x.findViewById(R.id.nearest_building_listview);

          BuildingsAdapter adapter= new BuildingsAdapter(getContext(), android.R.layout.simple_list_item_1, buildingsFromServer);
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


        } catch (Exception e) {
          Log.e("BuildingAll", e.toString());
        }

      }
    });


    x =  inflater.inflate(R.layout.buildings_nearest,null);

    return x;
  }

}