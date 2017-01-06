package lmu.de.unificiencyandroid.components.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import lmu.de.unificiencyandroid.R;

public class NotesFromSingleGroup extends AppCompatActivity implements MyItemClickListener{

    private TextView notes_singleGroup_name;
    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;
    private Note_DividerItemDecoration mDividerItemDecoration;

    List<Note> notes_from_singleGroup = Arrays.asList(
            new Note("MSP", "REST", "CRUD with HTTP", "Zhen", 8),
            new Note("MMN", "Bluetooth", "Connectivity", "Rob",6),
            new Note("IV", "GPS", "Outdoor Positioning", "Jin", 12),
            new Note("MMu2", "REST", "CRUD with HTTP", "Zhen", -3),
            new Note("itsec", "REST", "CRUD with HTTP", "Zhen", -5),
            new Note("itjur", "REST", "CRUD with HTTP", "Zhen", -9)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_from_single_group);

        notes_singleGroup_name=(TextView) findViewById(R.id.notes_singleGroup_name);
        mRecyclerView=(RecyclerView) findViewById(R.id.notes_from_singleGroup);

        Intent intent = getIntent();
        notes_singleGroup_name.setText("Alle Notizen im Group: "+intent.getStringExtra("groupName"));

        // specify an adapter
        mAdapter = new NotesAdapter(notes_from_singleGroup);
        mAdapter.setOnItemClickListener(this);


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // specify an itemDecoration
        mDividerItemDecoration = new Note_DividerItemDecoration(mRecyclerView.getContext(),(new LinearLayoutManager(this.getApplicationContext())).getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

    }


    public void onItemClick(View view, int position) {

        Note note=(Note) notes_from_singleGroup.get(position);
        Intent notesDetails=new Intent(getApplicationContext(),NoteDetails.class);
        notesDetails.putExtra("course", note.getCourse());
        notesDetails.putExtra("title", note.getTitle());
        notesDetails.putExtra("content", note.getContent());
        notesDetails.putExtra("creator", note.getCreatedBy());
        notesDetails.putExtra("rating", note.getRating().toString());
        startActivity(notesDetails);
    }
}
