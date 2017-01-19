package lmu.de.unificiencyandroid.components.buildings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class BuildingsNearest extends BuildingsBase {

  View view;
  RecyclerView nearestBuildingListview;
  ArrayList<Building> buildings;
  RecyclerView.LayoutManager groupsLayoutManager;
  com.wang.avi.AVLoadingIndicatorView avi;
  BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      // Get extra data included in the Intent
      String message = intent.getStringExtra("Status");

      if(message!=null) {
        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(getContext(), new Style(), Style.TYPE_STANDARD)
            .setText(message)
            .setDuration(Style.DURATION_LONG)
            .setFrame(Style.FRAME_KITKAT)
            .setColor(ResourcesCompat.getColor(getResources(), R.color.green_400, null))
            .setAnimations(Style.ANIMATIONS_SCALE)
            .show();
      }

    }
  };;
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    view = inflater.inflate(R.layout.buildings_nearest, container, false);
    nearestBuildingListview = (RecyclerView) view.findViewById(R.id.buildings_nearest_buildings_list);

    this.avi = (com.wang.avi.AVLoadingIndicatorView)view.findViewById(R.id.avi);
    nearestBuildingListview.setNestedScrollingEnabled(false);
    groupsLayoutManager = new LinearLayoutManager(this.getActivity());
    nearestBuildingListview.setLayoutManager(groupsLayoutManager);
    askLocationPermission();

    LocalBroadcastManager.getInstance(getContext()).registerReceiver(
        mMessageReceiver, new IntentFilter("ServerUpdates"));

    return view;
  }


  public void fetchData(double lat, double lng) {
    avi.show();
    String authToken =  SharedPref.getDefaults("authToken", getContext());

    final RequestParams params = new RequestParams();
    params.put("lat", lat);
    params.put("lng", lng);


    UnificiencyClient client = new NodeAPIClient();


    client.addHeader("Authorization", "Bearer " + authToken);
    client.get("buildings/nearest", params, new JsonHttpResponseHandler() {

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        String err = "Fehler beim Server, bitte später nochmal versuchen";
        if(errorResponse!=null){
          err = errorResponse.toString();
        }

        SuperActivityToast.cancelAllSuperToasts();
        SuperActivityToast.create(getContext(), new Style(), Style.TYPE_STANDARD)
            .setText(err)
            .setDuration(Style.DURATION_LONG)
            .setFrame(Style.FRAME_KITKAT)
            .setColor(ResourcesCompat.getColor(getResources(), R.color.red_400, null))
            .setAnimations(Style.ANIMATIONS_SCALE)
            .show();
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray buildings) {
        // Pull out the first event on the public timeline
        try {
          Log.d("buildings", buildings.length()+"");

          final List<Building> buildingsFromServer = new ArrayList<>();
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

          BuildingsNearestAdapter adapter = new BuildingsNearestAdapter(getContext(), buildingsFromServer);
          nearestBuildingListview.setAdapter(adapter);
          nearestBuildingListview.addOnItemTouchListener(
              new RecyclerItemClickListener(getContext(), nearestBuildingListview ,new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                  Building building= buildingsFromServer.get(position);
                  Intent buildungDetails=new Intent(getActivity(),BuildingDetails.class);
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
          Log.e("BuildingAll", e.toString());
        } finally {
          avi.hide();
        }
      }
    });
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    super.onConnected(bundle);
    try {
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
          mGoogleApiClient);
      if(mLastLocation != null) {
        Log.d("loc obj", mLastLocation.toString());

        //LMU Info Bau
        double myLat = 48.1493226;
        double myLng = 11.5918366;
        if(mLastLocation!=null){
          myLat = mLastLocation.getLatitude();
          myLng = mLastLocation.getLongitude();
        }

        fetchData(myLat, myLng);

      }
    } catch (SecurityException e){
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
  }

}
