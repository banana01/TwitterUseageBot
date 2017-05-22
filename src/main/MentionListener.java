package main;

import java.awt.BorderLayout;
import java.awt.print.Printable;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class MentionListener
{
	Twitter twitter;
	public MentionListener(Twitter twitter, Tweeter tweeter) throws TwitterException, IOException, InterruptedException
	{
		FileOutputStream FOS;
		ObjectOutputStream OOS;
		FileInputStream FIS;
		ObjectInputStream OIS;
		this.twitter = twitter;
		PrintStream consolePrint = System.out;
		LHSTwitter lhsTwitter = new LHSTwitter(consolePrint);
		JFrame jFrame = new JFrame();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLayout(new BorderLayout());
		
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);
     	
		BarGraph barGraph;
		ResponseList<Status> mentions;
		User user;
		File file;
		Status lastProccessed = null;
		try
		{
			if(new File("lastProcessed.ser").exists())
			{
				FIS = new FileInputStream(new File("lastProcessed.ser"));
				OIS = new ObjectInputStream(FIS);
					lastProccessed = (Status) OIS.readObject();
					FIS.close();
					OIS.close();
			}
			
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(EOFException e)
		{
			System.err.println("No Seralized Status");
		}
		while(true)
		{
			System.out.println("awaiting mentions");
			mentions = twitter.getMentionsTimeline();
			System.out.println(mentions.size());
			int count = 0;
			for(Status status: mentions)
			{
				if(lastProccessed == null || status.getId() > lastProccessed.getId())
				{
					count++;
					System.out.println("processing mentions");
					user = status.getUser();
					lhsTwitter.makeSortedListOfWordsFromTweets(user.getScreenName());
					barGraph = new BarGraph(lhsTwitter.getUsageMapOfSize(10));
					jFrame.add(new JScrollPane(barGraph));
					jFrame.setVisible(true);
					file = new File(user.getScreenName()+".png");
					ImageIO.write(barGraph.getScreenShot(), "png", file);
					jFrame.remove(barGraph);
					System.out.println("replied to "+user.getScreenName());
					
					tweeter.reply(status,"Hey @"+user.getScreenName()+", here is a graph of your most common words!", file);
					FOS = new FileOutputStream(new File("lastProcessed.ser"), false);
					OOS = new ObjectOutputStream(FOS);
					lastProccessed = status;
					OOS.writeObject(lastProccessed);
					OOS.flush();
					OOS.close();
					FOS.flush();
					FOS.close();
				}

				
				
			}
			System.out.println("Actual Number of Tweets replied to: "+count);
			Thread.sleep(60000);
		}
		
	}

	
}
