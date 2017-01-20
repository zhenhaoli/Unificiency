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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;

public class BuildingDetails extends AppCompatActivity {

  private static final String TAG = BuildingDetails.class.getName();

  ArrayList<Room> rooms = new ArrayList<Room>();

  BuildingDetailsAdapter buildingDetailsAdapter;

  @BindView(R.id.buildings_details_backButton)
  ImageView backBtn;

  @BindView(R.id.building_details_name)
  TextView name;

  @BindView(R.id.building_rooms)
  ListView roomsListView;

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

    final RequestParams params = new RequestParams();
    params.put("address", address);

    UnificiencyClient client = new NodeAPIClient();

    client.get("rooms", params, new JsonHttpResponseHandler() {

      public void onFailure(int statusCode, byte[] errorResponse, Throwable e){
        Log.e(TAG, statusCode + "" );
        Log.e(TAG, e.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray rooms) {
        try {
          Log.d(TAG, rooms.length()+ " rooms got");

          for(int i=0; i<rooms.length(); i++){
            String name = rooms.getJSONObject(i).getString("name");
            String level = rooms.getJSONObject(i).getString("level");
            String address = rooms.getJSONObject(i).getString("address");

            Integer fromHour = rooms.getJSONObject(i).getInt("freeFromHour");
            Integer toHour = rooms.getJSONObject(i).getInt("freeToHour");
            Integer fromMinute = rooms.getJSONObject(i).getInt("freeFromMinutes");
            Integer toMinute = rooms.getJSONObject(i).getInt("freeToMinutes");

            LocalTime from = new LocalTime(fromHour, fromMinute);
            LocalTime to = new LocalTime(toHour, toMinute);
            BuildingDetails.this.rooms.add(new Room(name, level, address, from, to));
          }

          Collections.sort(BuildingDetails.this.rooms, new Comparator<Room>() {
            public int compare(Room r1, Room r2) {
              int comp = r1.getState().compareTo(r2.getState());
              return  comp == 0 ? r1.freeForMinutes().compareTo(r2.freeForMinutes()) : comp;
            }
          });

          ArrayList<Room> newRooms = new ArrayList<Room>();
          for(Room r: BuildingDetails.this.rooms){
            if(!(r.getState() == Room.State.TAKEN)){
              newRooms.add(r);
            }
          }

          buildingDetailsAdapter = new BuildingDetailsAdapter(BuildingDetails.this, newRooms);
          roomsListView.setAdapter(buildingDetailsAdapter);

        } catch (Exception e) {
          Log.e(TAG, e.toString());
          Message.success(BuildingDetails.this, e.toString());
        }

      }
    });

  }
}
