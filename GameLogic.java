import java.util.Random;
import java.util.Scanner;

import javax.management.monitor.GaugeMonitorMBean;

import java.util.Arrays;
import java.util.Map;
import java.awt.event.KeyEvent;
import java.io.*;

import java.io.File;
import java.io.FileNotFoundException;

//Name: Daniel Leon Silva

//Handles the logic for the JavaBee game
public class GameLogic{
   
   
   //Name of dictionary file (containing English words to validate guesses against)
   private static final String DICTIONARY_FILENAME = "dictionary.txt";   
    
   //Total number of hives in the game
   public static final int HIVE_COUNT = 7;
      
   //Required Min/Max length for a valid player guess
   public static final int MIN_WORD_LENGTH = 4;
   public static final int MAX_WORD_LENGTH = 19;    
   
   
   //Required Min/Max number of formable words for a randomized hive
   public static final int MIN_FORMABLE = 30;
   public static final int MAX_FORMABLE = 110;    
   
   //Collection of various letters (vowels only, consonants only, all letters)
   public static final String VOWEL_CHARS = "AEIOU";  
   public static final String CONSONANT_CHARS = "BCDFGHJKLMNPQRSTVWXYZ";
   public static final String ALL_CHARS = VOWEL_CHARS + CONSONANT_CHARS;
   
   //The various score rank thresholds and their respective titles
   public static final double[] RANK_PERCENTS = {0, 0.02, 0.05, 0.08, 0.15, 0.25, 0.4, 0.5, 0.7};
   public static final String[] RANK_TITLES = {"Beginner", "Good Start", "Moving Up", 
      "Good", "Solid", "Nice", "Great", 
      "Amazing", "Genius"};
   
   //Text for different error messages that occur for various invalid inputs
   private static final String ERROR_TOO_LONG = "Too long...";
   private static final String ERROR_TOO_SHORT = "Too short...";
   private static final String ERROR_MISSING_CENTER = "Missing yellow letter...";
   private static final String ERROR_INVALID_LETTER = "Contains non-hive letter...";   
   private static final String ERROR_ALREADY_FOUND = "Already in word list...";  
   private static final String ERROR_NOT_WORD = "Not in dictionary...";
   
   //Character codes for the enter and backspace key press
   public static final char ENTER_KEY = KeyEvent.VK_ENTER;
   public static final char BACKSPACE_KEY = KeyEvent.VK_BACK_SPACE;  
   
   //A collection of letters to be used for the hives when the "Hardcoded hives" debug is enabled
   private static final char[] DEBUG_HARDCODED_HIVES = {'R', 'A', 'C', 'D', 'L', 'O', 'T'};      
   
   //Use me for generating random numbers (see https://docs.oracle.com/javase/8/docs/api/java/util/Random.html)!
   private static final Random rand = new Random(); 

      

   //...Feel free to add more **FINAL** variables of your own!
   
   



   
   
   //******************   NON-FINAL GLOBAL VARIABLES   ******************
   //********  YOU CANNOT ADD ANY ADDITIONAL NON-FINAL GLOBALS!  ******** 
   //********     YOU WILL ONLY NEED THESE FOR MILESTONE #2       ********
   
   
   //Array storing all formable words given the chosen hives
   public static String[] validWords = new String[MAX_FORMABLE];   
   
   //The maximum number of points possible given the game's chosen hive letters
   public static int maxPoints = 0;
   
   
   
   //*******************************************************************
   
   //Helper Functions

   public static char letterPicker (String letterSet, int lowestIndex, int highestIndex) {
      int letterChooser = (int) (Math.random() * (highestIndex - lowestIndex + 1)) + lowestIndex;
      return letterSet.charAt(letterChooser);
   }

   public static boolean arrHasVowel (char[] arr) {
      int vowelCount = 0;
      for (int i = 0; i < arr.length; i++) {
         if ("AEIOU".contains(arr[i] + "")) {
            vowelCount++;
         }
      }
      return vowelCount > 0;
   }

   public static char[] newHiveArr () {
      char[] hiveArr = new char[7];
      int count = 0;
      String potentialLetters = "ABCDEFGHIJKLMNOPQRTUVWXYZ";
      String vowelLetters = "AEIOU";

      while (count < 7) 
      {
         char pickedLetter = letterPicker(potentialLetters, 0, potentialLetters.length() - 1);
         if (potentialLetters.indexOf(pickedLetter + "") == potentialLetters.length() - 1) {
            potentialLetters = potentialLetters.substring(0, potentialLetters.length() - 1);
         } else {
            potentialLetters = potentialLetters.substring(0, potentialLetters.indexOf(pickedLetter + "")) 
                           + potentialLetters.substring(potentialLetters.indexOf(pickedLetter + "") + 1, potentialLetters.length());
         }
         hiveArr[count] = pickedLetter;
         count++;
      }

      if (arrHasVowel(hiveArr) == false) {
         int vowelPicker = (int) (Math.random() * 5); 
         int indexPicker = (int) (Math.random() * 7); 
         hiveArr[indexPicker] = vowelLetters.charAt(vowelPicker);
      }
      
      return hiveArr;
   }

