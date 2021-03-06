//Mr. Black
//version 2016.05.10
package main;
import twitter4j.*;       //set the classpath to lib\twitter4j-core-4.0.2.jar
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Twitter_Driver
{
   private static PrintStream consolePrint;
   
   public static void main (String []args) throws TwitterException, IOException
   {
      consolePrint = System.out;
      Tweeter tweeter = new Tweeter();

//      LHSTwitter bigBird = new LHSTwitter(consolePrint);
//
//       JFrame jFrame = new JFrame("Histogram");
//       jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//       jFrame.setLayout(new BorderLayout());
//
//
//       Scanner scan = new Scanner(System.in);
//       System.out.print("Please enter a Twitter handle, do not include the @symbol --> ");
//       String twitter_handle = scan.next();
//       String commonest;
//       String commonestword;
//       String commonestnum;
//       while (!twitter_handle.equals("done"))
//       {
//    	   commonest = null;
//    	   commonestword = "";
//    	   commonestnum = "";
//    	 
//           bigBird.makeSortedListOfWordsFromTweets(twitter_handle);
//           System.out.println("Twitter_Driver.main()2");
//          jFrame.setVisible(true);
//          commonest = bigBird.mostPopularWord(bigBird.getUsageMap(false));
//          for(int i = 0; i < commonest.length(); i++)
//          {
//        	  if(Character.isDigit(commonest.charAt(i)))
//        	  {
//        		  commonestnum += commonest.charAt(i);
//        	  }
//        	  else
//        	  {
//        		  commonestword += commonest.charAt(i);
//        	  }
//          }
//          consolePrint.println("The most common word from @" + twitter_handle + " is: " + commonestword +" with a count of:: "+commonestnum);
//          BarGraph barGraph = new BarGraph(bigBird.getUsageMapOfSize(10));
//          jFrame.add(new JScrollPane(barGraph));
//          jFrame.pack();
//          jFrame.setLocationRelativeTo(null);
//          jFrame.setVisible(true);
//          ImageIO.write(barGraph.getScreenShot(), "png", new File(twitter_handle+".png"));
//   
//          consolePrint.println();
//          consolePrint.print("Please enter a Twitter handle, do not include the @ symbol --> ");
//          twitter_handle = scan.next();
//       }
//       scan.close();
         
         
   }//end main  
   
         
}//end driver        
         
class LHSTwitter 
{
   private Twitter twitter;
   private PrintStream consolePrint;
   private List<Status> statuses;
   private List<String> sortedTerms;
   private LinkedHashMap<String, Integer> usageMap;
   
   public LHSTwitter(PrintStream console)
   {
      // Makes an instance of Twitter - this is re-useable and thread safe.
      twitter = TwitterFactory.getSingleton(); //connects to Twitter and performs authorizations
      consolePrint = console;
      statuses = new ArrayList<Status>();
      sortedTerms = new ArrayList<String>();   
      
   }
   
   /******************  Part 1 *******************/
   public void tweetOut(String message) throws TwitterException, IOException
   {
	   twitter.updateStatus("I just tweeted from my Java program! #APCSRocks @LHSWesterners Thanks @lhscompsci!");
   }
   @SuppressWarnings("unchecked")
   /******************  Part 2 *******************/
   public void makeSortedListOfWordsFromTweets(String handle) throws TwitterException, IOException
   {
	  
      statuses.clear();
      sortedTerms.clear();
      PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt")); // Creates file for dedebugging purposes
      Paging page = new Paging (1,200);
      int p = 1;
      while (p <= 10)
      {
         page.setPage(p);
         System.out.println("Getting Page ::"+p);
         statuses.addAll(twitter.getUserTimeline(handle,page)); 
         p++;        
      }
      int numberTweets = statuses.size();
      fileout.println("Number of tweets = " + numberTweets);
      System.out.println("Number of tweets = " + numberTweets);
      fileout.close();
      fileout = new PrintStream(new FileOutputStream("garbageOutput.txt"));
   
      int count=1;
      for (Status j: statuses)
      {
         fileout.println(count+".  "+j.getText());
         count++;
      }		
      fileout.close();  	
     	//Makes a list of words from user timeline
      for (Status status : statuses)
      {	
         String[]array = status.getText().split(" ");
         for (String word : array)
         {
        		 sortedTerms.add(removePunctuation(word).toLowerCase());
         }
      }	
   					
      // Remove common English words, which are stored in commonWords.txt
      System.out.println("Removing common words!");
      sortedTerms = removeCommonEnglishWords(sortedTerms, handle);
      sortAndRemoveEmpties();
      
      
   }
   
   // Sort words in alpha order. You should use your old code from the Sort/Search unit.
   // Remove all empty strings ""
   @SuppressWarnings("unchecked")
   private void sortAndRemoveEmpties()
   {
	   System.out.println("Sorted Terms length before space removal and sorting::"+sortedTerms.size());
      for(int i = 0; i < sortedTerms.size(); i++)
      {
    	  if(sortedTerms.get(i).equals(""))
    	  {
    		  sortedTerms.remove(i);
    		  i--;
    	  }
      }
      sortedTerms.sort(new comparators.stringComparator());
      System.out.println("Sorted Terms length after space removal and sorting::"+sortedTerms.size());
   }
   
