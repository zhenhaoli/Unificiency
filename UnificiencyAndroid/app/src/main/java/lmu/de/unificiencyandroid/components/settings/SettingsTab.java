package lmu.de.unificiencyandroid.components.settings;

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

public class SettingsTab extends Fragment {

  public static PagerSlidingTabStrip tabLayout;
  public static ViewPager viewPager;
  public static int tabs = 2 ;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view =  inflater.inflate(R.layout.settings_tab,null);
    tabLayout = (PagerSlidingTabStrip) view.findViewById(R.id.settings_tab);
    viewPager = (ViewPager) view.findViewById(R.id.settings_viewpager);

    viewPager.setAdapter(new SettingsTab.MyAdapter(getChildFragmentManager()));

    tabLayout.setViewPager(viewPager);

    return view;
  }

  class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
      switch (position){
        case 0 : return new Profile();
        case 1 : return new Settings();
      }
      return null;
    }

    @Override
    public int getCount() {
      return tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

      switch (position){
        case 0 :
          return "Profile";
        case 1 :
          return "Notifications";
      }
      return null;
    }
  }
}
