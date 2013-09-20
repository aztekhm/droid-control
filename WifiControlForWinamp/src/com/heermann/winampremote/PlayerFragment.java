package com.heermann.winampremote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PlayerFragment extends Fragment implements OnClickListener {

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
  private ImageButton mPreviousButton;
  private ImageButton mPlayButton;
  private ImageButton mPauseButton;
  private ImageButton mStopButton;
  private ImageButton mNextButton;

  private MessagingService messagingService;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.player, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getViews();
    volumeControl.setOnSeekBarChangeListener(volumeChangeListener);
    progressSong.setOnSeekBarChangeListener(progressSongChangeListener);

    messagingService = MessagingService.getInstance();

  }

  private void getViews() {
    toggleRepeat = (ToggleButton) getView().findViewById(R.id.toggle_repeat);
    toggleShuffle = (ToggleButton) getView().findViewById(R.id.toggle_shuffle);
    toggleMute = (ToggleButton) getView().findViewById(R.id.toggle_mute);
    volumeControl = (SeekBar) getView().findViewById(R.id.volume_control);
    artistTitle = (TextView) getView().findViewById(R.id.artist_title);
    songTitle = (TextView) getView().findViewById(R.id.song_title);
    progressSong = (SeekBar) getView().findViewById(R.id.progress_song);
    bitrate = (TextView) getView().findViewById(R.id.bitrate);
    trackLength = (TextView) getView().findViewById(R.id.track_length);
    frecuency = (TextView) getView().findViewById(R.id.frecuency);

    mPreviousButton = (ImageButton) getView().findViewById(R.id.previous);
    mPlayButton = (ImageButton) getView().findViewById(R.id.play);
    mPauseButton = (ImageButton) getView().findViewById(R.id.pause);
    mStopButton = (ImageButton) getView().findViewById(R.id.stop);
    mNextButton = (ImageButton) getView().findViewById(R.id.next);

    toggleMute.setOnClickListener(this);
    toggleRepeat.setOnClickListener(this);
    toggleShuffle.setOnClickListener(this);
    mPreviousButton.setOnClickListener(this);
    mPlayButton.setOnClickListener(this);
    mPauseButton.setOnClickListener(this);
    mStopButton.setOnClickListener(this);
    mNextButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
    case R.id.previous:
      messagingService.send(2);
      break;
    case R.id.play:
      messagingService.send(4);
      break;
    case R.id.pause:
      messagingService.send(5);
      break;
    case R.id.stop:
      messagingService.send(6);
      break;
    case R.id.next:
      messagingService.send(3);
      break;
    case R.id.toggle_repeat:
      messagingService.send(15);
      break;
    case R.id.toggle_mute:
      messagingService.send(32);
      break;
    case R.id.toggle_shuffle:
      messagingService.send(16);
      break;

    default:
      break;
    }
  }
}
