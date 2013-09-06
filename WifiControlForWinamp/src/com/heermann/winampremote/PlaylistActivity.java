package com.heermann.winampremote;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PlaylistActivity extends Fragment {

  public static List<PlaylistEntry> entrys;
  private static PlaylistAdapter adapter;
  private MessagingService messagingService;
  private ListView listView;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.playlist, container, false);
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    listView = (ListView) getView().findViewById(R.id.playlist);
    messagingService = MessagingService.getInstance();

    entrys = new ArrayList<PlaylistEntry>();
    adapter = new PlaylistAdapter(getActivity(), android.R.id.list, entrys);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int track,
          long id) {
        messagingService.send(4, track);
      }
    });
  } 


  public static void addEntry(PlaylistEntry entry) {
    entrys.add(entry);
    adapter.notifyDataSetChanged();
  }

}
