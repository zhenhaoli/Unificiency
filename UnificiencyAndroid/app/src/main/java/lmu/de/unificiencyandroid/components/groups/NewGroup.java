package lmu.de.unificiencyandroid.components.groups;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import lmu.de.unificiencyandroid.R;

public class NewGroup extends AppCompatActivity {

    private Button createButton;
    private EditText description;
    private EditText name;
    private EditText password;

    public void setupViewReferences() {
        this.createButton = (Button) findViewById(R.id.groups_new_group_create);
        this.description = (EditText) findViewById(R.id.groups_new_group_description);
        this.name = (EditText) findViewById(R.id.groups_new_group_name);
        this.password = (EditText) findViewById(R.id.groups_new_group_password);
    }

    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.groups_new_group_title);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        setupToolbar();
        setupViewReferences();
    }

    /* restore back button functionality*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:{return super.onOptionsItemSelected(item);}

        }
    }
}
