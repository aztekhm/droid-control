package com.heermann.winampremote;

import java.util.ArrayList;
import java.util.List;

import com.heermann.winampremote.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class PlaylistActivity extends ListActivity {
	
	public static List<PlaylistEntry> entrys;
	private static PlaylistAdapter adapter;
	private MessagingService messagingService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		
		entrys = new ArrayList<PlaylistEntry>();
		adapter = new PlaylistAdapter(this, android.R.id.list, entrys);
		setListAdapter(adapter);
		
		messagingService = MessagingService.getInstance();
	}
	
	public static void addEntry(PlaylistEntry entry) {
		entrys.add(entry);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int track, long id) {
	  messagingService.send(4, track);
	}
}
