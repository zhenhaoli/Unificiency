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

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.R;

public class BuildingsTab extends Fragment {

  @BindView(R.id.buildings_tab)
  PagerSlidingTabStrip tabLayout;

  @BindView(R.id.buildings_viewpager)
  ViewPager viewPager;

  int tabs = 3 ;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view =  inflater.inflate(R.layout.buildings_tab,null);
    ButterKnife.bind(this, view);

    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
    tabLayout.setViewPager(viewPager);

    return view;
  }

  class MyAdapter extends FragmentPagerAdapter{

    public MyAdapter(FragmentManager fm) {
      super(fm);
    }

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
      return tabs;
    }

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