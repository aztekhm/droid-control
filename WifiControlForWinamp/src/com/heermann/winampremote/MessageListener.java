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
				PlayerFragment.trackLength.setText(Util.secondsToMinutes(trackProgress) + "/" + Util.secondsToMinutes(trackLength));
				PlayerFragment.progressSong.setMax(trackLength);
				PlayerFragment.progressSong.setProgress(trackProgress);
				try {
					int volume = Integer.valueOf(values[2]);
					PlayerFragment.volumeControl.setProgress(volume);
					PlayerFragment.toggleMute.setChecked(volume == 0);
					if (EqualizerFragment.equalizer != null) {
						int eqMax = 63;
						EqualizerFragment.equalizer[0].setProgress(eqMax - Integer.valueOf(values[3]));
						EqualizerFragment.equalizer[1].setProgress(eqMax - Integer.valueOf(values[4]));
						EqualizerFragment.equalizer[2].setProgress(eqMax - Integer.valueOf(values[5]));
						EqualizerFragment.equalizer[3].setProgress(eqMax - Integer.valueOf(values[6]));
						EqualizerFragment.equalizer[4].setProgress(eqMax - Integer.valueOf(values[7]));
						EqualizerFragment.equalizer[5].setProgress(eqMax - Integer.valueOf(values[8]));
						EqualizerFragment.equalizer[6].setProgress(eqMax - Integer.valueOf(values[9]));
						EqualizerFragment.equalizer[7].setProgress(eqMax - Integer.valueOf(values[10]));
						EqualizerFragment.equalizer[8].setProgress(eqMax - Integer.valueOf(values[11]));
						EqualizerFragment.equalizer[9].setProgress(eqMax - Integer.valueOf(values[12]));
						//EqualizerActivity.equalizer[10].setProgress(Integer.valueOf(values[13]));	//preamp value
					}
					PlayerFragment.frecuency.setText(values[14] + "KHz");
					PlayerFragment.bitrate.setText(values[15] + "Kbps");
					//PlayerActivity.channels.setText(values[16] + "CH");	//channels
					
					PlayerFragment.artistTitle.setText(values[17]);
					PlayerFragment.songTitle.setText(values[18]);
					
					PlayerFragment.toggleRepeat.setChecked(values[19].equals("1"));
					PlayerFragment.toggleShuffle.setChecked(values[20].equals("1"));
					
				} catch (NumberFormatException e) {
					Log.d("", "Incorrect value format");
				}
				
			} else if (prefix.equals(playlistPrefix)) {
				///------------------------ PLAYLIST ------------------------///
				if (PlaylistFragment.entrys != null) {
					PlaylistFragment.entrys.clear();
					
					String[] entrys = content.split("[|]{2}");
					int playingTrack = Integer.valueOf(entrys[0]) + 1;
					for (int i=1; i<entrys.length; i++) {
						String[] entry = entrys[i].split("[|]");
						if(entry.length == 2){
						  PlaylistFragment.addEntry(new PlaylistEntry(entry[0], entry[1], playingTrack == i));
						}
					}
					
				}
			}
		}
	}
}
