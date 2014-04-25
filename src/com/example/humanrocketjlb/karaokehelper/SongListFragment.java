package com.example.humanrocketjlb.karaokehelper;

import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

public class SongListFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    private SongListAdapter mAdapter;
    private static String TAG = "KaraokeHelperSongFragment";
    private String myDataUrl = "http://www.cs.uml.edu/~jbraley/data.xml";
    private ListView mListView = null;
    private ProgressBar mProgressBar = null;
    private SearchView mSearchView;

    public ListView getListView()
    {
	return mListView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
	mAdapter = new SongListAdapter(getActivity().getApplicationContext());

	mListView = (ListView) rootView.findViewById(R.id.object_list_view);
	mListView.setAdapter(mAdapter);

	mProgressBar = (ProgressBar) rootView.findViewById(R.id.object_progressBar);
	mProgressBar.setVisibility(View.VISIBLE);

	setHasOptionsMenu(true);

	new DownloaderTask(getActivity(), this, false).execute(myDataUrl);

	return rootView;
    }

    protected void setAdapter(List<SongRecord> myList)
    {
	mAdapter.clear();
	mAdapter.add(myList);
	mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	inflater.inflate(R.menu.song_list, menu);
	
	SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
	mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
	mSearchView.setOnQueryTextListener(this);
	mSearchView.setOnCloseListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.action_settings:
	    Toast.makeText(getActivity().getApplicationContext(), "Nothing to see here", Toast.LENGTH_SHORT).show();
	    return true;

	case R.id.action_search:
	    Toast.makeText(getActivity().getApplicationContext(), "Something here soon", Toast.LENGTH_SHORT).show();
	    return true;

	case R.id.action_refresh:
	    mAdapter.clear();
	    mProgressBar.setVisibility(View.VISIBLE);
	    new DownloaderTask(getActivity(), this, true).execute(myDataUrl);
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
