package com.example.humanrocketjlb.karaokehelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter implements Filterable
{
    private static String TAG = "SongListAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<SongRecord> mOriginalData;
    private List<SongRecord> mData;

    public SongListAdapter(Context context)
    {
	mContext = context;
	mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
	if (mData != null)
	{
	    return mData.size();
	}
	else
	{
	    return 0;
	}
    }

    @Override
    public SongRecord getItem(int position)
    {
	if (mData != null)
	{
	    return mData.get(position);
	}
	else
	{
	    return null;
	}

    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    public void add(List<SongRecord> list)
    {
	mOriginalData = list;
	mData = mOriginalData;
	notifyDataSetChanged();
    }

    public void sortByTitle()
    {
	Collections.sort(mOriginalData, new Comparator<SongRecord>()
	{
	    @Override
	    public int compare(SongRecord o1, SongRecord o2)
	    {
		return o1.getTitle().compareTo(o2.getTitle());
	    }
	});
	mData = mOriginalData;
    }

    public void sortByArtist()
    {
	Collections.sort(mOriginalData, new Comparator<SongRecord>()
	{
	    @Override
	    public int compare(SongRecord o1, SongRecord o2)
	    {
		int temp = 0;
		try
		{
		    temp = o1.getArtist().compareTo(o2.getArtist());
		}
		catch (NullPointerException myExc)
		{
		    Log.i(TAG, "Caught a null exception");
		}

		return temp;
	    }
	});
	mData = mOriginalData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	// Get the current ToDoItem
	final SongRecord record = (SongRecord) getItem(position);

	// Inflate the View for this ToDoItem
	// from todo_item.xml.
	RelativeLayout itemLayout = null;

	if (convertView != null)
	{
	    itemLayout = (RelativeLayout) convertView;
	}
	else
	{
	    itemLayout = (RelativeLayout) mInflater.inflate(R.layout.song, parent, false);
	}

	// Fill in specific data
	// Remember that the data that goes in this View
	// corresponds to the user interface elements defined
	// in the layout file

	// Display Title in TextView
	final TextView titleView = (TextView) itemLayout.findViewById(R.id.title);
	titleView.setText(record.getTitle());

	final TextView artistView = (TextView) itemLayout.findViewById(R.id.artist);
	artistView.setText(record.getArtist());

	final TextView idView = (TextView) itemLayout.findViewById(R.id.id);
	idView.setText(record.getId());

	// Set up Status CheckBox

	// Return the View you just created
	return itemLayout;
    }

    @Override
    public Filter getFilter()
    {
	Filter myFilter = new Filter()
	{
	    @Override
	    protected FilterResults performFiltering(CharSequence constraint)
	    {
		FilterResults filterResults = new FilterResults();
		if (constraint != null && constraint.length() > 0)
		{
		    int count = mOriginalData.size();
		    List<SongRecord> filt = new ArrayList<SongRecord>(count);
		    List<SongRecord> lItems = mOriginalData;
		    String lowercaseConstraint = constraint.toString().toLowerCase();
		   
		    for (int i = 0, l = lItems.size(); i < l; i++)
		    {
			SongRecord m = lItems.get(i);
			if (m.getTitle().toLowerCase().contains(lowercaseConstraint))
			{
			    filt.add(m);
			}
			else if (m.getArtist().toLowerCase().contains(lowercaseConstraint))
			{
			    filt.add(m);
			}
		    }
		    filterResults.count = filt.size();
		    filterResults.values = filt;
		}
		else
		{
		    filterResults.values = mOriginalData;
		    filterResults.count = mOriginalData.size();
		}
		return filterResults;
	    }

	    @Override
	    protected void publishResults(CharSequence contraint, FilterResults results)
	    {
		if (results != null && results.count > 0)
		{
		    Log.i(TAG, "Publishing results: " + results.count );
		    try
		    {
			mData = (ArrayList<SongRecord>)results.values;
		    }
		    catch( ClassCastException exc)
		    {
			Log.i(TAG, "Failed to cast correctly");
		    }
		    
		    notifyDataSetChanged();
		}
		else
		{
		    Log.i(TAG, "Doing a clear" );
		    mData.clear();
		    notifyDataSetChanged();
		}
	    }
	};
	return myFilter;
    }

}
