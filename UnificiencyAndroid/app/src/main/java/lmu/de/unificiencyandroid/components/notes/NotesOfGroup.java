package lmu.de.unificiencyandroid.components.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import static java.lang.Boolean.TRUE;

public class NotesOfGroup extends Fragment implements NoteClickListener {

  @BindView(R.id.notes_public_recycler_view)
  RecyclerView mRecyclerView;
  private NotesAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;
  private NoteDividerItemDecoration mDividerItemDecoration;

  List<Note> favoriteNotes = Arrays.asList(
      new Note("Favorite_Note1", "Bluetooth", "Connectivity", "Rob", 6),
      new Note("Favorite_Note3", "GPS", "Outdoor Positioning", "Jin", 12),
      new Note("Favorite_Note4", "REST", "CRUD with HTTP", "Zhen", 3),
      new Note("Favorite_Note2", "REST", "CRUD with HTTP", "Zhen", 5),
      new Note("Favorite_Note5", "REST", "CRUD with HTTP", "Zhen", 9)
  );


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.notes_public, null);
    ButterKnife.bind(this, v);

    // use a linear layout manager
    mLayoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);

    // specify an adapter
    mAdapter = new NotesAdapter(favoriteNotes);
    mAdapter.setFavoriteFalg(TRUE);
    mAdapter.setOnItemClickListener(this);
    mRecyclerView.setAdapter(mAdapter);

    // specify an itemDecoration
    mDividerItemDecoration = new NoteDividerItemDecoration(mRecyclerView.getContext(), (new LinearLayoutManager(this.getContext())).getOrientation());
    mRecyclerView.addItemDecoration(mDividerItemDecoration);

    return v;
  }
  public void onItemClick(View view, int position) {
    Note note=(Note) favoriteNotes.get(position);
    Intent notesDetails=new Intent(getActivity(),NoteDetails.class);
    notesDetails.putExtra("course", note.getCourse());
    notesDetails.putExtra("title", note.getTitle());
    notesDetails.putExtra("content", note.getContent());
    notesDetails.putExtra("creator", note.getCreatedBy());
    notesDetails.putExtra("rating", note.getRating().toString());
    startActivity(notesDetails);
  }
}