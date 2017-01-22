package lmu.de.unificiencyandroid.components.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

import static java.lang.Boolean.TRUE;

public class NotesOfGroup extends Fragment implements NoteClickListener {

  @BindView(R.id.notes_public_recycler_view)
  RecyclerView mRecyclerView;

  @BindView(R.id.avi)
  com.wang.avi.AVLoadingIndicatorView avi;

  @BindView(R.id.notes_floating_button)
  FloatingActionButton addNewNoteBtn;

  NotesAdapter mAdapter;
  RecyclerView.LayoutManager mLayoutManager;
  NoteDividerItemDecoration mDividerItemDecoration;
  List<Note> notes;
  Integer groupId;
  String groupName;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.notes, null);
    ButterKnife.bind(this, view);

    Bundle bundle = this.getArguments();
    groupId = bundle.getInt("groupId", -1);


    getNotesOfGroup();

    groupName = bundle.getString("name");
    //Toast.makeText(getContext(), groupName+groupId, Toast.LENGTH_LONG).show();
    this.addNewNoteBtn.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent intent= new Intent(getContext(), NoteNew.class);
        intent.putExtra("groupname",groupName);
        startActivity(intent);
      }
    });

    return view;
  }

  public void getNotesOfGroup() {
    avi.show();

    String authToken =  SharedPref.getDefaults("authToken", getContext());

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.get("groups/" + groupId + "/notes/", null, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray notesOfGroup) {
        try {
          Logger.d(notesOfGroup.length() + " notes in group got");

          List<Note> notesFromServer = new ArrayList<>();
          for(int i=0; i<notesOfGroup.length(); i++){
            Integer id = notesOfGroup.getJSONObject(i).getInt("id");
            String topic = notesOfGroup.getJSONObject(i).getString("topic");
            String name = notesOfGroup.getJSONObject(i).getString("name");
            String content = notesOfGroup.getJSONObject(i).getString("content");
            String createdBy = notesOfGroup.getJSONObject(i).getJSONObject("creator").getString("username");

            notesFromServer.add(new Note(id, topic, name, content, createdBy, null));
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
          Logger.e(e, "Exception");

        } finally {
          avi.hide();
        }

      }
    });
  }

  public void onItemClick(View view, int position) {
    Note note = notes.get(position);

    Intent notesDetails = new Intent(getActivity(), NoteDetails.class);
    notesDetails.putExtra("noteId", note.getNoteId());
    notesDetails.putExtra("groupId", groupId);

    startActivityForResult(notesDetails, 1);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        Bundle extras = data.getExtras();
        String message;

        if (extras != null) {
          message = extras.getString("success");
          Message.success(getContext(), message);
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        Logger.d("User back from note details");
      }
    }
    getNotesOfGroup();

  }//onActivityResult
}