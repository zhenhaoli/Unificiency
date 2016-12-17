package lmu.de.unificiencyandroid.view.buildings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.model.Building;
import lmu.de.unificiencyandroid.model.Room;


public class BuildungDetails extends AppCompatActivity {

    List<Room> rooms= new ArrayList<Room>();
    List<Room> rooms_availabe= new ArrayList<Room>();

    @BindView(R.id.img_details)
    ImageView imgView;

    @BindView(R.id.text_details)
    TextView textView;

    @BindView(R.id.allRooms_listview)
    ListView allRooms_listview;

    @BindView(R.id.now_availableRoom_details)
    ListView now_availableRoom_details;

    @BindView(R.id.later_availableRoom_details)
    ListView later_availableRoom_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildung_details);
        ButterKnife.bind(this);


        //
        for (int i=0; i<10;i++) {
            rooms.add(new Room("U139", true));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
            rooms.add(new Room("033", false));
        }
        for (Room room : rooms) {
            if(room.getAvailability()==true) rooms_availabe.add(room);
        }

        Intent intent = getIntent();
        Building building= (Building) Parcels.unwrap(getIntent().getParcelableExtra("building"));
        textView.setText(building.getAddress()+building.getCity());
        imgView.setImageBitmap(building.getImg());

        ArrayAdapter adapter1= new ArrayAdapter(this, android.R.layout.simple_list_item_1,rooms_availabe);
        now_availableRoom_details.setAdapter(adapter1);

        ArrayAdapter adapter2= new ArrayAdapter(this, android.R.layout.simple_list_item_1,rooms_availabe);
        later_availableRoom_details.setAdapter(adapter2);

        ArrayAdapter adapter3= new ArrayAdapter(this, android.R.layout.simple_list_item_1,rooms);
        allRooms_listview.setAdapter(adapter3);

    }
}