   public static boolean notInWordList(String[] listOfWords, String playerGuess) {
      for (int i = 0; i < listOfWords.length; i++) {
         if (listOfWords[i].toUpperCase().equals(playerGuess.toUpperCase())) {
            return false;
         }
      }
      return true;
   }

   public static boolean hasValidChars(char[] guessAsList, String hiveAsString) {
      for (int i = 0; i < guessAsList.length; i++) {
         if (!(hiveAsString.contains(guessAsList[i] + ""))) {
            return false;
         }
      }
      return true;
   }

   public static String hiveString(char[] hiveLetters) {
      String hiveStr = "";
      // creates a string with all the hive letters in it
      for (int i = 0; i < hiveLetters.length; i++) {
         hiveStr += hiveLetters[i];
      }
      return hiveStr;
   } 

   public static boolean formableWord(String word, char[] hiveLetters) {
      String hiveStr = hiveString(hiveLetters);
      int count = 0;

      if (!(word.contains(hiveLetters[0] + ""))) {
         return false;
      }
      for (int i = 0; i < word.length(); i++) {
         if (hiveStr.contains(word.substring(i, i+1))) {
            count++;
         }
      }

      return count == word.length();
   }

   public static boolean isValidEnglishWord(String playerGuess, String[] validWordsArr) {
      for (int i = 0; i < validWordsArr.length; i++) {
         if (playerGuess.equals(validWordsArr[i])) {
            return true;
         }
      }
      return false;
   }

   public static boolean usesAllHives (char[] hive, String word) {
      
      for (int i = 0; i < hive.length; i++) {
         if (!(word.contains(hive[i] + ""))) {
            return false;
         }
      }
      return true;
   }

   public static int totalPoints (char[] hives, String word) {
      
      if (word.length() == 4) {
         return 1;
      } else if (word.length() > 4 && usesAllHives(hives, word)) {
         return word.length() + 7;
      }
      return word.length(); 
   }

   public static String currentRank (int totalPoints) {
      double percentage = ((double) totalPoints)/maxPoints;
      for (int i = RANK_PERCENTS.length - 1; i > 0; i--) {
         if (percentage >= RANK_PERCENTS[i]) {
            return RANK_TITLES[i];
         }
      }
      return RANK_TITLES[0];
   }

   public static boolean useHardCodedHives() {
      return JavaBeeLauncher.DEBUG_USE_HARDCODED_HIVES;
   }

   public static boolean useDictToVerify() {
      return JavaBeeLauncher.DEBUG_NO_DICT_VERIFY == false;
   }


   //End of Helper Functions
   
   //This function gets called ONCE when the game is very first launched
   //before the user has the opportunity to do anything.
   //
   //Should perform any initialization that needs to happen at the start of the game,
   //and return the randomly chosen hive letters as a char array.  Whichever letter
   //is at index 0 of the array will be the center (yellow) hive letter, the remainder
   //will be the outer (gray) hive letters.
   //
   //The returned char array:
   //  -must be seven letters long
   //  -cannot have duplicate letters
   //  -cannot have an 'S' as one of its letters
   //  -must contain AT LEAST one vowel character (AEIOU) 
   //   (additionally: if the array only contains one vowel, it should be 
   //    possible for the vowel to be in any hive, including the center)
   public static char[] initializeGame(){

      /*
      if dictionary verify boolean is enabled, make sure that you
      have a dictionary use the hive to see how many words are formable. Use the try
      statement below to make a valid words array for this hardcoded hive
      */
      char[] hiveAsArr = newHiveArr();
      if (useHardCodedHives()) {
         hiveAsArr = DEBUG_HARDCODED_HIVES;
      }

      if (useDictToVerify() == false) {
         return hiveAsArr;
      }
      

      File dictionary = new File (DICTIONARY_FILENAME);
      Scanner scan;
      boolean hiveMeetsRequirements = false;
      int validWordCount = 0;
      
      
      try {
         while (hiveMeetsRequirements == false) {
            scan = new Scanner(dictionary);
            while (scan.hasNextLine()) {
               String dictWord = scan.nextLine();
               if (formableWord(dictWord, hiveAsArr)) {
                  if (useHardCodedHives()) {
                     if (validWordCount < 110) {
                        validWords[validWordCount] = dictWord;
                        validWordCount++;
                        maxPoints += totalPoints(hiveAsArr, dictWord);
                     }
                  } else {
                     if (validWordCount < 110) {
                        validWords[validWordCount] = dictWord;
                        validWordCount++;
                        maxPoints += totalPoints(hiveAsArr, dictWord);
                     } else {
                        validWordCount++;
                     }
                  }
               }
            }
            if (useHardCodedHives()) {
               hiveMeetsRequirements = true;
            } else if (validWordCount >= 30 && validWordCount <= 110) {
               hiveMeetsRequirements = true;
            } else {
               hiveAsArr = newHiveArr();
               validWordCount = 0;
               maxPoints = 0;
               validWords = new String[MAX_FORMABLE];
            }
         }

      } catch (FileNotFoundException notFound) {
         System.out.println("File " + DICTIONARY_FILENAME + " not found.");
         System.exit(1);
      }

      GameGUI.setRank(RANK_TITLES[0]);
      
      return hiveAsArr;
   }

