package com.example.humanrocketjlb.karaokehelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class SongListParser
{
    private static final String ns = null;

    public SongListParser()
    {

    }

    public List<SongRecord> parse(InputStream in)
	    throws XmlPullParserException, IOException
    {
	try
	{
	    XmlPullParser parser = Xml.newPullParser();
	    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	    parser.setInput(in, null);
	    parser.nextTag();
	    return readFeed(parser);
	}
	finally
	{
	    in.close();
	}
    }

    private List<SongRecord> readFeed(XmlPullParser parser)
	    throws XmlPullParserException, IOException
    {
	List<SongRecord> entries = new ArrayList<SongRecord>();

	parser.require(XmlPullParser.START_TAG, ns, "plist");
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }
	    String name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("dict"))
	    {
		readDict1(parser, entries);
	    }
	    else
	    {
		skip(parser);
	    }
	}
	return entries;
    }

    private void readDict1(XmlPullParser parser,
	    List<SongRecord> entries) throws XmlPullParserException,
	    IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "dict");
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }
	    String name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("dict"))
	    {
		readDict2(parser, entries);
	    }
	    else
	    {
		skip(parser);
	    }
	}
    }

    private void readDict2(XmlPullParser parser,
	    List<SongRecord> entries) throws XmlPullParserException,
	    IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "dict");
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }
	    String name = parser.getName();

	    // Starts by looking for the entry tag
	    if (name.equals("dict"))
	    {
		entries.add(readDict3(parser));
	    }
	    else
	    {
		skip(parser);
	    }
	}
    }

    private SongRecord readDict3( XmlPullParser parser )
	    throws XmlPullParserException, IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "dict");
	String title = null;
	String artist = null;
	String id = null;
	String lastKey = null;
	while (parser.next() != XmlPullParser.END_TAG)
	{
	    if (parser.getEventType() != XmlPullParser.START_TAG)
	    {
		continue;
	    }
	    String name = parser.getName();
	    if (name.equals("key"))
	    {
		lastKey = readKey(parser);
	    }
	    else if ( lastKey != null && lastKey.equals("Track ID") && name.equals("integer") )
	    {
		id = readTrackId(parser);
	    }
	    else if ( lastKey != null && lastKey.equals("Artist") && name.equals("string") )
	    {
		artist = readArtist(parser);
	    }
	    else if ( lastKey != null && lastKey.equals("Name") && name.equals("string") )
	    {
		title = readTitle(parser);
	    }
	    else
	    {
		skip(parser);
	    }
	}
	return new SongRecord(artist, title, id);
    }

    private String readKey(XmlPullParser parser) throws XmlPullParserException,
	    IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "key");
	String text = readText(parser);
	parser.require(XmlPullParser.END_TAG, ns, "key");
	return text;
    }
    
    private String readTrackId(XmlPullParser parser)
	    throws XmlPullParserException, IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "integer");
	String text = readText(parser);
	parser.require(XmlPullParser.END_TAG, ns, "integer");
	return text;
    }
    
    private String readArtist(XmlPullParser parser)
	    throws XmlPullParserException, IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "string");
	String text = readText(parser);
	parser.require(XmlPullParser.END_TAG, ns, "string");
	return text;
    }
    
    private String readTitle(XmlPullParser parser)
	    throws XmlPullParserException, IOException
    {
	parser.require(XmlPullParser.START_TAG, ns, "string");
	String text = readText(parser);
	parser.require(XmlPullParser.END_TAG, ns, "string");
	return text;
    }

    private String readText(XmlPullParser parser) throws IOException,
	    XmlPullParserException
    {
	String result = "";
	if (parser.next() == XmlPullParser.TEXT)
	{
	    result = parser.getText();
	    parser.nextTag();
	}
	return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException,
	    IOException
    {
	if (parser.getEventType() != XmlPullParser.START_TAG)
	{
	    throw new IllegalStateException();
	}
	int depth = 1;
	while (depth != 0)
	{
	    switch (parser.next())
	    {
	    case XmlPullParser.END_TAG:
		depth--;
		break;
	    case XmlPullParser.START_TAG:
		depth++;
		break;
	    }
	}
    }
}
