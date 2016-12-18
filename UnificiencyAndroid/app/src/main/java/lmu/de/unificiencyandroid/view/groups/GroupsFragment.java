package lmu.de.unificiencyandroid.view.groups;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.adapters.GroupsAdapter;


public class GroupsFragment extends Fragment {
    private RecyclerView groupsRecyclerView;
    private RecyclerView.Adapter groupsAdapter;
    private RecyclerView.LayoutManager groupsLayoutManager;
    private NestedScrollView groupsScrollview;
    private AppBarLayout groupsAppBar;


    public GroupsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        groupsRecyclerView = (RecyclerView) view.findViewById(R.id.groups_recycler_view);
        //fix scrolling issues when inside a nestedScrollView
        groupsRecyclerView.setNestedScrollingEnabled(false);
        // use a linear layout manager
        groupsLayoutManager = new LinearLayoutManager(this.getActivity());
        groupsRecyclerView.setLayoutManager(groupsLayoutManager);
        this.groupsScrollview = (NestedScrollView) view.findViewById(R.id.groups_nested_scroll_view);
        this.groupsAppBar = (AppBarLayout) view.findViewById(R.id.groups_app_bar_layout);
        groupsScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                if(scrollY == 0){
                    groupsAppBar.setExpanded(true);
                }
            }
        });
        this.groupsRecyclerView.setVerticalScrollBarEnabled(true);
        //bind to data
        groupsAdapter = new GroupsAdapter((getActivity()));
        groupsRecyclerView.setAdapter(groupsAdapter);
        return view;
    }



}
