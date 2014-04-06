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
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter implements Filterable
{
    private static String TAG = "SongListAdapter";
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<SongRecord> data = new ArrayList<SongRecord>();

    public SongListAdapter(Context context)
    {
	mContext = context;
	mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
	return data.size();
    }

    @Override
    public SongRecord getItem(int position)
    {
	return data.get(position);
    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    public void add(SongRecord record)
    {
	data.add(record);
	notifyDataSetChanged();
    }

    public void add(List<SongRecord> list)
    {
	data.addAll(list);
	notifyDataSetChanged();
    }

    public void sortByTitle()
    {
	Collections.sort(data, new Comparator<SongRecord>()
	{
	    @Override
	    public int compare(SongRecord o1, SongRecord o2)
	    {
		return o1.getTitle().compareTo(o2.getTitle());
	    }
	});
    }

    public void sortByArtist()
    {
	Collections.sort(data, new Comparator<SongRecord>()
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
                if(constraint != null) 
                {
                    filterResults.values = data;
                    filterResults.count = data.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) 
            {
                if(results != null && results.count > 0) 
                {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

}
