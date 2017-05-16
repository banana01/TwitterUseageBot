//Mr. Black
//version 2016.05.10

import twitter4j.*;       //set the classpath to lib\twitter4j-core-4.0.2.jar
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

public class Twitter_Driver
{
   private static PrintStream consolePrint;
   
   public static void main (String []args) throws TwitterException, IOException
   {
      consolePrint = System.out;
      
      // PART 1
      // set up classpath and properties file
             
      LHSTwitter bigBird = new LHSTwitter(consolePrint);
         
      //create message to tweet, then call the tweetOut method
      // bigBird.tweetOut(message);
     
      // PART 2
      // Choose a public Twitter user's handle 
         
      // Scanner scan = new Scanner(System.in);
//       console.print("Please enter a Twitter handle, do not include the @symbol --> ");
//       String twitter_handle = scan.next();
//          
//       while (!twitter_handle.equals("done"))
//       {
//          // Print the most popular word they tweet
//          bigBird.makeSortedListOfWordsFromTweets(twitter_handle);
//          consolePrint.println("The most common word from @" + twitter_handle + " is: " + bigBird.mostPopularWord());
//          consolePrint.println();
//          consolePrint.print("Please enter a Twitter handle, do not include the @ symbol --> ");
//          twitter_handle = scan.next();
//       }
         
      // PART 3
      //bigBird.investigate();
         
         
   }//end main         
         
}//end driver        
         
class LHSTwitter 
{
   private Twitter twitter;
   private PrintStream consolePrint;
   private List<Status> statuses;
   private List<String> sortedTerms;
   
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
         statuses.addAll(twitter.getUserTimeline(handle,page)); 
         p++;        
      }
      int numberTweets = statuses.size();
      fileout.println("Number of tweets = " + numberTweets);
      
      fileout = new PrintStream(new FileOutputStream("garbageOutput.txt"));
   
      int count=1;
      for (Status j: statuses)
      {
         fileout.println(count+".  "+j.getText());
         count++;
      }		
         	
     	//Makes a list of words from user timeline
      for (Status status : statuses)
      {			
         String[]array = status.getText().split(" ");
         for (String word : array)
            sortedTerms.add(removePunctuation(word).toLowerCase());
      }	
   					
      // Remove common English words, which are stored in commonWords.txt
      sortedTerms = removeCommonEnglishWords(sortedTerms);
      sortAndRemoveEmpties();
      
   }
   
   // Sort words in alpha order. You should use your old code from the Sort/Search unit.
   // Remove all empty strings ""
   @SuppressWarnings("unchecked")
   private void sortAndRemoveEmpties()
   {
      
   }
   
   // Removes common English words from list
   // Remove all words found in commonWords.txt  from the argument list.
   // The count will not be given in commonWords.txt. You must count the number of words in this method.
   // This method should NOT throw an exception. Use try/catch.
    @SuppressWarnings("unchecked")
   private List removeCommonEnglishWords (List<String> list)
   {	
      return null;
   }
   
   //Remove punctuation - borrowed from prevoius lab
   //Consider if you want to remove the # or @ from your words. They could be interesting to keep (or remove).
   private String removePunctuation( String s )
   {
      return "";
   }
   //Should return the most common word from sortedTerms. 
   //Consider case. Should it be case sensitive? The choice is yours.
    @SuppressWarnings("unchecked")
   public String mostPopularWord()
   {
      return "";
   }
   

   /******************  Part 3 *******************/
   public void investigate ()
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