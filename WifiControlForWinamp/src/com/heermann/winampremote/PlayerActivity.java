package com.heermann.winampremote;

import com.heermann.winampremote.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlayerActivity extends Activity {

  private OnSeekBarChangeListener volumeChangeListener = new OnSeekBarChangeListener() {
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
        boolean fromUser) {
      if (fromUser)
        messagingService.send(30, progress);
    }
  };

  private OnSeekBarChangeListener progressSongChangeListener = new OnSeekBarChangeListener() {
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
        boolean fromUser) {
      if (fromUser)
        messagingService.send(28, progress * 1000);
    }
  };

  public static ToggleButton toggleRepeat;
  public static ToggleButton toggleShuffle;
  public static ToggleButton toggleMute;
  public static SeekBar volumeControl;
  public static TextView artistTitle;
  public static TextView songTitle;
  public static SeekBar progressSong;
  public static TextView bitrate;
  public static TextView trackLength;
  public static TextView frecuency;

  private MessagingService messagingService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.player);

    getViews();
    volumeControl.setOnSeekBarChangeListener(volumeChangeListener);
    progressSong.setOnSeekBarChangeListener(progressSongChangeListener);

    messagingService = new MessagingService((WifiControlApp) getApplication());
  }

  private void getViews() {
    toggleRepeat = (ToggleButton) findViewById(R.id.toggle_repeat);
    toggleShuffle = (ToggleButton) findViewById(R.id.toggle_shuffle);
    toggleMute = (ToggleButton) findViewById(R.id.toggle_mute);
    volumeControl = (SeekBar) findViewById(R.id.volume_control);
    artistTitle = (TextView) findViewById(R.id.artist_title);
    songTitle = (TextView) findViewById(R.id.song_title);
    progressSong = (SeekBar) findViewById(R.id.progress_song);
    bitrate = (TextView) findViewById(R.id.bitrate);
    trackLength = (TextView) findViewById(R.id.track_length);
    frecuency = (TextView) findViewById(R.id.frecuency);
  }

  // Actions
  public void toggleRepeat(View v) {
    messagingService.send(15);
  }

  public void toggleShuffle(View v) {
    messagingService.send(16);
  }

  public void toggleMute(View v) {
    messagingService.send(32);
  }

  public void previous(View v) {
    messagingService.send(2);
  }

  public void play(View v) {
    messagingService.send(4);
  }

  public void pause(View v) {
    messagingService.send(5);
  }

  public void stop(View v) {
    messagingService.send(6);
  }

  public void next(View v) {
    messagingService.send(3);
  }
}
