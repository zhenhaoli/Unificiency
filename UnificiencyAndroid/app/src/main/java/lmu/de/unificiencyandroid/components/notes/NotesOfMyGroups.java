package lmu.de.unificiencyandroid.components.notes;

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
import lmu.de.unificiencyandroid.components.groups.adapters.GroupMemberAdapter;
import lmu.de.unificiencyandroid.components.groups.models.Group;


public class NotesOfMyGroups extends Fragment {

    @BindView(R.id.notes_public_recycler_view)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Note_DividerItemDecoration mDividerItemDecoration;

    ArrayList<String> GroupMember =new ArrayList<String>(){{
        add("Zhenhao");
        add("Robert");
        add("Jindong");
    }};

    List<Group> GroupNotes = Arrays.asList(
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
        mRecyclerView.setAdapter(mAdapter);

        // specify an itemDecoration
        mDividerItemDecoration = new Note_DividerItemDecoration(mRecyclerView.getContext(),(new LinearLayoutManager(this.getContext())).getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        return v;
    }
}