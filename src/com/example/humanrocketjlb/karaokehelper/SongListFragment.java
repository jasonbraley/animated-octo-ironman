package com.example.humanrocketjlb.karaokehelper;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

public class SongListFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener
{
    private SongListAdapter mAdapter;
    private static String TAG = "KaraokeHelperSongFragment";
    private String myDataUrl = "http://www.cs.uml.edu/~jbraley/data.xml";
    private ListView mListView = null;
    private ProgressBar mProgressBar = null;

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
    public boolean onQueryTextSubmit(String query)
    {
	Log.i(TAG, "Got submit " + query);
	mAdapter.getFilter().filter(query);
	getActivity().getSearchView().clearFocus();
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
