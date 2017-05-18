package main;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;

public class Tweeter
{
	Twitter twitter;
	ImageUpload iUpload;
	public Tweeter(Twitter twitter)
	{
		this.twitter = twitter;
		iUpload = new ImageUploadFactory().getInstance();
	}
	public void reply(User usr)
	{
		String text = "Hey"+usr.getName()+", Here is a graph of your most commonly used words";
		StatusUpdate status = new StatusUpdate(text);
		
	}
}
