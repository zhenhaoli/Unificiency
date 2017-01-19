package lmu.de.unificiencyandroid.components.buildings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class BuildingDetails extends AppCompatActivity {


  List<Room> rooms_taken = new ArrayList<Room>();
  List<Room> rooms_availabe= new ArrayList<Room>();

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
            if(!available) {
              rooms_taken.add(new Room(name, level, address, available));
            } else {
              rooms_availabe.add(new Room(name, level, address, available));
            }
          }

          buildingDetailsAdapter = new BuildingDetailsAdapter(BuildingDetails.this);

          for (Room room : rooms_availabe) {
            if(room.getAvailable()) buildingDetailsAdapter.addItem(room.toString());
          }

          for (Room room : rooms_taken) {
            if(!room.getAvailable()) buildingDetailsAdapter.addItem(room.toString());
          }

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
