package lmu.de.unificiencyandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import net.danlew.android.joda.JodaTimeAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;
import lmu.de.unificiencyandroid.components.buildings.BuildingsTab;
import lmu.de.unificiencyandroid.components.groups.GroupsTab;
import lmu.de.unificiencyandroid.components.login.LoginActivity;
import lmu.de.unificiencyandroid.components.notes.NotesTab;
import lmu.de.unificiencyandroid.components.settings.ActivityThatStartsCamera;
import lmu.de.unificiencyandroid.components.settings.SettingsTab;

public class MainActivity extends AppCompatActivity{

  @BindView(R.id.drawerLayout)
  DrawerLayout mDrawerLayout;

  @BindView(R.id.navigation_view)
  NavigationView mNavigationView;

  @BindView(R.id.toolbar)
  Toolbar mActionBarToolbar;

  FragmentManager mFragmentManager;
  FragmentTransaction mFragmentTransaction;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    JodaTimeAndroid.init(this);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    Log.d("fcm token", FirebaseInstanceId.getInstance().getToken());
    FirebaseMessaging.getInstance().subscribeToTopic("news");

    /**
     * To set our Toolbar Title depending on the current fragment
     */
    setSupportActionBar(mActionBarToolbar);

    /**
     * Lets inflate the very first fragment
     * Here , we are inflating the TabFragment as the first Fragment
     */

    mFragmentManager = getSupportFragmentManager();
    mFragmentTransaction = mFragmentManager.beginTransaction();
    mFragmentTransaction.replace(R.id.containerView,new BuildingsTab()).commit();

    /**
     * Setup click events on the Navigation View Items.
     */

    mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()){
          case R.id.nav_item_buildings: {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new BuildingsTab()).commit();
            getSupportActionBar().setTitle(getString(R.string.nav_item_buildings));
            break;
          }
          case R.id.nav_item_groups: {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new GroupsTab()).commit();
            getSupportActionBar().setTitle(getString(R.string.nav_item_groups));
            break;
          }

          case R.id.nav_item_notes: {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new NotesTab()).commit();
            getSupportActionBar().setTitle(getString(R.string.nav_item_notes));
            break;
          }

          case R.id.nav_item_setting: {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new SettingsTab()).commit();
            getSupportActionBar().setTitle(getString(R.string.nav_item_setting));
            break;
          }

          case R.id.nav_item_camera: {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new ActivityThatStartsCamera()).commit();
            getSupportActionBar().setTitle(getString(R.string.nav_item_setting));
            break;
          }

          case R.id.nav_item_logout: {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            break;
          }

        }

        return false;
      }

    });

    /**
     * Setup Drawer Toggle of the Toolbar
     */

    android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
    ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
        R.string.app_name);

    mDrawerLayout.addDrawerListener(mDrawerToggle);

    mDrawerToggle.syncState();

  }

  @Override
  public void onBackPressed() {
    new AlertDialog.Builder(this)
        .setMessage("Unificieny beenden?")
        .setCancelable(false)
        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            MainActivity.this.finish();
          }
        })
        .setNegativeButton("Abbrechen", null)
        .show();
  }

}