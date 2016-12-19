package lmu.de.unificiencyandroid.view.buildings;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.adapters.BuildingDetailsAdapter;
import lmu.de.unificiencyandroid.model.Building;
import lmu.de.unificiencyandroid.model.Room;


public class BuildingDetails extends AppCompatActivity {

    List<Room> rooms= new ArrayList<Room>();
    List<Room> rooms_availabe= new ArrayList<Room>();

    ImageView imgView;

    TextView textView;

    ListView section_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildung_details);

        imgView= (ImageView) findViewById(R.id.img_details);
        textView= (TextView) findViewById(R.id.text_details);
        section_listview=(ListView) findViewById(R.id.section_listview);

        for (int i=0; i<3;i++){
            rooms.add(new Room("U01"+i, true));
            rooms.add(new Room("10"+i, true));
            rooms.add(new Room("03"+i, false));
            rooms.add(new Room("12"+i, false));
            rooms.add(new Room("20"+i, false));
            rooms.add(new Room("13"+i, false));
            rooms.add(new Room("22"+i, false));
            rooms.add(new Room("00"+i, false));
            rooms.add(new Room("33"+i, false));
            rooms.add(new Room("10"+i, false));
            rooms.add(new Room("23"+i, false));
            rooms.add(new Room("03"+i, false));
            rooms.add(new Room("12"+i, false));
            rooms.add(new Room("20"+i, false));
            rooms.add(new Room("13"+i, false));
            rooms.add(new Room("22"+i, false));
            rooms.add(new Room("00"+i, false));
            rooms.add(new Room("33"+i, false));
            rooms.add(new Room("10"+i, false));
            rooms.add(new Room("23"+i, false));


        }

        BuildingDetailsAdapter buildingDetailsAdapter;
        buildingDetailsAdapter=new BuildingDetailsAdapter(this);


        buildingDetailsAdapter.addSectionHeaderItem("Jetzt Frei");
        for (Room room : rooms) {
            if(room.getAvailability()==true) buildingDetailsAdapter.addItem(room.toString());
        }

        buildingDetailsAdapter.addSectionHeaderItem("Bald Frei");
        for (Room room : rooms) {
            if(room.getAvailability()==true) buildingDetailsAdapter.addItem(room.toString());
        }

        buildingDetailsAdapter.addSectionHeaderItem("Alle");
        for (Room room : rooms) {
            buildingDetailsAdapter.addItem(room.toString());
        }


        Intent intent=getIntent();
        Resources res = this.getResources();
        Bitmap imga17str = ((BitmapDrawable) res.getDrawable(R.drawable.a7astr)).getBitmap();
        Building building= new Building("Amalienstr. 17","München",imga17str, null, null, null);


        textView.setText(intent.getStringExtra("address")+",  "+intent.getStringExtra("city"));
        imgView.setImageBitmap(building.getImg());

        section_listview.setAdapter(buildingDetailsAdapter);
    }
}
