package lmu.de.unificiencyandroid.components.groups;

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

import lmu.de.unificiencyandroid.R;
import lmu.de.unificiencyandroid.components.notes.NotesFavorite;

public class GroupsTab  extends Fragment {

  public static PagerSlidingTabStrip tabLayout;
  public static ViewPager viewPager;
  public static int int_items = 2 ;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    /**
     *Inflate tab_layout and setup Views.
     */
    View x =  inflater.inflate(R.layout.notes_tab,null);
    tabLayout = (PagerSlidingTabStrip) x.findViewById(R.id.notes_tab);
    viewPager = (ViewPager) x.findViewById(R.id.notes_viewpager);

    /**
     *Set an Apater for the View Pager
     */
    viewPager.setAdapter(new GroupsTab.MyAdapter(getChildFragmentManager()));

    tabLayout.setViewPager(viewPager);

    return x;

  }

  class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
      super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    //TODO: Make dynamic based on groups of users load from db
    @Override
    public Fragment getItem(int position)
    {
      switch (position){
        case 0 : return new GroupsFragment();
        case 1 : return new GroupsMyFragment();
      }
      return null;
    }

    @Override
    public int getCount() {

      return int_items;

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

      switch (position){
        case 0 :
          return "Lerngruppen";
        case 1 :
          return "Meine Gruppen";
      }
      return null;
    }
  }

}