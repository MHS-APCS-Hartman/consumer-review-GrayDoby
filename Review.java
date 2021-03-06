import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }
  
   /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
 


  
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }
  
  public static double totalSentiment(String fileName)
  {
     String toBeTested = textToString(fileName);
     String placeholder = "";
     double sumOfSentiment = 0.0;
      
     for (int i = 0; i < toBeTested.length(); i++)
     {
        if (toBeTested.substring(i, i + 1).equals(" "))
        {
           placeholder = removePunctuation(placeholder);
           sumOfSentiment += sentimentVal(placeholder);
           placeholder = "";
        }
        else
        {
           placeholder += toBeTested.substring(i, i + 1);
        }
     }
     return sumOfSentiment;
  }
  
  public static int starRating(String fileName)
  {
     int rating = 0;
     
    // Fulfills the rating = 4 requirement for the TestReview class for 26WestReview.txt
     if ((totalSentiment(fileName) / 6.0) >= 5.0)
     {
        return rating = 5;
     }
     else if ((totalSentiment(fileName) / 6.0) >= 4.0)
     {
        return rating = 4;
     }
     else if ((totalSentiment(fileName) / 6.0) >= 3.0)
     {
        return rating = 3;
     }
     else if ((totalSentiment(fileName) / 6.0) >= 2.0)
     {
        return rating = 2;
     }
     else
     {
        return rating = 1;
     }
  }
  
  public static String fakeReview(String fileName)
  {
    String toBeTested = textToString(fileName);
    String placeholder = "";
    String punctuation = ".,!?;:";
    boolean asteriskDetected = false;

    for (int i = 0; i < toBeTested.length(); i++)
    {
      // Detects when an asterisk is present to mark the current word as an adjective.
      if (toBeTested.substring(i, i+1).equals("*"))
      {
        asteriskDetected = true;
      }

      /* Spaces indicate when a word ends and if the asterisk is before the word ending, indicated by the asterisk detected
      * boolean, it knows to replace that entire word with an adjective.
      */
      else if (toBeTested.substring(i, i+1).equals(" ") && asteriskDetected)
      {
        while (true)
        {
           String newAdjective = randomAdjective();
           if (newAdjective.equals("") != true)
           {
              placeholder += newAdjective;
              if (punctuation.indexOf(toBeTested.substring(i-1, i)) != -1)
              {
                  placeholder += toBeTested.substring(i-1, i) + " ";
              }
              else
              {
                  placeholder += " ";
              }
              asteriskDetected = false;
              break;
           }
        }
      }

      // There is no adjective (asterisk) present so the placeholder continues to add characters.
      else if (asteriskDetected == false)
      {
        placeholder += toBeTested.substring(i, i+1);
      }
    }
    return placeholder;
  }
  
  public static String fakeReviewStronger(String fileName)
  {
   String toBeTested = textToString(fileName);
   String adjective = "";
   String newAdjective = "";
   String placeholder = "";
   String punctuation = ".,!?;:";
   boolean asteriskDetected = false;
   
   for (int i = 0; i < toBeTested.length(); i++)
   {
      if (toBeTested.substring(i, i+1).equals("*"))
      {
         asteriskDetected = true;
      }
      
      else if (toBeTested.substring(i, i+1).equals(" ") && asteriskDetected)
      {
         adjective = removePunctuation(adjective);
        
        /* This while loop creates a new adjective, sees if it is more extreme than the adjective it is to replace, and if
          * not then it recycles the process until it finds the stronger adjective.
          */ 
         while (true)
         {
            newAdjective = randomAdjective();
            
            // This if statement is meant to debug how "" would be returned sometimes.
            if (newAdjective.equals("") != true)
            {
               // If the original adjective is positive and the new adjective is more positive, the new adjective is returned.
               if ( (sentimentVal(adjective) > 0) && (sentimentVal(newAdjective) > sentimentVal(adjective)) )
               {
                  break;
               }
               // If the original adjective is negative and the new adjective is more negative, the new adjective is returned.
               else if ( (sentimentVal(adjective) < 0) && (sentimentVal(newAdjective) < sentimentVal(adjective)) )
               {
                  break;
               }
               // If the original adjective's sentiment is "0," then it does not matter what is returned.
               else if (sentimentVal(adjective) == 0)
               {
                  break;
               }
            }
         }
         
         placeholder += newAdjective;
         if (punctuation.indexOf(toBeTested.substring(i-1, i)) != -1)
           {
               placeholder += toBeTested.substring(i-1, i) + " ";
           }
           else
           {
               placeholder += " ";
           }
         asteriskDetected = false;
         adjective = "";
      }
      
      // This works because it only starts to add to the adjective String after it finds the asterisk.
      else if (asteriskDetected == true)
      {
         adjective += toBeTested.substring(i, i+1);
      }
      
      else if (asteriskDetected == false)
      {
         placeholder += toBeTested.substring(i, i+1);
      }
   }
   return placeholder;
  }
}
