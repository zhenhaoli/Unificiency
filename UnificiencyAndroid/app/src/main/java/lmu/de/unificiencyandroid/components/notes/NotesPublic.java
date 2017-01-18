package lmu.de.unificiencyandroid.components.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.buildings.BuildingDetails;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.Toast;

public class NotesPublic extends Fragment implements MyItemClickListener {

    @BindView(R.id.notes_public_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.notes_floating_button)
    FloatingActionButton addNewNoteBtn;
    private NotesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Note_DividerItemDecoration mDividerItemDecoration;



   public static List<Note> publicNotes = Arrays.asList(
          new Note("MSP", "REST", "CRUD with HTTP", "Zhen", 8),
          new Note("MMN", "Bluetooth", "Connectivity", "Rob",6),
          new Note("IV", "GPS", "Outdoor Positioning", "Jin", 12),
          new Note("MMu2", "REST", "CRUD with HTTP", "Zhen", 3),
          new Note("itsec", "REST", "CRUD with HTTP", "Zhen", 5),
          new Note("itjur", "REST", "CRUD with HTTP", "Zhen", 9)
   );

    public void onAddNote(View view) {
        Intent intent= new Intent(getContext(), NewNote.class);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View v = inflater.inflate(R.layout.notes_public, null);
        ButterKnife.bind(this, v);

      this.addNewNoteBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view) {
              onAddNote(view);
          }
      });

      // use a linear layout manager
      mLayoutManager = new LinearLayoutManager(getContext());
      mRecyclerView.setLayoutManager(mLayoutManager);

      // specify an adapter
      mAdapter = new NotesAdapter(publicNotes);
      mAdapter.setOnItemClickListener(this);
      mRecyclerView.setAdapter(mAdapter);
      mRecyclerView.setItemAnimator(new DefaultItemAnimator());
      // specify an itemDecoration
      mDividerItemDecoration = new Note_DividerItemDecoration(mRecyclerView.getContext(),(new LinearLayoutManager(this.getContext())).getOrientation());
      mRecyclerView.addItemDecoration(mDividerItemDecoration);

      return v;
    }
    public void onItemClick(View view, int position) {

        Note note=(Note) publicNotes.get(position);
        Intent notesDetails=new Intent(getActivity(),NoteDetails.class);
        notesDetails.putExtra("course", note.getCourse());
        notesDetails.putExtra("title", note.getTitle());
        notesDetails.putExtra("content", note.getContent());
        notesDetails.putExtra("creator", note.getCreatedBy());
        notesDetails.putExtra("rating", note.getRating().toString());
        startActivity(notesDetails);
    }
}