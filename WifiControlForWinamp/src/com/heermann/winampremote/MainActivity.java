package com.heermann.winampremote;

import java.util.Map;

import com.heermann.winampremote.R;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {

	///---------------------- UI -------------------///
	private TabHost tabHost;
	private ProgressDialog workingDialog;
	
	///---------------------- OTHER -------------------///
	public static Resources resources;
	public static MessagingService msgService;
	public static Map<String, ?> preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		msgService = new MessagingService(this);
		resources = getResources();
		addTabs();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		preferences = PreferenceManager.getDefaultSharedPreferences(this).getAll();
		msgService.start(
				(String)preferences.get(resources.getString(R.string.pref_ip)), 
				Integer.valueOf((String)preferences.get(resources.getString(R.string.pref_port))), 
				Integer.valueOf((String)preferences.get(resources.getString(R.string.pref_timeout))));
//		msgService.start("192.168.1.65", 670, 3000);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		msgService.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				Intent preferencesActivity = new Intent(this, PreferencesActivity.class);
				startActivity(preferencesActivity);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void addTabs() {
		tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.tab_player)).
				//setIndicator(resources.getString(R.string.tab_player), resources.getDrawable(android.R.drawable.ic_menu_agenda)).
				setIndicator(resources.getString(R.string.tab_player)).
				setContent(new Intent(this, PlayerActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.tab_playlist)).
				//setIndicator(resources.getString(R.string.tab_playlist), resources.getDrawable(android.R.drawable.ic_menu_agenda)).
				setIndicator(resources.getString(R.string.tab_playlist)).
				setContent(new Intent(this, PlaylistActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.tab_equalizer)).
				//setIndicator(resources.getString(R.string.tab_equalizer), resources.getDrawable(android.R.drawable.ic_menu_agenda)).
				setIndicator(resources.getString(R.string.tab_equalizer)).
				setContent(new Intent(this, EqualizerActivity.class)));
		
		tabHost.setCurrentTab(0);
	}
	
	//-------------------------- UI Managers --------------------------//
	
	public void startProgressDialog(String message) {
		workingDialog = ProgressDialog.show(MainActivity.this, "", message, true);
	}
	
	public void stopProgressDialog() {
		workingDialog.dismiss();
	}
	
	public void showToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}
}

