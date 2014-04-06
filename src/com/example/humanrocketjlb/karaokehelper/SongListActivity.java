package com.example.humanrocketjlb.karaokehelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

public class SongListActivity extends ListActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener
{
    private SongListAdapter adapter;
    private List<SongRecord> songList;
    private static String TAG = "KaraokeHelper";
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);

	SongListParser parser = new SongListParser();

	try
	{
	    InputStream inputStream = getResources().openRawResource(R.raw.data);
	    songList = parser.parse(inputStream);
	    inputStream.close();
	}
	catch (XmlPullParserException parseExc)
	{
	    Log.i(TAG, "Failed to parse");
	}
	catch (IOException ioExc)
	{
	    Log.i(TAG, "Failed to read IO");
	}

	adapter = new SongListAdapter(getApplicationContext());
	adapter.add(songList);
	adapter.sortByArtist();
	getListView().setAdapter(adapter);
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
	default:
	    return false;
	}
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
	Log.i(TAG, "Got submit " + query);
	mSearchView.clearFocus();
	return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
	Log.i(TAG, "Got change " + newText);
	return false;
    }

    @Override
    public boolean onClose()
    {
	Log.i(TAG, "Called close");
	return false;
    }

}
