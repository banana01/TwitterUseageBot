package main;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class MentionListener
{
	Twitter twitter;
	Tweeter tweeter;
	public MentionListener(Twitter twitter) throws TwitterException
	{
		this.twitter = twitter;
		tweeter = new Tweeter(twitter);
		ResponseList<Status> mentions;
		User user;
		while(true)
		{
			mentions = twitter.getMentionsTimeline();
			for(Status status: mentions)
			{
				user = status.getUser();
				tweeter.reply(user);
			}
		}
	}
	
}
