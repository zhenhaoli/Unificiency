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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.models.Group;


public class NotesOfMyGroups extends Fragment implements MyItemClickListener{

    @BindView(R.id.notes_public_recycler_view)
    RecyclerView mRecyclerView;
    private NotesGroupAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Note_DividerItemDecoration mDividerItemDecoration;

    //fake data
   public static ArrayList<String> GroupMember =new ArrayList<String>(){{
        add("Zhenhao");
        add("Robert");
        add("Jindong");
    }};

   public static List<Group> GroupNotes = Arrays.asList(
            new Group("Group_Name_1", "MSP", GroupMember, "wearebest"),
            new Group("Group_Name_2", "GPS", GroupMember,"wearebest"),
            new Group("Group_Name_3", "REST", GroupMember, "wearebest")
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
        mAdapter = new NotesGroupAdapter(GroupNotes);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // specify an itemDecoration
        mDividerItemDecoration = new Note_DividerItemDecoration(mRecyclerView.getContext(),(new LinearLayoutManager(this.getContext())).getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        return v;
    }
    public void onItemClick(View view, int position) {
        Group group=(Group) GroupNotes.get(position);
        Intent notesFromSingleGroup=new Intent(getActivity(),NotesFromSingleGroup.class);
        notesFromSingleGroup.putExtra("groupName", group.getName());
        notesFromSingleGroup.putExtra("groupDescription", group.getDescription());
       // notesFromSingleGroup.putExtra("groupMember", group.getMembers());
        startActivity(notesFromSingleGroup);
    }
}