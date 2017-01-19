package lmu.de.unificiencyandroid.components.notes;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import lmu.de.unificiencyandroid.R;

public class NoteNew extends AppCompatActivity {

    private Button createButton;
    private TextInputLayout topic;
    private TextInputLayout content;
    private TextInputLayout name;
    private TextInputLayout password;

    public void setupViewReferences() {
        this.createButton = (Button) findViewById(R.id.notes_new_note_create);
        this.topic = (TextInputLayout) findViewById(R.id.notes_new_new_topic);
        this.name = (TextInputLayout) findViewById(R.id.notes_new_note_name);
        this.password = (TextInputLayout) findViewById(R.id.notes_new_note_password);
        this.content = (TextInputLayout) findViewById(R.id.notes_new_note_content);
    }

    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_note);
        toolbar.setTitle(R.string.notes_new_note_title);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_new);
        setupViewReferences();
        setupToolbar();

        this.name.getEditText().setText("Notizname ");
        this.content.getEditText().setText("Notizinhalt");
        this.password.getEditText().setText("123456");

        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
