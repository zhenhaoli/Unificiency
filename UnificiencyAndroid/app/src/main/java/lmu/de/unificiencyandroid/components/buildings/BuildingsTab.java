package lmu.de.unificiencyandroid.components.buildings;

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

public class BuildingsTab extends Fragment {

  public static PagerSlidingTabStrip tabLayout;
  public static ViewPager viewPager;
  public static int int_items = 3 ;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    /**
     *Inflate tab_layout and setup Views.
     */
    View view =  inflater.inflate(R.layout.buildings_tab,null);
    tabLayout = (PagerSlidingTabStrip) view.findViewById(R.id.buildings_tab);
    viewPager = (ViewPager) view.findViewById(R.id.buildings_viewpager);

    /**
     *Set an Apater for the View Pager
     */
    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

    tabLayout.setViewPager(viewPager);

    return view;

  }

  class MyAdapter extends FragmentPagerAdapter{

    public MyAdapter(FragmentManager fm) {
      super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position)
    {
      switch (position){
        case 0 : return new BuildingsNearest();
        case 1 : return new BuildingsAll();
        case 2 : return new BuildingsMap();
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
          return "In der Nähe";
        case 1 :
          return "Alle Gebäude";
        case 2 :
          return "Karte";
      }
      return null;
    }
  }

}