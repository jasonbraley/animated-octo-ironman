package com.example.humanrocketjlb.karaokehelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class DownloaderTask extends AsyncTask<String, Void, List<SongRecord>>
{
    private SongListActivity mParentActivity;
    private Context mApplicationContext;
    private static String TAG = "KaraokeDownloaderTask";
    private HttpURLConnection mHttpUrl;
    private Boolean mForce = false;

    // Constructor
    public DownloaderTask(SongListActivity parentActivity, Boolean force)
    {
	super();

	mParentActivity = parentActivity;
	mApplicationContext = parentActivity.getApplicationContext();
	mForce = force;
    }

    @Override
    protected List<SongRecord> doInBackground(String... urlParameters)
    {
	Log.i(TAG, "In doInBackground()");
	List<SongRecord> myList = null;
	BufferedReader in = null;
	FileOutputStream out = null;

	if (urlParameters != null)
	{
	    String fileName = Uri.parse(urlParameters[0]).getLastPathSegment();
	    File myFile = mParentActivity.getFileStreamPath(fileName);
	    Date today = new Date();
	    long todayMsecs = today.getTime();
	    long SEVENDAYS = 7 * 24 * 60 * 60 * 1000;

	    ConnectivityManager cm = (ConnectivityManager) mParentActivity
		    .getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    Boolean connected = true;
	    if (netInfo == null || netInfo.isConnected() == false)
	    {
		connected = false;
	    }

	    // if we're not connect but have a file, just use it.
	    if (connected == false && myFile.exists())
	    {
		// nothing
	    }
	    // if we're forcing a refresh or there's no file or the file is too
	    // old, download the file.
	    else if (mForce == true || !myFile.exists() || (todayMsecs - myFile.lastModified()) > SEVENDAYS)
	    {
		try
		{
		    String line;
		    URL url = new URL(urlParameters[0]);

		    mHttpUrl = (HttpURLConnection) url.openConnection();
		    out = mParentActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
		    in = new BufferedReader(new InputStreamReader(mHttpUrl.getInputStream()));

		    while ((line = in.readLine()) != null)
		    {
			out.write(line.getBytes());
		    }

		    in.close();
		    out.close();
		}
		catch (MalformedURLException e)
		{
		    Log.i(TAG, "Caught malformed URL");
		}
		catch (IOException e)
		{
		    Log.i(TAG, "Caught malformed IOException");
		}
		finally
		{
		    try
		    {
			if (in != null)
			{
			    in.close();
			}
			if (out != null)
			{
			    out.close();
			}
		    }
		    catch (IOException e)
		    {
			Log.i(TAG, "Cannot close file descriptors");
		    }
		}
	    }

	    SongListParser parser = new SongListParser();
	    InputStream is = null;
	    try
	    {
		is = mParentActivity.openFileInput(fileName);
		myList = parser.parse(is);
		is.close();
	    }
	    catch (XmlPullParserException e)
	    {
		Log.i(TAG, "Caught XMLPullParserException");
	    }
	    catch (FileNotFoundException e)
	    {
		Log.i(TAG, "Caught filenotfound");
	    }
	    catch (IOException e)
	    {
		Log.i(TAG, "Caught ioexception");
	    }
	    finally
	    {
		try
		{
		    if (is != null)
		    {
			is.close();
		    }
		}
		catch (IOException e)
		{
		    Log.i(TAG, "Couldn't close is");
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
	    mParentActivity.setAdapter(result);
	}

    }
}
