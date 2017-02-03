package lmu.de.unificiencyandroid.components.buildings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

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

public class BuildingsNearest extends BuildingsBase {

  ArrayList<Building> buildings;

  @BindView(R.id.buildings_nearest_buildings_list)
  RecyclerView nearestBuildings;

  @BindView(R.id.avi)
  com.wang.avi.AVLoadingIndicatorView avi;

  RecyclerView.LayoutManager groupsLayoutManager;

  BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String message = intent.getStringExtra("Status");
      if(message!=null) {
        Message.success(getContext(), message);
      }
    }
  };

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.buildings_nearest, container, false);
    ButterKnife.bind(this, view);

    nearestBuildings.setNestedScrollingEnabled(false);
    groupsLayoutManager = new LinearLayoutManager(this.getActivity());
    nearestBuildings.setLayoutManager(groupsLayoutManager);

    LocalBroadcastManager.getInstance(getContext()).registerReceiver(
        mMessageReceiver, new IntentFilter("ServerUpdates"));

    return view;
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    super.onConnected(bundle);
    getLocation();
  }

  @Override
  public void onLocationChanged(Location location) {
    fetchData(location.getLatitude(), location.getLongitude());
  }

  public void getLocation(){
    try {
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
          mGoogleApiClient);

      //LMU Info Bau
      double myLat = 48.1493226;
      double myLng = 11.5918366;
      if(mLastLocation!=null){

        myLat = mLastLocation.getLatitude();
        myLng = mLastLocation.getLongitude();
      }

      fetchData(myLat, myLng);

    } catch (SecurityException e){
      Logger.e(e, "SecurityException");
      Message.fail(getContext(), e.toString());
    }
  }


  public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
    Logger.d("REQ LOCATION " + requestCode);
    if (requestCode == REQUEST_LOCATION) {
      if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        getLocation();
      }
    } else {
      Message.fail(getContext(), "GPS Funktionen deaktiviert. Bitte unter Einstellung -> Apps Permission vergeben");
    }
  }


  public void fetchData(double lat, double lng) {
    avi.show();

    final RequestParams params = new RequestParams();
    params.put("lat", lat);
    params.put("lng", lng);

    UnificiencyClient client = new NodeAPIClient();

    client.get("buildings/nearest", params, new JsonHttpResponseHandler() {

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Logger.e(errorResponse.toString());

        String err = getString(R.string.server_error);
        if(errorResponse!=null && errorResponse.toString()!= null){
          err = errorResponse.toString();
        }
        Message.fail(getContext(), err);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        Logger.e(responseString);
        Message.fail(getContext(), responseString);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray buildings) {
        try {
          Logger.d(buildings.length()+ " buildings got");

          final List<Building> buildingsFromServer = new ArrayList<>();
          for(int i=0; i<buildings.length(); i++){
            String address = buildings.getJSONObject(i).getString("address");
            String city = buildings.getJSONObject(i).getString("city");
            Double lat = buildings.getJSONObject(i).getDouble("lat");
            Double lng = buildings.getJSONObject(i).getDouble("lng");
            Integer distance = buildings.getJSONObject(i).getInt("distance");
            buildingsFromServer.add(new Building(address, city, lat, lng, distance, null));
          }

          BuildingsNearestAdapter adapter = new BuildingsNearestAdapter(getContext(), buildingsFromServer);
          nearestBuildings.setAdapter(adapter);
          nearestBuildings.addOnItemTouchListener(
              new BuildingClickListener(getContext(), nearestBuildings ,new BuildingClickListener.OnItemClickListener() {

                @Override public void onItemClick(View view, int position) {
                  Building building = buildingsFromServer.get(position);

                  Intent buildungDetails = new Intent(getActivity(),BuildingDetails.class);
                  buildungDetails.putExtra("address", building.getAddress());
                  buildungDetails.putExtra("city", building.getCity());

                  startActivity(buildungDetails);
                }

                @Override public void onLongItemClick(View view, int position) {
                  // not needed right now (maybe later ...)
                }
              })
          );

        } catch (Exception e) {
          Logger.e(e, "Exception");
          Message.fail(getContext(), e.toString());
        } finally {
          avi.hide();
        }
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
  }

}
