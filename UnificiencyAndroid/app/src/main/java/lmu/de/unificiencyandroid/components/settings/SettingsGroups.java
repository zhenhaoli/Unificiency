package lmu.de.unificiencyandroid.components.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import lmu.de.unificiencyandroid.R;

public class SettingsGroups extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_groups);

        listView=(ListView) findViewById(R.id.settinggroup_list);
        SettingsGroupsAdapter adapter= new SettingsGroupsAdapter(this, android.R.layout.simple_list_item_1, null);
        listView.setAdapter(adapter);

    }
}
