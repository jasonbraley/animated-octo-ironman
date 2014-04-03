package com.example.humanrocketjlb.karaokehelper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter
{
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
    
    public void add( List<SongRecord> list)
    {
	data.addAll(list);
	notifyDataSetChanged();
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

}
