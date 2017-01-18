package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class BuildingsNearest extends BuildingsFragment {


    View view;
    RecyclerView nearestBuildingListview;
    ArrayList<Building> buildings;
    RecyclerView.LayoutManager groupsLayoutManager;
    com.wang.avi.AVLoadingIndicatorView avi;

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
        fetchData();
        return view;
    }

    public void fetchData() {
      avi.show();
        String authToken =  SharedPref.getDefaults("authToken", getContext());

        Log.d("bA Token in sharedPref", authToken);

        UnificiencyClient client = new NodeAPIClient();

        client.addHeader("Authorization", "Bearer " + authToken);
        client.get("buildings/nearest", null, new JsonHttpResponseHandler() {
            public void onFailure(int statusCode, byte[] errorResponse, Throwable e){
                Log.e("status", statusCode + "" );
                Log.e("e", e.toString());
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

}
