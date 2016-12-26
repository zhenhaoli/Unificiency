package lmu.de.unificiencyandroid.view.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lmu.de.unificiencyandroid.R;

public class GroupsFragment extends Fragment {
    private RecyclerView groupsRecyclerView;
    private RecyclerView.Adapter groupsAdapter;
    private RecyclerView.LayoutManager groupsLayoutManager;
    private NestedScrollView groupsScrollview;
    private AppBarLayout groupsAppBar;
    private FloatingActionButton addNewGroupBtn;


    public GroupsFragment() {
        // Required empty public constructor
    }

    public void setupViewReferences(View view){
        this.groupsScrollview = (NestedScrollView) view.findViewById(R.id.groups_nested_scroll_view);
        this.groupsAppBar = (AppBarLayout) view.findViewById(R.id.groups_app_bar_layout);
        this.addNewGroupBtn = (FloatingActionButton) view.findViewById(R.id.groups_floating_button);
    }

    public void bindGroupData(){
        this.groupsAdapter = new GroupsAdapter((getActivity()));
        this.groupsRecyclerView.setAdapter(groupsAdapter);
    }

    public void onAddGroup(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, NewGroup.class);
        context.startActivity(intent);
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
        setupViewReferences(view);
        this.groupsScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                if(scrollY == 0){
                    groupsAppBar.setExpanded(true);
                }
            }
        });
        this.addNewGroupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onAddGroup(view);
            }
        });
        this.groupsRecyclerView.setVerticalScrollBarEnabled(true);
        //bind to data
        bindGroupData();
        return view;
    }

}
