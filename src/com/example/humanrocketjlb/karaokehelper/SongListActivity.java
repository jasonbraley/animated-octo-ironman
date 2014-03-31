package com.example.humanrocketjlb.karaokehelper;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

public class SongListActivity extends ListActivity
{
    private SongListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	
	getListView().setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.song_list, menu);
	return true;
    }

}
