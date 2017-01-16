package lmu.de.unificiencyandroid.components.buildings;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.untils.SharedPref;

public class BuildingDetails extends AppCompatActivity {

  List<Room> rooms_taken = new ArrayList<Room>();
  List<Room> rooms_availabe= new ArrayList<Room>();

  ImageView imgView;

  TextView textView;

  ListView section_listview;

  BuildingDetailsAdapter buildingDetailsAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buildung_details);

    imgView= (ImageView) findViewById(R.id.img_details);
    textView= (TextView) findViewById(R.id.text_details);
    section_listview=(ListView) findViewById(R.id.section_listview);

    String authToken =  SharedPref.getDefaults("authToken", getApplicationContext());

    Intent intent=getIntent();

    String clickedBuilding = intent.getStringExtra("address");

    NodeAPIClient client = new NodeAPIClient();
    client.addHeader("Authorization", "Bearer " + authToken);
    client.get("rooms?address="+clickedBuilding, null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // If the response is JSONObject instead of expected JSONArray
      }
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
            if(! available) {
              rooms_taken.add(new Room(name, level, address, available));
            } else {
              rooms_availabe.add(new Room(name, level, address, available));
            }
          }

          buildingDetailsAdapter=new BuildingDetailsAdapter(BuildingDetails.this);

          buildingDetailsAdapter.addSectionHeaderItem("Jetzt Frei");
          for (Room room : rooms_availabe) {
            if(room.getAvailable()) buildingDetailsAdapter.addItem(room.toString());
          }

          buildingDetailsAdapter.addSectionHeaderItem("Bald Frei");
          for (Room room : rooms_taken) {
            if(!room.getAvailable()) buildingDetailsAdapter.addItem(room.toString());
          }

          buildingDetailsAdapter.addSectionHeaderItem("Alle");
          for (Room room : rooms_taken) {
            buildingDetailsAdapter.addItem(room.toString());
          }

          Intent intent=getIntent();
          Resources res = BuildingDetails.this.getResources();
          Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a7astr)).getBitmap();
          Building building= new Building();

          textView.setText(intent.getStringExtra("address")+",  "+intent.getStringExtra("city"));
          imgView.setImageBitmap(building.getImg());

          section_listview.setAdapter(buildingDetailsAdapter);
          setListViewHeightBasedOnChildren(section_listview);


        } catch (Exception e) {
          Log.e("BuildingDetails", e.toString());
        }

      }
    });

  }


  public void setListViewHeightBasedOnChildren(ListView listView) {
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
  }
}
