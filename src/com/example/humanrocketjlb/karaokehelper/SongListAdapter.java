package com.example.humanrocketjlb.karaokehelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter implements Filterable, SectionIndexer
{
    private static String TAG = "SongListAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<SongRecord> mOriginalData;
    private List<SongRecord> mData;
    private HashMap<String, Integer> alphabetIndexer;
    private String[] sections;

    static class SongRecordViewHolder
    {
	TextView artist;
	TextView title;
	TextView id;
    }

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
	sortByTitle();
	mData = mOriginalData;

	alphabetIndexer = new HashMap<String, Integer>();
	int size = mData.size();
	for (int x = 0; x < size; x++)
	{
	    String s = (String) mData.get(x).getTitle();
	    // Get the first character of the track
	    String ch = s.substring(0, 1);
	    // convert to uppercase otherwise lowercase a -z will be sorted
	    // after upper A-Z
	    ch = ch.toUpperCase();
	    if (!alphabetIndexer.containsKey(ch))
	    {
		alphabetIndexer.put(ch, x);
	    }
	}

	Set<String> sectionLetters = alphabetIndexer.keySet();
	// create a list from the set to sort
	ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
	Collections.sort(sectionList);
	sections = new String[sectionList.size()];
	sectionList.toArray(sections);
	notifyDataSetChanged();
    }

    public void sortByTitle()
    {
	Collections.sort(mOriginalData, new Comparator<SongRecord>()
	{
	    @Override
	    public int compare(SongRecord o1, SongRecord o2)
	    {
		int temp = 0;
		try
		{
		    temp = o1.getTitle().compareTo(o2.getTitle());
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
	Log.i(TAG, "Position: " + position);

	// Inflate the View for this ToDoItem
	// from todo_item.xml.
	SongRecordViewHolder holder;

	if (convertView != null)
	{
	    holder = (SongRecordViewHolder) convertView.getTag();
	}
	else
	{
	    convertView = (RelativeLayout) mInflater.inflate(R.layout.song, parent, false);
	    holder = new SongRecordViewHolder();
	    holder.title = (TextView) convertView.findViewById(R.id.title);
	    holder.artist = (TextView) convertView.findViewById(R.id.artist);
	    holder.id = (TextView) convertView.findViewById(R.id.id);
	    convertView.setTag(holder);
	}

	// Fill in specific data
	// Remember that the data that goes in this View
	// corresponds to the user interface elements defined
	// in the layout file

	// Get the current ToDoItem
	SongRecord record = (SongRecord) getItem(position);
	holder.title.setText(record.getTitle());
	holder.artist.setText(record.getArtist());
	holder.id.setText(record.getId());

	// Return the View you just created
	return convertView;
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
		    Log.i(TAG, "Publishing results: " + results.count);
		    try
		    {
			mData = (ArrayList<SongRecord>) results.values;
		    }
		    catch (ClassCastException exc)
		    {
			Log.i(TAG, "Failed to cast correctly");
		    }

		    notifyDataSetChanged();
		}
		else
		{
		    Log.i(TAG, "Doing a clear");
		    mData.clear();
		    notifyDataSetChanged();
		}
	    }
	};
	return myFilter;
    }

    @Override
    public int getPositionForSection(int section)
    {
	String letter;
	try
	{
	    letter = (String) sections[section];
	    return alphabetIndexer.get(letter);
	}
	catch (ArrayIndexOutOfBoundsException exc)
	{
	    Log.i(TAG, "Died on section" + section);
	}

	return 0;
    }

    @Override
    public int getSectionForPosition(int position)
    {
	String s = (String) mData.get(position).getTitle();
	String ch = s.substring(0, 1);

	for (int i = 0; i < sections.length; i++)
	{
	    if (sections[i].equals(ch))
	    {
		return i;
	    }
	}

	return 0;
    }

    @Override
    public Object[] getSections()
    {
	return sections;
    }

    public void clear()
    {
	if (mOriginalData != null)
	{
	    mOriginalData.clear();
	}
	if (mData != null)
	{
	    mData.clear();
	}

	notifyDataSetChanged();
    }

}
