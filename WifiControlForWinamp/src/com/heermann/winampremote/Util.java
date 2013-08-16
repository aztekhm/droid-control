package com.heermann.winampremote;

import java.util.Locale;

public class Util {
	public static String secondsToMinutes(int seconds) {
		int minutes = seconds / 60;
		seconds -= (minutes * 60);
		return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
	}
}
