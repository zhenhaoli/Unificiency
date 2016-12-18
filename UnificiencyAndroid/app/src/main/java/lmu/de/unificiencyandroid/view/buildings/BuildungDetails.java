package lmu.de.unificiencyandroid.view.buildings;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    ImageView imgView;

    TextView textView;

    ListView allRooms_listview;

    ListView now_availableRoom_listview;

    ListView later_availableRoom_listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildung_details);

        imgView= (ImageView) findViewById(R.id.img_details);
        textView= (TextView) findViewById(R.id.text_details);

        allRooms_listview=(ListView) findViewById(R.id.allRooms_listview);
        now_availableRoom_listview=(ListView) findViewById(R.id.now_availableRoom_listview);
        later_availableRoom_listview=(ListView) findViewById(R.id.later_availableRoom_listview);



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
        Resources res = this.getResources();
        Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a17str)).getBitmap();
        Building building= new Building("Amalienstr. 17","München",imga17str, null, null, null);


        textView.setText(building.getAddress()+"  "+building.getCity());
        imgView.setImageBitmap(building.getImg());

        ArrayAdapter adapter1= new ArrayAdapter(this, android.R.layout.simple_list_item_1,rooms_availabe);
        now_availableRoom_listview.setAdapter(adapter1);

        ArrayAdapter adapter2= new ArrayAdapter(this, android.R.layout.simple_list_item_1,rooms_availabe);
        later_availableRoom_listview.setAdapter(adapter2);

        ArrayAdapter adapter3= new ArrayAdapter(this, android.R.layout.simple_list_item_1,rooms);
        allRooms_listview.setAdapter(adapter3);
    }
}
