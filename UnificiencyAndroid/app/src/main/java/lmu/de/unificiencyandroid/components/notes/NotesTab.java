package lmu.de.unificiencyandroid.components.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.groups.Group;
import lmu.de.unificiencyandroid.network.PythonAPIClient;
import lmu.de.unificiencyandroid.network.UnificiencyClient;
import lmu.de.unificiencyandroid.utils.Message;
import lmu.de.unificiencyandroid.utils.SharedPref;

public class NotesTab extends Fragment {

  PagerSlidingTabStrip tabLayout;
  ViewPager viewPager;

  List<Group> myGroups;

  com.wang.avi.AVLoadingIndicatorView avi;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view =  inflater.inflate(R.layout.notes_tab,null);
    avi = (com.wang.avi.AVLoadingIndicatorView)view.findViewById(R.id.avi);

    getTabsForMyGroups(view);

    return view;
  }

  class MyAdapter extends FragmentPagerAdapter {

    List<Group> myGroups;

    public MyAdapter(FragmentManager fm, List<Group> myGroups) {

      super(fm);
      this.myGroups = myGroups;
    }

    @Override
    public Fragment getItem(int position)
    {

      if(position <=  getCount() && position >= 0){
        switch (position) {
          case 0:
            return new NotesFavorite();
          case 1:
            return new NotesPublic();
          default: {
            NotesOfGroup notesOfGroup =  new NotesOfGroup();
            Bundle bundle = new Bundle();
            bundle.putInt("groupId", myGroups.get(position).getId());
            bundle.putString("name", myGroups.get(position).getName());
            notesOfGroup.setArguments(bundle);
            return notesOfGroup;
          }
        }

      }
      return null;
    }

    @Override
    public int getCount() {
      return myGroups.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if(position <= getCount() && position >= 0) {
        switch (position) {
          case 0:
            return "Favoriten";
          case 1:
            return "Öffentlich";
          default:
            return myGroups.get(position).getName();
        }
      }
      return null;
    }
  }

  public void getTabsForMyGroups(final View view){
    avi.show();
    String authToken =  SharedPref.getDefaults("authToken", getContext());

    final RequestParams params = new RequestParams();
    params.put("isMember", true);

    UnificiencyClient client = new PythonAPIClient();
    client.addHeader("Authorization", authToken);
    client.get("groups/lmu", params, new JsonHttpResponseHandler() {

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Logger.json(errorResponse.toString());
        Message.fail(getContext(), errorResponse.toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray groups) {

        try {
          Logger.d("Initialized tabs with group names");

          List<Group> groupsFromServer = new ArrayList<>();

          groupsFromServer.add(new Group(null,"Öffentlich", null ,null,null,false));
          groupsFromServer.add(new Group(null,"Favoriten", null, null,null, false));
          for(int i=0; i<groups.length(); i++){
            Integer id = groups.getJSONObject(i).getInt("id");
            String name = groups.getJSONObject(i).getString("name");
            String topic = groups.getJSONObject(i).getString("topic_area");
            Boolean hasPassword = groups.getJSONObject(i).getBoolean("protected");
            groupsFromServer.add(new Group(id, name, topic, null, null, hasPassword));
          }

          myGroups = groupsFromServer;

          tabLayout = (PagerSlidingTabStrip) view.findViewById(R.id.notes_tab);
          viewPager = (ViewPager) view.findViewById(R.id.notes_viewpager);

          viewPager.setAdapter(new NotesTab.MyAdapter(getChildFragmentManager(), myGroups));
          tabLayout.setViewPager(viewPager);

        } catch (Exception e) {
          Logger.e(e, "Exception");

        } finally {
          avi.hide();
        }

      }

    });

  }

}