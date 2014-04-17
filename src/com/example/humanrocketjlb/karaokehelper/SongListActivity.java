package com.example.humanrocketjlb.karaokehelper;

import java.util.List;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

public class SongListActivity extends ListActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener
{
    private SongListAdapter adapter;
    private static String TAG = "KaraokeHelper";
    private SearchView mSearchView;
    private String myDataUrl = "http://www.cs.uml.edu/~jbraley/data.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	new DownloaderTask(this, false).execute(myDataUrl);

	adapter = new SongListAdapter(getApplicationContext());
    }
    
    protected void setAdapter( List<SongRecord> myList )
    {
	adapter.clear();
	adapter.add(myList);
	getListView().setFastScrollEnabled(false);
	getListView().setAdapter(adapter);
	getListView().setFastScrollEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.song_list, menu);

	SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	mSearchView.setOnQueryTextListener(this);
	mSearchView.setOnCloseListener(this);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.action_settings:
	    Toast.makeText(getApplicationContext(), "Nothing to see here", Toast.LENGTH_SHORT).show();
	    return true;
	case R.id.action_search:
	    Toast.makeText(getApplicationContext(), "Something here soon", Toast.LENGTH_SHORT).show();
	    return true;
	    
	case R.id.action_refresh:
	    adapter.clear();
	    new DownloaderTask(this, true).execute(myDataUrl);
	    return true;
	    
	default:
	    return false;
	}
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
	Log.i(TAG, "Got submit " + query);
	adapter.getFilter().filter(query);
	mSearchView.clearFocus();
	return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
	Log.i(TAG, "Got change " + newText);
	adapter.getFilter().filter(newText);
	return false;
    }

    @Override
    public boolean onClose()
    {
	Log.i(TAG, "Called close");
	adapter.getFilter().filter(null);
	return false;
    }

}
