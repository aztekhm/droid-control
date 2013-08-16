package com.heermann.winampremote;

import java.net.Socket;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class WifiControlApp extends Application implements
    OnSharedPreferenceChangeListener {
  
  //Actions for broadcast messages
  public final static String ACTION_CONNECTING_TO_HOST = "com.heermann.winampremote.action.CONNECTING_TO_HOST";
  private Socket hostConnection;
  private boolean isConnected;
  private SharedPreferences sharedPreferences;
  private String ip;
  private int port;
  private int timeout;

  @Override
  public void onCreate() {
    super.onCreate();

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    asignValuesFromPreferences();
  }

  private void asignValuesFromPreferences() {
    ip = sharedPreferences.getString(getString(R.string.pref_ip),
        getString(R.string.default_ip));
    port = Integer.parseInt(sharedPreferences.getString(
        getString(R.string.pref_port), getString(R.string.default_port)));
    timeout = Integer.parseInt(sharedPreferences.getString(
        getString(R.string.pref_timeout), getString(R.string.default_timeout)));
  }

  public Socket getHostConnection() {
    return hostConnection;
  }

  public void setHostConnection(Socket hostConnection) {
    this.hostConnection = hostConnection;
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void setConnected(boolean isConnected) {
    this.isConnected = isConnected;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public int getTimeout() {
    return timeout;
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
      String key) {
    this.sharedPreferences = sharedPreferences;
    asignValuesFromPreferences();
  }

}
