package com.heermann.winampremote.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.heermann.winampremote.R;
import com.heermann.winampremote.WifiControlApp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ConnectionService extends IntentService {
  private static final String TAG = "ConnectionService";

  public ConnectionService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    WifiControlApp wifiControlApp = (WifiControlApp) getApplication();
    Socket socket = new Socket();
    Log.d(
        TAG,
        "Conecting with " + wifiControlApp.getIp() + ", port "
            + wifiControlApp.getPort() + " whith a timeout of "
            + wifiControlApp.getTimeout());
    try {
      socket.connect(new InetSocketAddress(wifiControlApp.getIp(),
          wifiControlApp.getPort()), wifiControlApp.getTimeout());
      wifiControlApp.setHostConnection(socket);
      wifiControlApp.setConnected(true);
      Log.d(TAG, "Connected with host");

    } catch (IOException e) {
      Log.d(TAG, "Timeout," + e.getMessage());
    }
    Intent actionIntent = new Intent(WifiControlApp.ACTION_CONNECTING_TO_HOST)
        .putExtra(getString(R.string.key_connected), wifiControlApp.isConnected());
    sendBroadcast(actionIntent);
  }

}
