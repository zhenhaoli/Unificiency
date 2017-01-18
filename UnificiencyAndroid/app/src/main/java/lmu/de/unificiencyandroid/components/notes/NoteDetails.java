package lmu.de.unificiencyandroid.components.notes;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import lmu.de.unificiencyandroid.R;


public class NoteDetails extends AppCompatActivity {

    public TextView note_detail_course;
    public TextView note_detail_title;
    public TextView note_detail_creator;
    public TextView note_detail_content;
    public TextView note_detail_rating;


    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_noteDetials);
        toolbar.setTitle(R.string.note_detail);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setupViewReferences() {
        note_detail_course=(TextView) findViewById(R.id.note_detail_course);
        note_detail_title=(TextView) findViewById(R.id.note_detail_title);
        note_detail_creator=(TextView) findViewById(R.id.note_detail_creator);
        note_detail_content=(TextView) findViewById(R.id.note_detail_content);
        note_detail_rating=(TextView) findViewById(R.id.note_detail_rating);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        setupToolbar();
        setupViewReferences();


        Intent intent = getIntent();
        note_detail_course.setText("Vorlesung: "+intent.getStringExtra("course"));
        note_detail_title.setText("Titel: "+intent.getStringExtra("title"));
        note_detail_creator.setText("Ersteller: "+intent.getStringExtra("creator"));
        note_detail_rating.setText("Rank: "+intent.getStringExtra("rating"));
        note_detail_content.setText("Content: \n"+intent.getStringExtra("content"));
    }
}
