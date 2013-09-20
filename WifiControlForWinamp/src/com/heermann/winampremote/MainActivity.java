package com.heermann.winampremote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.heermann.winampremote.services.ConnectionService;

public class MainActivity extends ActionBarActivity {

  // /---------------------- UI -------------------///
  private ActionBar actionBar;
  private ViewPager mViewPager;
  private PageAdapter mPageAdapter;
  private ProgressDialog workingDialog;

  // /---------------------- OTHER -------------------///
  public static SharedPreferences preferences;
  public static Resources resources;

  private WifiControlApp wifiControlApp;

  // Receivers
  private WifiControlReceiver wifiControlReceiver;

  // Listeners
  private ActionBar.TabListener actionBarTabListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Get ViewPager
    mViewPager = (ViewPager) findViewById(R.id.viewPager);

    // Configure ActionBar
    actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Set ViewPager listeners
    mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

      @Override
      public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
      }

      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {

      }

      @Override
      public void onPageScrollStateChanged(int arg0) {

      }
    });

    // Initialize TabListener
    actionBarTabListener = new ActioBarTabListener();

    // Add navigation tabs to action bar
    addTabs();

    wifiControlApp = (WifiControlApp) getApplication();
    connectWithHost();

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    resources = getResources();

  }

  @Override
  protected void onResume() {
    super.onResume();

    if (wifiControlReceiver == null) {
      wifiControlReceiver = new WifiControlReceiver();
    }

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(WifiControlApp.ACTION_CONNECTING_TO_HOST);
    registerReceiver(wifiControlReceiver, intentFilter);
  }

  private void connectWithHost() {
    startProgressDialog(getString(R.string.msg_connecting));
    startService(new Intent(this, ConnectionService.class));
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(wifiControlReceiver);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.menu_settings:
      Intent preferencesActivity = new Intent(this, PreferencesActivity.class);
      startActivity(preferencesActivity);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  private void addTabs() {

    // Create fragments
    List<Fragment> fragments = new ArrayList<Fragment>();
    fragments.add(Fragment.instantiate(this, PlayerFragment.class.getName()));
    fragments.add(Fragment.instantiate(this, PlaylistFragment.class.getName()));
    fragments
        .add(Fragment.instantiate(this, EqualizerFragment.class.getName()));
    mPageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
    mViewPager.setAdapter(mPageAdapter);

    actionBar.addTab(actionBar.newTab().setText(R.string.tab_player)
        .setTabListener(actionBarTabListener));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_playlist)
        .setTabListener(actionBarTabListener));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_equalizer)
        .setTabListener(actionBarTabListener));
  }

  // -------------------------- UI Managers --------------------------//

  public void startProgressDialog(String message) {
    workingDialog = ProgressDialog.show(MainActivity.this, "", message, true);
  }

  public void stopProgressDialog() {
    workingDialog.dismiss();
  }

  public void showToast(String message) {
    Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
    toast.show();
  }

  // TabListener
  private class ActioBarTabListener implements ActionBar.TabListener {

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
      mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
      // TODO Auto-generated method stub

    }
  }

  // Broadcast Reveiver
  private class WifiControlReceiver extends BroadcastReceiver {

    private static final String TAG = "WifiControlReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(WifiControlApp.ACTION_CONNECTING_TO_HOST)) {
        stopProgressDialog();
        if (intent.getBooleanExtra(getString(R.string.key_connected), false)) {
          showToast(getString(R.string.msg_connected));
          try {
            MessageListener messageListener = new MessageListener(
                wifiControlApp.getHostConnection().getInputStream());
            messageListener.execute();

            MessagingService.getInstance().setSocket(
                wifiControlApp.getHostConnection());
          } catch (IOException e) {
            Log.d(TAG, e.getMessage());
          }
        } else {
          Log.d(TAG, "Not connected");
          showToast(getString(R.string.msg_conntimeout));
        }
      }
    }

  }
}
