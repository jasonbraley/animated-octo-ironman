package com.example.humanrocketjlb.karaokehelper;

import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

public class SongListActivity extends Activity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    private SongListAdapter mAdapter;
    private static String TAG = "KaraokeHelper";
    private SearchView mSearchView;
    private String myDataUrl = "http://www.cs.uml.edu/~jbraley/data.xml";
    private ListView mListView = null;
    private ProgressBar mProgressBar = null;

    public ListView getListView()
    {
	return mListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_list_view);

	mAdapter = new SongListAdapter(getApplicationContext());

	mListView = (ListView) findViewById(R.id.object_list_view);
	mListView.setAdapter(mAdapter);

	mProgressBar = (ProgressBar) findViewById(R.id.object_progressBar);
	mProgressBar.setVisibility(View.VISIBLE);

	new DownloaderTask(this, false).execute(myDataUrl);
    }

    protected void setAdapter(List<SongRecord> myList)
    {
	mAdapter.clear();
	mAdapter.add(myList);
	mProgressBar.setVisibility(View.INVISIBLE);
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
	    mAdapter.clear();
	    mProgressBar.setVisibility(View.VISIBLE);
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
	mAdapter.getFilter().filter(query);
	mSearchView.clearFocus();
	return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
	Log.i(TAG, "Got change " + newText);
	mAdapter.getFilter().filter(newText);
	return false;
    }

    @Override
    public boolean onClose()
    {
	Log.i(TAG, "Called close");
	mAdapter.getFilter().filter(null);
	return false;
    }

}
