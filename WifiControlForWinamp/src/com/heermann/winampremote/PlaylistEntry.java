package com.heermann.winampremote;

public class PlaylistEntry {

	public final String songTitle;
	public final String artist;
	public final boolean isPlaying;
	
	public PlaylistEntry(String songTitle, String artist, boolean isPlaying) {
		this.songTitle = songTitle;
		this.artist = artist;
		this.isPlaying = isPlaying;
	}
}
