package com.heermann.winampremote;

import java.io.IOException;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.heermann.winampremote.services.ConnectionService;

public class MainActivity extends TabActivity {

  // /---------------------- UI -------------------///
  private TabHost tabHost;
  private ProgressDialog workingDialog;

  // /---------------------- OTHER -------------------///
  public static MessagingService msgService;
  public static SharedPreferences preferences;
  public static Resources resources;

  private WifiControlApp wifiControlApp;

  // Receivers
  private WifiControlReceiver wifiControlReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    wifiControlApp = (WifiControlApp) getApplication();
    connectWithHost();

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    msgService = new MessagingService(this);
    resources = getResources();
    addTabs();
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
    tabHost = getTabHost();

    tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_player))
        .
        // setIndicator(resources.getString(R.string.tab_player),
        // resources.getDrawable(android.R.drawable.ic_menu_agenda)).
        setIndicator(getString(R.string.tab_player))
        .setContent(new Intent(this, PlayerActivity.class)));

    tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_playlist))
        .
        // setIndicator(resources.getString(R.string.tab_playlist),
        // resources.getDrawable(android.R.drawable.ic_menu_agenda)).
        setIndicator(getString(R.string.tab_playlist))
        .setContent(new Intent(this, PlaylistActivity.class)));

    tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_equalizer))
        .
        // setIndicator(resources.getString(R.string.tab_equalizer),
        // resources.getDrawable(android.R.drawable.ic_menu_agenda)).
        setIndicator(getString(R.string.tab_equalizer))
        .setContent(new Intent(this, EqualizerActivity.class)));

    tabHost.setCurrentTab(0);
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
