package lmu.de.unificiencyandroid.components.groups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.adapters.GroupsAdapter;
import lmu.de.unificiencyandroid.components.groups.models.Group;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.SharedPref;


public class GroupsFragment extends Fragment {
  RecyclerView groupsRecyclerView;
  GroupsAdapter groupsAdapter;
  RecyclerView.LayoutManager groupsLayoutManager;
  NestedScrollView groupsScrollview;
  AppBarLayout groupsAppBar;
  FloatingActionButton addNewGroupBtn;
  com.wang.avi.AVLoadingIndicatorView avi;

  public GroupsFragment() {
    // Required empty public constructor
  }

  public void setupViewReferences(View view){
    this.groupsScrollview = (NestedScrollView) view.findViewById(R.id.groups_nested_scroll_view);
    this.groupsAppBar = (AppBarLayout) view.findViewById(R.id.groups_app_bar_layout);
    this.addNewGroupBtn = (FloatingActionButton) view.findViewById(R.id.groups_floating_button);
    this.avi = (com.wang.avi.AVLoadingIndicatorView)view.findViewById(R.id.avi);
  }

  public void bindGroupData(){
    avi.show();
    String authToken =  SharedPref.getDefaults("authTokenPython", getContext());

    Log.d("gf Token in sharedPref", authToken);

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.get("groups/lmu/", null, new JsonHttpResponseHandler() {
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
            Integer id = groups.getJSONObject(i).getInt("id");

            String name = groups.getJSONObject(i).getString("name");
            String topic = groups.getJSONObject(i).getString("topic_area");
            //String description = groups.getJSONObject(i).getString("description");
            //JSONArray members = groups.getJSONObject(i).getJSONArray("members");
           // List<String> memberNames = new ArrayList<String>();
            //for(int j = 0; j<members.length(); j++){
            //  memberNames.add(members.getJSONObject(j).getString("username"));
           // }

            groupsFromServer.add(new Group(id, name, topic, null, null, null));
          }
          Collections.reverse(groupsFromServer);
          if(groupsAdapter == null) {
            groupsAdapter = new GroupsAdapter(getActivity(), groupsFromServer, new GroupsAdapter.OnGroupItemClickListener() {
              @Override
              public void onGroupClick(Group group) {
                Intent intent = new Intent(getContext(), GroupDetails.class);
                intent.putExtra("groupId", group.getId());
                startActivity(intent);
              }
            });
          } else {
            groupsAdapter.setData(groupsFromServer);
          }

          groupsRecyclerView.setAdapter(groupsAdapter);

        } catch (Exception e) {
          Log.e("GroupAll", e.toString());

        } finally {
          avi.hide();
        }

      }
    });

  }

  public void onAddGroup(View view) {
    Intent intent= new Intent(getContext(), NewGroup.class);
    startActivityForResult(intent, 1);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.groups, container, false);
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

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == 1) {
      if(resultCode == Activity.RESULT_OK){

        bindGroupData();

        Bundle extras = data.getExtras();
        String createdGroupMsg;

        if (extras != null) {
          createdGroupMsg = extras.getString("createGroupSuccess");
          SuperActivityToast.cancelAllSuperToasts();
          SuperActivityToast.create(getContext(), new Style(), Style.TYPE_STANDARD)
              .setText(createdGroupMsg)
              .setDuration(Style.DURATION_LONG)
              .setFrame(Style.FRAME_KITKAT)
              .setColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null))
              .setAnimations(Style.ANIMATIONS_SCALE)
              .show();
        }

      }
      if (resultCode == Activity.RESULT_CANCELED) {
        //Write your code if there's no result
      }
    }
  }//onActivityResult



}
