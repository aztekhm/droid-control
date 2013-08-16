package com.heermann.winampremote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.util.Log;

public class MessagingService {
	
	private class ConnectTask extends AsyncTask<Object, Void, Socket> {
		
		private boolean isConnected;

		@Override
		protected void onPreExecute() {
			mainActivity.startProgressDialog(MainActivity.resources.getString(R.string.msg_connecting));
		}
		
		@Override
		protected void onPostExecute(Socket socket) {
			mainActivity.stopProgressDialog();
			if (isConnected) {
				mainActivity.showToast(MainActivity.resources.getString(R.string.msg_connected));
				messageListener = new MessageListener(inputStream);
				messageListener.execute();
				
			} else {
				mainActivity.showToast(MainActivity.resources.getString(R.string.msg_conntimeout));
			}
		}
		
		@Override
		protected Socket doInBackground(Object... params) {
			socket = new Socket();
			try {
				socket.connect(new InetSocketAddress((String) params[0], (Integer) params[1]), (Integer) params[2]);
				outputStream = socket.getOutputStream();
				inputStream = socket.getInputStream();
				isConnected = true;

			} catch (IOException e) {
				Log.d("", "Timeout," + e.getMessage());
				isConnected = false;
			}
			return null;
		}
	}
	
	private class SendMessageTask implements Runnable {
		
		private String message;
		
		public SendMessageTask(String message) {
			this.message = message + "\0";
		}
		@Override
		public void run() {
			try {
				outputStream.write(message.getBytes());
				
			} catch (IOException e) {
				Log.d("", "Cant send message," + e.getMessage());
			}
		}
	}
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private MainActivity mainActivity;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private MessageListener messageListener;
	
	public MessagingService(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	public void start(String ip, int port, int timeout) {
		new ConnectTask().execute(ip, port, timeout);
	}
	
	public void stop() {
		if (messageListener != null)
			messageListener.shutdown();
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				Log.d("", "Cant close socket");
			}
		}
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
}
