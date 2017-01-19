package lmu.de.unificiencyandroid.components.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import lmu.de.unificiencyandroid.R;

public class SettingGroups extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_groups);

        listView=(ListView) findViewById(R.id.settinggroup_list);
        SettingGroupsAdapter adapter= new SettingGroupsAdapter(this, android.R.layout.simple_list_item_1, null);
        listView.setAdapter(adapter);

    }
}
