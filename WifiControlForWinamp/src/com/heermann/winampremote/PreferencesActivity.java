package com.heermann.winampremote;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private SharedPreferences preferenceValues;
	private Preference preferenceIp;
	private Preference preferencePort;
	private Preference preferenceTimeout;
	private Resources resources;
	
	private String KEY_IP;
	private String KEY_PORT;
	private String KEY_TIMEOUT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		
		resources = getResources();
		preferenceValues = PreferenceManager.getDefaultSharedPreferences(this);
		
		KEY_IP = resources.getString(R.string.pref_ip);
		KEY_PORT = resources.getString(R.string.pref_port);
		KEY_TIMEOUT = resources.getString(R.string.pref_timeout);
		
		preferenceIp = findPreference(KEY_IP);
		preferencePort = findPreference(KEY_PORT);
		preferenceTimeout = findPreference(KEY_TIMEOUT);
		
		preferenceIp.setSummary(preferenceValues.getString(KEY_IP, ""));
		preferencePort.setSummary(preferenceValues.getString(KEY_PORT, ""));
		preferenceTimeout.setSummary(preferenceValues.getString(KEY_TIMEOUT, ""));
		
		preferenceValues.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
		if (key.equals(KEY_IP)) {
			preferenceIp.setSummary(preferences.getString(key, ""));
			
		} else if (key.equals(KEY_PORT)) {
			preferencePort.setSummary(preferences.getString(key, ""));
			
		} else if (key.equals(KEY_TIMEOUT)) {
			preferenceTimeout.setSummary(preferences.getString(key, ""));
			
		}
	}
}

