//Launches the JavaBee game
public class JavaBeeLauncher{
   
   
   //Uses the hardcoded final array in GameLogic for the hive's
   //characters instead of generating random letters
   public static boolean DEBUG_USE_HARDCODED_HIVES = false;
      
   //When enabled, words can use letters that are not in any of the 
   //hives, and do not need to contain the center hive letter.
   //Words must still meet lenght requirements.  
   //Words already in the wordlist are still rejected.
   public static boolean DEBUG_ALL_LETTERS_VALID = false;
   
   //When enabled, words which don't appear in the dictionary are
   //still considered valid. Words must still meet length requirements.
   //Words must also still meet letter requirements (unless 
   //DEBUG_ALL_LETTERS_VALID is enabled).
   public static boolean DEBUG_NO_DICT_VERIFY = false;
   
   
   
   
   //Run me to launch the game!
   public static void main(String[] args){
      GameGUI.launchGame();
   }
   
   
   
   
   
}