   //Complete your warmup task (Section 3.2.2 step 2) here by calling the requisite
   //functions out of GameGUI.
   //This function gets called ONCE after the graphics window has been
   //initialized and initializeGame has been called.
   public static void warmup(){
      /*
      GameGUI.setPlayerGuess("DAN");
      GameGUI.setRank("CompSci");
      GameGUI.addToWordList("Raiders", 100);
      GameGUI.addToWordList("Java", 2);
      */
      
      //All of your 3.2.2 step 2 warmup code will go here!
      //Where will the code for step 3 go...?
   }     
   

   
   //This function gets called everytime the user types a valid key on the
   //keyboard (alphabetic character, enter, or backspace) or clicks one of the
   //hives/buttons in the game window.
   //
   //The key pressed is passed in as a char value.
   public static void reactToKey(char key){
      /*
      if (key == 'G') {
         GameGUI.displayErrorMessage("WARMUP!");
         GameGUI.wigglePlayerGuess();
      }
      */
      

      String word = GameGUI.getPlayerGuessStr();
      String[] listOfWords = GameGUI.getWordList();
      String hiveAsString = hiveString(GameGUI.getAllHiveLetters());
      char[] guessAsList = GameGUI.getPlayerGuessArr();
      String[] validEnglishWords = GameLogic.validWords;
      
      if (key == BACKSPACE_KEY) {
         //If length more than 0 removes one char from word
         if (word.length() > 0) {
            word = word.substring(0, word.length()-1);
            GameGUI.setPlayerGuess(word);
         }
      }
      else if (key == ENTER_KEY) {
         //If length is more than three shows a "too short" error and wiggles the word
         if (word.length() <= 3) {
            GameGUI.displayErrorMessage(ERROR_TOO_SHORT);
            GameGUI.wigglePlayerGuess();
         }
         // A "missing center" error and a wiggle is displayed if the word does not have a center
         else if (word.indexOf(GameGUI.getCenterHiveLetter()) == -1) {
            GameGUI.displayErrorMessage(ERROR_MISSING_CENTER);
            GameGUI.wigglePlayerGuess();
         } 
         // If there are chars that are not from the hive in the world then an error and a wiggle are shown
         else if (hasValidChars(guessAsList, hiveAsString) == false) {
            GameGUI.displayErrorMessage(ERROR_INVALID_LETTER);
            GameGUI.wigglePlayerGuess();
         } 
         // If the word is in the word list then an error and a wiggle are displayed
         else if (notInWordList(listOfWords, word) == false) {
            GameGUI.displayErrorMessage(ERROR_ALREADY_FOUND);
            GameGUI.wigglePlayerGuess();
         } else if (useDictToVerify() && isValidEnglishWord(word, validEnglishWords) == false) {
            GameGUI.displayErrorMessage(ERROR_NOT_WORD);
            GameGUI.wigglePlayerGuess();
         // If the word does not contain of the first five errors it is added to the word list
         } else {
            if (useDictToVerify()) {
               GameGUI.addToWordList(word, totalPoints(GameGUI.getAllHiveLetters(), word));
               GameGUI.setRank(currentRank(GameGUI.getPlayerScore()));
            } else {
               GameGUI.addToWordList(word, 0);
            }
            word = "";
            GameGUI.setPlayerGuess(word);
            
         }
      } else {
         // If the user's word length is 19 and they attempt to type in another character an error and a wiggle are shown
         if (word.length() + 1 > 19) {
            GameGUI.displayErrorMessage(ERROR_TOO_LONG);
            GameGUI.wigglePlayerGuess();
         } else {
            // If there is no "too long" error then the character is added to the word
            word += key;
            GameGUI.setPlayerGuess(word);
         }
      }
      

      
      System.out.println("reactToKey(...) called! key (int value) = '" + ((int)key) + "'");
      
      
   }   
}
