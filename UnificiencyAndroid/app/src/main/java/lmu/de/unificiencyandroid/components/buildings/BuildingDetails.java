package lmu.de.unificiencyandroid.components.buildings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.LocalTime;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.Random;

import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class BuildingDetails extends AppCompatActivity {


  ArrayList<Room> rooms = new ArrayList<Room>();

  BuildingDetailsAdapter buildingDetailsAdapter;

  @BindView(R.id.buildings_details_backButton)
  ImageView backBtn;

  @BindView(R.id.building_details_name)
  TextView name;

  @BindView(R.id.section_listview)
  ListView section_listview;

  @OnClick(R.id.buildings_details_backButton)
  void goBack() {
    onBackPressed();
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.building_details);
    ButterKnife.bind(this);

    String address = getIntent().getStringExtra("address");

    name.setText(address);

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    final RequestParams params = new RequestParams();
    params.put("address", address);

    UnificiencyClient client = new NodeAPIClient();
    client.addHeader("Authorization", "Bearer " + authToken);
    client.get("rooms", params, new JsonHttpResponseHandler() {

      public void onFailure(int statusCode, byte[] errorResponse, Throwable e){
        Log.e("status", statusCode + "" );
        Log.e("e", e.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray rooms) {
        // Pull out the first event on the public timeline
        try {
          Log.d("rooms", rooms.length()+"");

          for(int i=0; i<rooms.length(); i++){
            String name = rooms.getJSONObject(i).getString("name");
            String level = rooms.getJSONObject(i).getString("level");
            String address = rooms.getJSONObject(i).getString("address");
            Boolean available = rooms.getJSONObject(i).getBoolean("available");
            LocalTime from_ = new LocalTime(10,15);
            LocalTime to_ = new LocalTime(12,15);
            LocalTime from = new LocalTime(15,30);
            LocalTime to = new LocalTime(19,30);
            LocalTime from__ = new LocalTime(18,10);
            LocalTime to__ = new LocalTime(20,30);
            if(i%2 == 0){
              BuildingDetails.this.rooms.add(new Room(name, level, address,from_,to_));
            }
            else if(i%5 == 0) {
              BuildingDetails.this.rooms.add(new Room(name, level, address,from,to));
            }
            else {
              BuildingDetails.this.rooms.add(new Room(name, level, address,from__,to__));
            }

          }
          Collections.sort(BuildingDetails.this.rooms, new Comparator<Room>() {
            public int compare(Room r1, Room r2) {
              return r1.getState().compareTo(r2.getState());
            }
          });

          buildingDetailsAdapter = new BuildingDetailsAdapter(BuildingDetails.this, BuildingDetails.this.rooms);
          section_listview.setAdapter(buildingDetailsAdapter);

        } catch (Exception e) {
          Log.e("BuildingDetails", e.toString());
        }

      }
    });

  }


  /*public void setListViewHeightBasedOnChildren(ListView listView) {
    if (buildingDetailsAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight =0;
    for (int i = 0; i < buildingDetailsAdapter.getCount(); i++) {
      View listItem = buildingDetailsAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (buildingDetailsAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }*/
}
