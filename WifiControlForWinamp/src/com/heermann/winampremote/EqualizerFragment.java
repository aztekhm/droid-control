package com.heermann.winampremote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class EqualizerFragment extends Fragment {

  private class EqualizerChangeListener implements OnSeekBarChangeListener {
    private int band;

    public EqualizerChangeListener(int band) {
      this.band = band;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
        boolean fromUser) {
      if (fromUser)
        messagingService.send(43, band, 63 - progress);
    }

  }

  public static SeekBar[] equalizer;
  private EqualizerChangeListener[] eqListeners;
  private MessagingService messagingService;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.equalizer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    equalizer = new SeekBar[10];
    equalizer[0] = (SeekBar) getView().findViewById(R.id.eq0);
    equalizer[1] = (SeekBar) getView().findViewById(R.id.eq1);
    equalizer[2] = (SeekBar) getView().findViewById(R.id.eq2);
    equalizer[3] = (SeekBar) getView().findViewById(R.id.eq3);
    equalizer[4] = (SeekBar) getView().findViewById(R.id.eq4);
    equalizer[5] = (SeekBar) getView().findViewById(R.id.eq5);
    equalizer[6] = (SeekBar) getView().findViewById(R.id.eq6);
    equalizer[7] = (SeekBar) getView().findViewById(R.id.eq7);
    equalizer[8] = (SeekBar) getView().findViewById(R.id.eq8);
    equalizer[9] = (SeekBar) getView().findViewById(R.id.eq9);

    eqListeners = new EqualizerChangeListener[equalizer.length];
    for (int i = 0; i < eqListeners.length; i++) {
      eqListeners[i] = new EqualizerChangeListener(i);
      equalizer[i].setOnSeekBarChangeListener(eqListeners[i]);
    }

    messagingService = MessagingService.getInstance();
  }

}