   // Removes common English words from list
   // Remove all words found in commonWords.txt  from the argument list.
   // The count will not be given in commonWords.txt. You must count the number of words in this method.
   // This method should NOT throw an exception. Use try/catch.
    @SuppressWarnings("unchecked")
   private List removeCommonEnglishWords (List<String> list, String handle)
   {
    	List<String> list2 = new ArrayList<String>();    	
    	Scanner scanner;
		try
		{
			
			scanner = new Scanner(new File("src/commonWords.txt"));
			while(scanner.hasNext())
	        {
	        	list2.add(scanner.next());
	        }
	        for(int i = 0; i < list.size(); i++)
	        {
	        	if(list2.contains(list.get(i)))
	        	{
	        		list.remove(i);
	        		i--;
	        	}
	        	else if(list.get(i).equals(handle))
	        	{
	        		list.remove(i);
	        		i--;
	        	}
	        }
	        scanner.close();
	        
	        
	        return list;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			System.err.println("Could Not Find commonwords.txt");
			e.printStackTrace();
		}
		return null;
        
   }
   
   //Remove punctuation - borrowed from prevoius lab
   //Consider if you want to remove the # or @ from your words. They could be interesting to keep (or remove).
   private String removePunctuation( String s )
   {
	  StringBuilder stringBuilder = new StringBuilder(s);
	  while(stringBuilder.indexOf("@") != -1)
	  {
		  stringBuilder.deleteCharAt(stringBuilder.indexOf("@"));
	  }
	  return stringBuilder.toString();
   }
   public LinkedHashMap<String, Integer> getUsageMap(boolean forceNewMap)
   {
	   if(usageMap == null || forceNewMap)
	   {
		   LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<String, Integer>(sortedTerms.size());
	   		for(String i: sortedTerms)
	   		{
	   			if(hashMap.get(i) == null)
	   			{
	   				hashMap.put(i, 1);
	   			}
	   			else 
	   			{
	   				hashMap.put(i, hashMap.get(i)+1);
	   			}
	   		}
	   		usageMap = hashMap;
	   }
	   return usageMap;
	   	
   }
   public LinkedHashMap<String, Integer> getUsageMapOfSize(int size)
   {
	   LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<String, Integer>(sortedTerms.size());
	   if(usageMap == null)
	   {
		  
	   		for(String i: sortedTerms)
	   		{
	   			if(hashMap.get(i) == null)
	   			{
	   				hashMap.put(i, 1);
	   			}
	   			else 
	   			{
	   				hashMap.put(i, hashMap.get(i)+1);
	   			}
	   		}
	   		usageMap = hashMap;
	   		
	   }
	   if(usageMap.size() >= size)
	   {
		   hashMap = new LinkedHashMap<String, Integer>();
		   List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(usageMap.entrySet());
		   Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>()
		   {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
				{
					return o2.getValue().compareTo(o1.getValue());
				}
				
			});
			for(int i = 0; i < size; i++)
			{
				hashMap.put(entries.get(i).getKey(), entries.get(i).getValue());
			}
		    
	   }
	   else 
		   return null;
	   
	   return hashMap;
	   	
   }
   
   //Should return the most common word from sortedTerms. 
   //Consider case. Should it be case sensitive? The choice is yours.
    @SuppressWarnings("unchecked")
   public String mostPopularWord(HashMap<String, Integer> map)
   { 
    	String largest = null;
    	HashMap<String, Integer> hashMap = map;
    	Iterator it = hashMap.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)it.next();
    		if(largest != null)
    		{
    			if(pair.getValue() > hashMap.get(largest))
        		{
        			largest = pair.getKey();
        		}
    		}
    		else
    		{
    			largest = pair.getKey();
    		}
    		
    		
    	}

      return largest+hashMap.get(largest);
   }

   /******************  Part 3 *******************/
   public void investigate()
   {
   }
   // A sample query to determine how many people in Lubbock, TX tweet about the Texas Rangers
   public void sampleInvestigate ()
   {
      Query query = new Query("Texas Rangers");
      query.setCount(100);
      query.setGeoCode(new GeoLocation(33.5755059,-101.8606604), 5, Query.MILES);
      query.setSince("2016-04-15");
      try {
         QueryResult result = twitter.search(query);
         System.out.println("Count : " + result.getTweets().size()) ;
         for (Status tweet : result.getTweets()) {
            System.out.println("@"+tweet.getUser().getName()+ ": " + tweet.getText());  
         }
      } 
      catch (TwitterException e) {
         e.printStackTrace();
      } 
      System.out.println(); 
   }  
   
}  

// Consider adding a sorter class here.