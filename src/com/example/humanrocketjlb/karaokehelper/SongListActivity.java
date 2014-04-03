package com.example.humanrocketjlb.karaokehelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class SongListActivity extends ListActivity
{
    private SongListAdapter adapter;
    private List<SongRecord> songList;
    
    private static String TAG = "KaraokeHelper";

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
