package lmu.de.unificiencyandroid.components.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;

import static java.lang.Boolean.TRUE;

public class NotesOfGroup extends Fragment implements NoteClickListener {

  @BindView(R.id.notes_public_recycler_view)
  RecyclerView mRecyclerView;

  @BindView(R.id.avi)
  com.wang.avi.AVLoadingIndicatorView avi;

  NotesAdapter mAdapter;
  RecyclerView.LayoutManager mLayoutManager;
  NoteDividerItemDecoration mDividerItemDecoration;
  List<Note> notes;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.notes, null);
    ButterKnife.bind(this, view);
    Bundle bundle = this.getArguments();
    if (bundle != null) {
      Integer groupId = bundle.getInt("groupId", -1);

      avi.show();

      String authToken =  SharedPref.getDefaults("authToken", getContext());

      UnificiencyClient client = new PythonAPIClient();
      client.addHeader("Authorization", authToken);
      client.get("notes/" + groupId, null, new JsonHttpResponseHandler() {
        public void onFailure(int statusCode, byte[] errorResponse, Throwable e){
          Log.e("status", statusCode + "" );
          Log.e("e", e.toString());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray notesOfGroup) {
          // Pull out the first event on the public timeline
          try {
            Log.d("notes in group", notesOfGroup.length()+"");

            List<Note> notesFromServer = new ArrayList<>();
            for(int i=0; i<notesOfGroup.length(); i++){
              String topic = notesOfGroup.getJSONObject(i).getString("topic");
              String name = notesOfGroup.getJSONObject(i).getString("name");
              String content = notesOfGroup.getJSONObject(i).getString("content");
              String createdBy = notesOfGroup.getJSONObject(i).getJSONObject("creator").getString("username");

              notesFromServer.add(new Note(topic, name, content, createdBy, null));
            }

            notes = notesFromServer;
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter
            mAdapter = new NotesAdapter(notes);
            mAdapter.setFavoriteFalg(TRUE);
            mAdapter.setOnItemClickListener(NotesOfGroup.this);
            mRecyclerView.setAdapter(mAdapter);

            // specify an itemDecoration
            mDividerItemDecoration = new NoteDividerItemDecoration(mRecyclerView.getContext(), (new LinearLayoutManager(NotesOfGroup.this.getContext())).getOrientation());
            mRecyclerView.addItemDecoration(mDividerItemDecoration);

          } catch (Exception e) {
            Log.e("NotesOfGroup", e.toString());

          } finally {
            avi.hide();
          }

        }
      });

    }
    return view;
  }

  public void onItemClick(View view, int position) {
    Note note= notes.get(position);
    Intent notesDetails=new Intent(getActivity(),NoteDetails.class);
    notesDetails.putExtra("course", note.getTopic());
    notesDetails.putExtra("title", note.getTitle());
    notesDetails.putExtra("content", note.getContent());
    notesDetails.putExtra("creator", note.getCreatedBy());
    startActivity(notesDetails);
  }
}