package lmu.de.unificiencyandroid.components.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.adapters.GroupsAdapter;
import lmu.de.unificiencyandroid.components.groups.models.Group;
import lmu.de.unificiencyandroid.network.NodeAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.untils.SharedPref;


public class GroupsFragment extends Fragment {
   RecyclerView groupsRecyclerView;
   RecyclerView.Adapter groupsAdapter;
   RecyclerView.LayoutManager groupsLayoutManager;
   NestedScrollView groupsScrollview;
   AppBarLayout groupsAppBar;
   FloatingActionButton addNewGroupBtn;


  public GroupsFragment() {
    // Required empty public constructor
  }

  public void setupViewReferences(View view){
    this.groupsScrollview = (NestedScrollView) view.findViewById(R.id.groups_nested_scroll_view);
    this.groupsAppBar = (AppBarLayout) view.findViewById(R.id.groups_app_bar_layout);
    this.addNewGroupBtn = (FloatingActionButton) view.findViewById(R.id.groups_floating_button);
  }

  public void bindGroupData(){

    String authToken =  SharedPref.getDefaults("authToken", getContext());

    Log.d("gf Token in sharedPref", authToken);

    UnificiencyClient client = new NodeAPIClient();
    client.addHeader("Authorization", "Bearer " + authToken);
    client.get("groups", null, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // If the response is JSONObject instead of expected JSONArray
      }
      public void onFailure(int statusCode, byte[] errorResponse, Throwable e){
        Log.e("status", statusCode + "" );
        Log.e("e", e.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray groups) {
        // Pull out the first event on the public timeline
        try {
          Log.d("Groups", groups.length()+"");

          List<Group> groupsFromServer = new ArrayList<>();
          for(int i=0; i<groups.length(); i++){
            String name = groups.getJSONObject(i).getString("name");
            String topic = groups.getJSONObject(i).getString("topic");
            String description = groups.getJSONObject(i).getString("description");
            groupsFromServer.add(new Group(name, topic, description, null, null));
          }
          
          groupsAdapter = new GroupsAdapter(getActivity(), groupsFromServer);
          groupsRecyclerView.setAdapter(groupsAdapter);
          
        } catch (Exception e) {
          Log.e("GroupAll", e.toString());
        }

      }
    });

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
