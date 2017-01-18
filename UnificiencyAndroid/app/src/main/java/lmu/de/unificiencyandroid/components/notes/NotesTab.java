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

import lmu.de.unificiencyandroid.R;

public class NotesTab extends Fragment {

  public static PagerSlidingTabStrip tabLayout;
  public static ViewPager viewPager;
  public static int int_items = 10 ;

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
    viewPager.setAdapter(new NotesTab.MyAdapter(getChildFragmentManager()));

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

    @Override
    public Fragment getItem(int position)
    {
      switch (position){
        case 0 : return new NotesPublic();
        case 1 : return new NotesFavorite();
        case 2 :
        case 3 :
        case 4 :
        case 5 :
        case 6 :
        case 7 :
        case 8 :
        case 9:
          return new NotesPublic();
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
          return "Öffentlich";
        case 1 :
          return "Favoriten";
        case 2:
          return "MSP Praktikum";
        case 3 :
          return "Verteilte Systeme";
        case 4 :
          return "Lernen";
        case 5 :
          return "Chillen";
        case 6 :
          return "Essen";
        case 7 :
          return "Python";
        case 8 :
          return "Java";
        case 9:
          return "iOS";
      }
      return null;
    }
  }

}