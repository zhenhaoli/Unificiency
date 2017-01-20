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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;

public class BuildingsAll extends BuildingsBase {

  static final String TAG = BuildingsAll.class.getName();

  @BindView(R.id.avi)
  com.wang.avi.AVLoadingIndicatorView avi;

  @BindView(R.id.all_building_listview)
  GridView allBuildings;

  @Nullable
  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.buildings_all,null);

    ButterKnife.bind(this, view);

    loadData();
    return view;
  }

  public void loadData() {
    avi.show();

    UnificiencyClient client = new NodeAPIClient();

    client.get("buildings", null, new JsonHttpResponseHandler() {

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Log.e(TAG, errorResponse.toString());
        Message.fail(getContext(), errorResponse.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray buildings) {
        try {
          Log.d(TAG, buildings.length()+" buildings got");

          List<Building> buildingsFromServer = new ArrayList<>();
          for(int i=0; i<buildings.length(); i++){

            String address = buildings.getJSONObject(i).getString("address");
            String city = buildings.getJSONObject(i).getString("city");
            Double lat = buildings.getJSONObject(i).getDouble("lat");
            Double lng = buildings.getJSONObject(i).getDouble("lng");

            buildingsFromServer.add(new Building(address, city, lat, lng));
          }

          BuildingsAllAdapter adapter= new BuildingsAllAdapter(getContext(), buildingsFromServer);

          allBuildings.setAdapter(adapter);

          allBuildings.setOnItemClickListener(new AdapterView.OnItemClickListener()
          {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {

              Building building=(Building) allBuildings.getItemAtPosition(position);

              Intent buildungDetails=new Intent(getActivity(),BuildingDetails.class);
              buildungDetails.putExtra("address", building.getAddress());
              buildungDetails.putExtra("city", building.getCity());

              startActivity(buildungDetails);

            }
          });

        } catch (Exception e) {
          Log.e(TAG, e.toString());
          Message.fail(getContext(), e.toString());
        } finally {
          avi.hide();
        }
      }
    });
  }

}