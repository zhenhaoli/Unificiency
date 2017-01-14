package lmu.de.unificiencyandroid.components.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.notes.NotesOfMyGroups;

public class SettingGroups extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_groups);

        listView=(ListView) findViewById(R.id.settinggroup_list);
        SettingGroupsAdapter adapter= new SettingGroupsAdapter(this, android.R.layout.simple_list_item_1, NotesOfMyGroups.GroupNotes);
        listView.setAdapter(adapter);

    }
}
