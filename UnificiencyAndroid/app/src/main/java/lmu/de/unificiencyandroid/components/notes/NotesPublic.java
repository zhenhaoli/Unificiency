package lmu.de.unificiencyandroid.components.notes;

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
import android.support.v7.widget.DividerItemDecoration;

public class NotesPublic extends Fragment {

    @BindView(R.id.notes_public_recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Note_DividerItemDecoration mDividerItemDecoration;

  List<Note> publicNotes = Arrays.asList(
          new Note("MSP", "REST", "CRUD with HTTP", "Zhen", 8),
          new Note("MMN", "Bluetooth", "Connectivity", "Rob",6),
          new Note("IV", "GPS", "Outdoor Positioning", "Jin", 12),
          new Note("MMu2", "REST", "CRUD with HTTP", "Zhen", -3),
          new Note("itsec", "REST", "CRUD with HTTP", "Zhen", -5),
          new Note("itjur", "REST", "CRUD with HTTP", "Zhen", -9)
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
      mAdapter = new NotesAdapter(publicNotes);
      mRecyclerView.setAdapter(mAdapter);

      // specify an itemDecoration
      mDividerItemDecoration = new Note_DividerItemDecoration(mRecyclerView.getContext(),(new LinearLayoutManager(this.getContext())).getOrientation());
      mRecyclerView.addItemDecoration(mDividerItemDecoration);

      return v;
    }
}