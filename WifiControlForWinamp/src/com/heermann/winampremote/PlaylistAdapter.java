package com.heermann.winampremote;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaylistAdapter extends ArrayAdapter<PlaylistEntry> {
	private Context ctx;
	private List<PlaylistEntry> entries;
	
	public PlaylistAdapter(Context context, int textViewResourceId, List<PlaylistEntry> objects) {
		super(context, textViewResourceId, objects);
		this.ctx = context;
		this.entries = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View entryView = inflater.inflate(R.layout.playlist_entry, parent, false);
		TextView artistView = (TextView)entryView.findViewById(R.id.plentry_artist);
		TextView titleView = (TextView)entryView.findViewById(R.id.plentry_title);
		
		PlaylistEntry entry = entries.get(position);
		artistView.setText(entry.artist);
		titleView.setText(entry.songTitle);
		
		if (entry.isPlaying) {
			artistView.setTextColor(Color.GREEN);
			titleView.setTextColor(Color.GREEN);
		}
		return entryView;
	}
	
}

