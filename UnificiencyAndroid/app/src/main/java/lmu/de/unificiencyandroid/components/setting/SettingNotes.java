package lmu.de.unificiencyandroid.components.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.notes.NotesPublic;

public class SettingNotes extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_notes);

        listView=(ListView) findViewById(R.id.settingnote_list);
        SettingNotesAdapter adapter= new SettingNotesAdapter(this, android.R.layout.simple_list_item_1, NotesPublic.publicNotes);
        listView.setAdapter(adapter);
    }
}
