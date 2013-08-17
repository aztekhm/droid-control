package com.heermann.winampremote;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class MessagingService{

  private static final String TAG = "MessagingService";
  private Socket socket;
  private ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static MessagingService messagingService;

  private class SendMessageTask implements Runnable {

    private String message;

    public SendMessageTask(String message) {
      this.message = message + "\0";
    }

    @Override
    public void run() {
      try {
        socket.getOutputStream().write(message.getBytes());

      } catch (IOException e) {
        Log.d(TAG, "Cant send message," + e.getMessage());
      }
    }
  }

  private MessagingService()
  {
    
  }
  
  public void send(int command, String param1, String param2) {
    if (socket != null && socket.isConnected()) {
      String message = "" + command
          + ((param1 != null && !"".equals(param1)) ? ("|" + param1) : "")
          + ((param2 != null && !"".equals(param2)) ? ("|" + param2) : "");

      executorService.execute(new SendMessageTask(message));
    }
  }

  public void send(int command) {
    send(command, null, null);
  }

  public void send(int command, int param) {
    send(command, "" + param, null);
  }

  public void send(int command, int param1, int param2) {
    send(command, "" + param1, "" + param2);
  }

  public void send(int command, String param) {
    send(command, param, null);
  }
  
  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public static MessagingService getInstance() {
    if(messagingService == null){
      messagingService = new MessagingService();
    }
    
    return messagingService;
  }
  
}
