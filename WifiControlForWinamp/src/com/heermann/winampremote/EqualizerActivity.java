package com.heermann.winampremote;

import com.heermann.winampremote.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class EqualizerActivity extends Activity {
	
	private class EqualizerChangeListener implements OnSeekBarChangeListener {
		private int band;
		public EqualizerChangeListener(int band) {
			this.band = band;
		}
		@Override public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override public void onStopTrackingTouch(SeekBar seekBar) {}
		@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (fromUser) MainActivity.msgService.send(43, band, 63-progress);
		}
		
	}
	
	public static SeekBar[] equalizer;
	private EqualizerChangeListener[] eqListeners;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equalizer);
		
		equalizer = new SeekBar[10];
		equalizer[0] = (SeekBar)findViewById(R.id.eq0);
		equalizer[1] = (SeekBar)findViewById(R.id.eq1);
		equalizer[2] = (SeekBar)findViewById(R.id.eq2);
		equalizer[3] = (SeekBar)findViewById(R.id.eq3);
		equalizer[4] = (SeekBar)findViewById(R.id.eq4);
		equalizer[5] = (SeekBar)findViewById(R.id.eq5);
		equalizer[6] = (SeekBar)findViewById(R.id.eq6);
		equalizer[7] = (SeekBar)findViewById(R.id.eq7);
		equalizer[8] = (SeekBar)findViewById(R.id.eq8);
		equalizer[9] = (SeekBar)findViewById(R.id.eq9);

		eqListeners = new EqualizerChangeListener[equalizer.length];
		for (int i=0; i<eqListeners.length; i++) {
			eqListeners[i] = new EqualizerChangeListener(i);
			equalizer[i].setOnSeekBarChangeListener(eqListeners[i]);
		}
	}
}
