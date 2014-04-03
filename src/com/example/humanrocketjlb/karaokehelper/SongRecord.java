package com.example.humanrocketjlb.karaokehelper;

import android.graphics.Bitmap;

public class SongRecord
{
    private String mTitle;
    private String mArtist;
    private String mId;
    private Bitmap mAlbumCover;

    public SongRecord(String artist, String title, String id)
    {
	if (artist == null)
	{
	    this.mArtist = "";
	}
	else
	{
	    this.mArtist = artist;
	}

	if (title == null)
	{
	    this.mTitle = "";
	}
	else
	{
	    this.mTitle = title;
	}

	if (id == null)
	{
	    this.mId = "";
	}
	else
	{
	    this.mId = id;
	}
    }

    public String getTitle()
    {
	return mTitle;
    }

    public void setTitle(String title)
    {
	this.mTitle = title;
    }

    public String getArtist()
    {
	return mArtist;
    }

    public void setArtist(String artist)
    {
	this.mArtist = artist;
    }

    public String getId()
    {
	return mId;
    }

    public void setId(String id)
    {
	this.mId = id;
    }

    public Bitmap getAlbumCover()
    {
	return mAlbumCover;
    }

    public void setAlbumCover(Bitmap albumCover)
    {
	this.mAlbumCover = albumCover;
    }
}
