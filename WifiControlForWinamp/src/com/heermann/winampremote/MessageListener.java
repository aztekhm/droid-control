package com.heermann.winampremote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.AsyncTask;
import android.util.Log;

public class MessageListener extends AsyncTask<Void, String, Void> {
	
	private InputStream input;
	private boolean active = true;
	
	public MessageListener(InputStream inputStream) {
		input = inputStream;
	}
	
	public void start() {
		execute();
	}
	
	public void shutdown() {
		this.active = false;
	}
	
	@Override
	protected Void doInBackground(Void... args) {
		if (input != null) {
			try {
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				while (true && active) {
					int _byte = input.read();
					buffer.write(_byte);
					if (_byte == '\n') {
						String line = new String(buffer.toByteArray(), "UTF-8");
						publishProgress(line);
						buffer.reset();
					}
				}
				
			} catch (IOException e) {
				Log.d("", "Input stream closed");
			}
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... messages) {
		String mainAndEqDataPrefix = "syncmaineq";
		String playlistPrefix = "syncplaylist";
		
		int indexOfSep = messages[0].indexOf('_');
		if (indexOfSep != -1) {
			String prefix = messages[0].substring(0, messages[0].indexOf('_'));
			String content = messages[0].substring(messages[0].indexOf('_') + 1);
			
			//Log.d("", prefix + "=>" + content);
			if (prefix.equals(mainAndEqDataPrefix)) {
				///------------------------ PLAYER / EQUALIZER ------------------------///
				String[] values = content.split("[|]");
				int trackLength = Integer.valueOf(values[0]);
				int trackProgress = Integer.valueOf(values[1]) / 1000;
				PlayerActivity.trackLength.setText(Util.secondsToMinutes(trackProgress) + "/" + Util.secondsToMinutes(trackLength));
				PlayerActivity.progressSong.setMax(trackLength);
				PlayerActivity.progressSong.setProgress(trackProgress);
				try {
					int volume = Integer.valueOf(values[2]);
					PlayerActivity.volumeControl.setProgress(volume);
					PlayerActivity.toggleMute.setChecked(volume == 0);
					if (EqualizerActivity.equalizer != null) {
						int eqMax = 63;
						EqualizerActivity.equalizer[0].setProgress(eqMax - Integer.valueOf(values[3]));
						EqualizerActivity.equalizer[1].setProgress(eqMax - Integer.valueOf(values[4]));
						EqualizerActivity.equalizer[2].setProgress(eqMax - Integer.valueOf(values[5]));
						EqualizerActivity.equalizer[3].setProgress(eqMax - Integer.valueOf(values[6]));
						EqualizerActivity.equalizer[4].setProgress(eqMax - Integer.valueOf(values[7]));
						EqualizerActivity.equalizer[5].setProgress(eqMax - Integer.valueOf(values[8]));
						EqualizerActivity.equalizer[6].setProgress(eqMax - Integer.valueOf(values[9]));
						EqualizerActivity.equalizer[7].setProgress(eqMax - Integer.valueOf(values[10]));
						EqualizerActivity.equalizer[8].setProgress(eqMax - Integer.valueOf(values[11]));
						EqualizerActivity.equalizer[9].setProgress(eqMax - Integer.valueOf(values[12]));
						//EqualizerActivity.equalizer[10].setProgress(Integer.valueOf(values[13]));	//preamp value
					}
					PlayerActivity.frecuency.setText(values[14] + "KHz");
					PlayerActivity.bitrate.setText(values[15] + "Kbps");
					//PlayerActivity.channels.setText(values[16] + "CH");	//channels
					
					PlayerActivity.artistTitle.setText(values[17]);
					PlayerActivity.songTitle.setText(values[18]);
					
					PlayerActivity.toggleRepeat.setChecked(values[19].equals("1"));
					PlayerActivity.toggleShuffle.setChecked(values[20].equals("1"));
					
				} catch (NumberFormatException e) {
					Log.d("", "Incorrect value format");
				}
				
			} else if (prefix.equals(playlistPrefix)) {
				///------------------------ PLAYLIST ------------------------///
				if (PlaylistActivity.entrys != null) {
					PlaylistActivity.entrys.clear();
					
					String[] entrys = content.split("[|]{2}");
					int playingTrack = Integer.valueOf(entrys[0]) + 1;
					for (int i=1; i<entrys.length; i++) {
						String[] entry = entrys[i].split("[|]");
						if(entry.length == 2){
						  PlaylistActivity.addEntry(new PlaylistEntry(entry[0], entry[1], playingTrack == i));
						}
					}
					
				}
			}
		}
	}
}
