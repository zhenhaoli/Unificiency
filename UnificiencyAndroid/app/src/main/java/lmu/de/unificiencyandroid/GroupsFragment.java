package lmu.de.unificiencyandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lmu.de.unificiencyandroid.adapters.GroupsAdapter;


public class GroupsFragment extends Fragment {
    private RecyclerView groupsRecyclerView;
    private RecyclerView.Adapter groupsAdapter;
    private RecyclerView.LayoutManager GroupsLayoutManager;


    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        groupsRecyclerView = (RecyclerView) view.findViewById(R.id.groups_recycler_view);
        // use a linear layout manager
        GroupsLayoutManager = new LinearLayoutManager(this.getActivity());
        groupsRecyclerView.setLayoutManager(GroupsLayoutManager);
        //bind to data
        groupsAdapter = new GroupsAdapter((getActivity()));
        groupsRecyclerView.setAdapter(groupsAdapter);
        return view;
    }

}
