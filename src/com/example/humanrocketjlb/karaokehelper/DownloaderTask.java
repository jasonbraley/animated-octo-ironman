package com.example.humanrocketjlb.karaokehelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloaderTask extends AsyncTask<String, Void, List<SongRecord>>
{
    private SongListActivity mParentActivity;
    private Context mApplicationContext;
    private static String TAG = "KaraokeDownloaderTask";
    private HttpURLConnection mHttpUrl;

    // Constructor
    public DownloaderTask(SongListActivity parentActivity)
    {
	super();

	mParentActivity = parentActivity;
	mApplicationContext = parentActivity.getApplicationContext();
    }

    @Override
    protected List<SongRecord> doInBackground(String... urlParameters)
    {
	Log.i(TAG, "In doInBackground()");
	List<SongRecord> myList = null;

	if (urlParameters != null)
	{
	    try
	    {
		URL url = new URL(urlParameters[0]);
		mHttpUrl = (HttpURLConnection) url.openConnection();
		SongListParser parser = new SongListParser();
		myList = parser.parse(mHttpUrl.getInputStream());
	    }
	    catch (MalformedURLException e)
	    {
		Log.i(TAG, "Caught malformed URL");
	    }
	    catch (IOException e)
	    {
		Log.i(TAG, "Caught malformed IOException");
	    }
	    catch( XmlPullParserException e)
	    {
		Log.i(TAG, "Caught malformed PullParsere");		
	    }
	    finally
	    {
		if (null != mHttpUrl)
		{
		    mHttpUrl.disconnect();
		}
	    }
	}

	return myList;
    }

    @Override
    protected void onPostExecute(List<SongRecord> result)
    {
	super.onPostExecute(result);

	if (mParentActivity != null)
	{
	    mParentActivity.setAdapter( result );
	}

    }
}
