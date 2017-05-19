package main;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;

public class Tweeter
{
	Twitter twitter;
	ImageUpload iUpload;
	ConfigurationBuilder cfb;
	MentionListener ml;
	public Tweeter()
	{
		cfb = new ConfigurationBuilder();
		cfb.setDebugEnabled(true);
		cfb.setOAuthConsumerKey("ep6rkXSxvFv8yomWQpV0kmntP");
		cfb.setOAuthConsumerSecret("QaqLuXnDBYlBK5wkPTQpqi7fZyzNAQVUhdMF8WOCzEI3sUxrKj");
		cfb.setOAuthAccessTokenSecret("edQOO3GyUk0zoMcEi0NvPiorcDH0lpupLoxNTzGBthBmi");
		cfb.setOAuthAccessToken("861658168002260992-cBtm1zL66gHK2yh1zQESPwo2v7UOJWL");
		this.twitter = new TwitterFactory(cfb.build()).getInstance();
		//iUpload = new ImageUploadFactory().getInstance();
		try
		{
			ml = new MentionListener(twitter, this);
		} catch (TwitterException | IOException | InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void reply(Status stat, String message, File img)
	{
		
		try
		{
			StatusUpdate status = new StatusUpdate(message);
			status.inReplyToStatusId(stat.getId());
			status.setMedia(img);
			
			twitter.updateStatus(status);
		} catch (TwitterException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
