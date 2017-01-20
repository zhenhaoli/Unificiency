package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;

public class BuildingsAll extends BuildingsBase {

  View view;
  GridView all_building_listview;
  com.wang.avi.AVLoadingIndicatorView avi;
  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    view =  inflater.inflate(R.layout.buildings_all,null);

    avi = (com.wang.avi.AVLoadingIndicatorView) view.findViewById(R.id.avi);

    loadData();
    return view;
  }

  public void loadData() {
    avi.show();

    UnificiencyClient client = new NodeAPIClient();

    client.get("buildings", null, new JsonHttpResponseHandler() {

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
            buildingsFromServer.add(new Building(address, city, lat, lng, null, null, null, null, null, null));
          }

          all_building_listview = (GridView) view.findViewById(R.id.all_building_listview);

          BuildingsAllAdapter adapter= new BuildingsAllAdapter(getContext(), buildingsFromServer);
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

        } catch (Exception e) {
          Log.e("BuildingAll", e.toString());
        } finally {
          avi.hide();
        }
      }
    });
  }

}