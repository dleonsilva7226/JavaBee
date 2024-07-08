import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;


//  **********************************************************************************
//  *                                                                                *
//  *                                                                                *
//  *                      DO NOT MODIFY ANY CODE IN THIS FILE                       *
//  *                                                                                *
//  *                                                                                *
//  **********************************************************************************




//Handles all of the graphics/UI logic for the JavaBee game
public class GameGUI extends JComponent implements KeyListener, MouseListener, ActionListener{
   
   
   
   
   
   
   //*************     FUNCTIONS FOR STUDENTS TO CALL IN GameLogic.java    **************
   
   
   
   
   
   //Returns the text currently entered for the player guess as a STRING
   public static String getPlayerGuessStr(){
      return GameGUI.playerGuess;      
   } 
   
   
   
   
   //Returns the text currently entered for the player guess as a CHAR ARRAY
   public static char[] getPlayerGuessArr(){
      char[] toReturn = new char[GameGUI.playerGuess.length()];
      for (int i = 0; i < GameGUI.playerGuess.length(); i++)
         toReturn[i] = GameGUI.playerGuess.charAt(i);
      
      return toReturn;      
   }    
   
   
   
   
   //Sets the player guess text in the game window (above the hives) to the
   //argument String value.
   // 
   //This function will throw an EXCEPTION if given too long a String
   public static void setPlayerGuess(String guess){
      if (guess == null)
         throw new IllegalArgumentException("Error! null String passed to setPlayerGuessText(...)!");
      else if (guess.length() > GameLogic.MAX_WORD_LENGTH)
         throw new IllegalArgumentException("Error! Too long String passed to setPlayerGuessText(...)!: " + guess);         
      GameGUI.playerGuess = guess;      
   }
   
   
   
   
   //Returns the letter in the center (i.e. Yellow) Hive as a CHAR
   public static char getCenterHiveLetter(){      
      return hives.get(0).getLetter();
   }
   
   
   
   
   //Returns the letters in the outer (i.e. Gray) Hives as a CHAR ARRAY
   public static char[] getOuterHiveLetters(){
      char[] toReturn = new char[hives.size() - 1];
      for (int i = 1; i < hives.size(); i++)
         toReturn[i-1] = hives.get(i).getLetter(); 
      
      return toReturn;
   }
   
   
   
   
   //Returns the letters in all (i.e. Gray and Yellow) Hives as a CHAR ARRAY
   public static char[] getAllHiveLetters(){
      char[] toReturn = new char[hives.size()];
      for (int i = 0; i < hives.size(); i++)
         toReturn[i] = hives.get(i).getLetter(); 
      
      return toReturn;
   }   
   
   
   
   
   //Returns all the words in the wordlist (i.e. the words the player has
   //successfully entered already, displayed on the right-hand side of the
   //game window) as an ARRAY OF STRINGS
   public static String[] getWordList(){
      
      String[] toReturn = new String[enteredWords.size()];
      for (int i = 0; i < enteredWords.size(); i++)
         toReturn[i] = enteredWords.get(i);
      return toReturn;
   } 
   
   
   
   
   
   //Adds the argument word to the wordlist on the righthand side of the game 
   //window. The second argument is the number of points the added word is worth;
   //this function then updates the score and RETURNS the new score total as an int.
   //
   //This function will throw an EXCEPTION if given an invalid word.   
   public static int addToWordList(String word, int points){
      
      if (word == null)
         throw new IllegalArgumentException("Error! null String passed to addToWordList(...)!");
      else if (word.length() < GameLogic.MIN_WORD_LENGTH)
         throw new IllegalArgumentException("Error! Word too short to be added to WordList!: " + word);         
      else if (word.length() > GameLogic.MAX_WORD_LENGTH)
         throw new IllegalArgumentException("Error! Word too long to be added to WordList!: " + word);         
      
      word = charToUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
      if (enteredWords.contains(word))
         throw new IllegalArgumentException("Error! Word: \"" + word + "\" already in word list!");
      if (points < 0)
         throw new IllegalArgumentException("Error! points value <= 0 passed to addToWordList(...)!: " + points);
      
      enteredWords.add(word); 
      enteredWordsPoints.add(points); 
      GameGUI.score += points;
      wordAdded = true;
      return GameGUI.score;
   }  
   
   
   
   
   //Returns the player's current score
   public static int getPlayerScore(){
      return GameGUI.score;
   }   
   
   
   
   
   //Sets the Player's rank, displayed to the left of their score.
   //A player's rank changes as their score increases.
   //
   //This function will throw an EXCEPTION if given an invalid rank
   public static void setRank(String newRank){
      
      if (newRank == null)
         throw new IllegalArgumentException("Error! null String passed to setRank(...)!");
      else if (newRank.length() == 0)
         throw new IllegalArgumentException("Error! empty String passed to setRank(...)!" + rank);         
      else if (newRank.length() > MAX_RANK_LENGTH)
         throw new IllegalArgumentException("Error! too long String passed to setRank(...): " + rank);         
      
      GameGUI.rank = newRank;
   }  
   
   
   
   
   //Displays the argument error message in red text above the player text 
   //entry area.  This function DOES NOT wiggle the player text.
   public static void displayErrorMessage(String message){
      
      if (message == null)
         throw new IllegalArgumentException("Error! null String passed to displayErrorMessage(...)!");
      
      GameGUI.errorText = message;
      errorAlpha = 0;
      errorFade = SPEED_FADE_IN;                  
   }
   
   
   
   
   //Triggers "wiggle" animation (used for invalid or incompelete 
   //input) on the player's guess text   
   public static void wigglePlayerGuess(){
      
      wiggleCount = DEFAULT_WIGGLE_COUNT;
      wiggleSpeed = DEFAULT_WIGGLE_SPEED;      
      wiggleOffset = 0;
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
//************************************************************************************      
//**                                                                                **    
//**                                                                                **    
//**                                                                                **
//**                       ⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿                       **
//**                       ⣿⣿⣿⣿⣿⣿⣿⠟⢁⣴⠶⠶⠶⠶⠶⠶⠶⠶⠶⠶⣦⡈⠻⣿⣿⣿⣿⣿⣿⣿                       **
//**                       ⣿⣿⣿⣿⣿⠟⢁⣴⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣦⡈⠻⣿⣿⣿⣿⣿                       **
//**                       ⣿⣿⣿⠟⢁⣴⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣦⡈⠻⣿⣿⣿                       **
//**                       ⣿⡟⢁⣴⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣦⡈⢻⣿                       **
//**                       ⣿⡇⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⢸⣿                       **
//**                       ⣿⡇⢸⡇⣠⡾⠿⠶⠆⠶⢶⣶⡶⠶⢀⣴⡾⠿⣶⣄⠀⣶⡶⠶⣶⣄⢸⡇⢸⣿                       **
//**                       ⣿⡇⢸⡇⠻⢷⣶⣦⡄⠀⠀⣿⡇⠀⢸⣿⠀⠀⢸⣿⠀⣿⣧⣤⣾⡟⢸⡇⢸⣿                       **
//**                       ⣿⡇⢸⡇⢤⣤⣠⣽⡏⠀⠀⣿⡇⠀⠘⢿⣦⣠⣾⠟⠀⣿⡇⠀⠀⠀⢸⡇⢸⣿                       **
//**                       ⣿⡇⢸⡇⠀⠈⠉⠁⠀⠀⠀⠉⠀⠀⠀⠀⠈⠉⠀⠀⠀⠈⠁⠀⠀⠀⢸⡇⢸⣿                       **
//**                       ⣿⣧⡈⠻⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠟⢁⣼⣿                       **
//**                       ⣿⣿⣿⣦⡈⠻⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠟⢁⣴⣿⣿⣿                       **
//**                       ⣿⣿⣿⣿⣿⣦⡈⠻⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠟⢁⣴⣿⣿⣿⣿⣿                       **
//**                       ⣿⣿⣿⣿⣿⣿⣿⣦⡈⠻⠶⠶⠶⠶⠶⠶⠶⠶⠶⠶⠟⢁⣴⣿⣿⣿⣿⣿⣿⣿                       **
//**                       ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿                       **      
//**                                                                                **
//**    YOU DO NOT NEED TO TRACE, UNDERSTAND, OR USE ANY CODE BELOW THIS LINE!      **
//**   (but you're welcome and encouraged to take a look if you're interested!)     **
//**                                                                                **
//**                                                                                **        
//************************************************************************************    
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   //*************     Constants    **************    
   
   //Title displayed at the top of the window
   private static final String WINDOW_TITLE = "JavaBee";   
   //Background color of the game window
   private static final Color BACKGROUND_COLOR = Color.WHITE;
   //Frequency (in milliseconds) that the window checks to see if repaint is necessary,
   private static final int REPAINT_INTERVAL = 16;   
   
   
   //Dimensions of game window
   private static final int FRAME_WIDTH = 700;
   private static final int FRAME_HEIGHT = 600;    
   
   
   //Location attributes for center hive (other hive locations derived from center)
   private static final int CENTER_HIVE_X = 150;
   private static final int CENTER_HIVE_Y = 250;
   //Size of hives and spacing between hives
   private final static int HIVE_SIDE_LEN = 45;
   private final static int HIVE_PADDING = 5;
   private final static int HIVE_WIDTH = (int)((Math.sqrt(3)/2) * (HIVE_SIDE_LEN * 2));
   private final static int HIVE_X_OFFSET = (int)(HIVE_SIDE_LEN * 1.5) + HIVE_PADDING;
   private final static int HIVE_Y_OFFSET = HIVE_WIDTH + HIVE_PADDING;
   //Used to procedurally draw hives
   private final static int HIVE_SIDES = 6;
   private final static double THETA = (Math.PI*2) / 6.0;   
   //Colors for hives
   private static final Color HIVE_COLOR = new Color(230, 230, 230);
   private static final Color CENTER_HIVE_COLOR = new Color(240, 221, 57); 
   //Font attributes for hive letters
   private static final int HIVE_FONT_SIZE = 24;
   private static final Color HIVE_FONT_COLOR = Color.BLACK;   
   private static final Font HIVE_FONT = new Font("Arial", Font.BOLD, HIVE_FONT_SIZE);      
   
   
   //Location and kerning for Player Guess text
   private static final int PLAYER_PLAYER_GUESS_X = 80;
   private static final int PLAYER_PLAYER_GUESS_Y = 70;
   private static final int TEXT_PADDING = 2;
   //Font and color attributes for PLayer Guess Text
   private static final Color PLAYER_GUESS_COLOR_CENTER = new Color(240, 221, 57);
   private static final Color PLAYER_GUESS_COLOR_OUTER = new Color(0, 0, 0);
   private static final Color PLAYER_GUESS_COLOR_INVALID = new Color(200, 200, 200);
   private static final int PLAYER_GUESS_FONT_SIZE = 30;
   private static final Font PLAYER_GUESS_FONT = new Font("Arial", Font.BOLD, PLAYER_GUESS_FONT_SIZE); 
   //Size and blink frequency for cursor in Player Guess text area   
   private static final int CURSOR_BLINK_INTERVAL = 30;
   private static final int CURSOR_HEIGHT = 40;
   private static final int CURSOR_WIDTH = 3;
   private static final int CURSOR_Y_OFFSET = CURSOR_HEIGHT - 10;   
   
   
   //Size and location attributes for Delete/Enter buttons
   private static final int DELETE_X = 55;
   private static final int ENTER_X = 165;
   private static final int BUTTON_Y = 390;
   private static final int BUTTON_WIDTH = 80;
   private static final int BUTTON_HEIGHT = 45;
   private static final int BUTTON_ARC_W = 40;
   private static final int BUTTON_ARC_H = 40;
   //Font and color attributes for Delete/Enter buttons
   private static final Color BUTTON_OUTLINE_COLOR = new Color(220, 220, 220);      
   private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
   private static final int BUTTON_FONT_SIZE = 15;     
   private static final Font BUTTON_FONT = new Font("SanSerif", Font.PLAIN, BUTTON_FONT_SIZE);   
   //Text displayed on Delete/Enter buttons
   private static String DELETE_BUTTON_TEXT = "Delete";
   private static String ENTER_BUTTON_TEXT = "Enter";
   
   
   
   //Size and location attributes for Wordlist (which displays words player has entered)
   private static final int WORDLIST_X = 400;
   private static final int WORDLIST_Y = 150;
   private static final int WORDLIST_WIDTH = 250;
   private static final int WORDLIST_HEIGHT = 370;
   private static final int WORDLIST_BORDER_PADDING = 20;
   
   
   //Location attributes for score and rank text
   private static final int RANK_X = WORDLIST_X - WORDLIST_BORDER_PADDING/3;
   private static final int RANK_Y = WORDLIST_Y - 40;   
   private static final int SCORE_X = RANK_X + 110;  
   private static final int SCORE_Y = RANK_Y - WORDLIST_BORDER_PADDING ; 
   //Font and color attributes for score/rank text
   private static final int SCORE_CIRCLE_SIZE = 30; //background circle drawn behind score number     
   private static final Font RANK_FONT = new Font("SanSerif", Font.BOLD, 15);      
   private static final Font SCORE_FONT = new Font("SanSerif", Font.PLAIN, 12);   
   private static final Color SCORE_FONT_COLOR = Color.BLACK;
   //Max length for a player's displayed rank
   private static int MAX_RANK_LENGTH = 10;
   
   
   //Location attributes for Debug text (only shown when one or more debug toggles are enabled)
   private static final int DEBUG_TEXT_X = 10;
   private static final int DEBUG_TEXT_Y = 550;
   //Font and color attributes for the debug text 
   private static final int DEBUG_FONT_SIZE = 12;
   private static final Color DEBUG_FONT_COLOR = new Color(0, 124, 0);    
   private static final Font DEBUG_FONT = new Font("Arial", Font.BOLD, DEBUG_FONT_SIZE); 
   //Text to be displayed for various debug toggles
   private static final String DEBUGTXT_HARDCODE = "Hardcoded hives, ";       
   private static final String DEBUGTXT_LETTER = "All letters valid, ";       
   private static final String DEBUGTXT_WORD = "No dictionary verification, ";    
   
   
   //Location attributes for Error text 
   private static final int ERROR_X = 80;
   private static final int ERROR_Y = 30;   
   //Font and color attributes for the Error text
   private static final int ERROR_FONT_SIZE = 12;
   private static final int ERROR_R = 210;
   private static final int ERROR_G = 66;
   private static final int ERROR_B = 29;
   private static final Font ERROR_FONT = new Font("Arial", Font.BOLD, ERROR_FONT_SIZE); 
   //Controls speed at which error text fades in/out
   private static final int SPEED_FADE_OUT = -10;
   private static final int SPEED_FADE_IN = 10;   
   private static final int FADE_STOP = 600;  
   
   
   //Governs speed/distance of player text "wiggle" animation (used to indicate invalid input)
   private static final int WIGGLE_MAX = 10; //max pixels left or right a wiggle should move the text
   private static final int DEFAULT_WIGGLE_COUNT = 3; //number of "wiggles" that occur in the animation
   private static final int DEFAULT_WIGGLE_SPEED = 6;
   
   
   //Key to quit the game
   private static final int KEY_QUIT_GAME = KeyEvent.VK_ESCAPE;//escape key 
   
   //Set containing all alpha characters
   private static final HashSet<Character> ALL_LETTERS = new HashSet<Character>();
   
   
   
   
   
//*************     Class Variables    **************  
   
   
   //Instantion of a JavaBee GUI, used to map the mouse and key listeners
   //(kind of unintuitive, but necessary to keep everything non-OOP for the first project!)
   private static GameGUI canvas;
   //Window containing all GUI content
   private static JFrame window; 
   //Text area where entered words are displayed
   private static JTextArea wordList = new JTextArea(); 
   //Shapes for the enter/delete buttons (mainly used to determine if clicked on)
   private static RoundRectangle2D enterButton, deleteButton; 
   
   
   //Stores all the "hives" (both center and outer hives)
   private static ArrayList<Hive> hives;
   
   //Stores just the hive letters
   private static char yellowLetter;
   private static HashSet<Character> grayLetters = new HashSet<Character>();
   
   //The words in the wordlist and their respective point values; the two lists work in tandem
   private static ArrayList<String> enteredWords = new ArrayList<String>();
   private static ArrayList<Integer> enteredWordsPoints = new ArrayList<Integer>();
   
   //Stores text entered by player and currently being displayed in the player guess text field
   private static String playerGuess = "";
   //Indicates if a new word was just added to the wordlist (and triggers a repaint)
   private static boolean wordAdded = false;
   
   //Player's current score and rank
   private static int score = 0;
   private static String rank = "Placeholder";
   
   //Tracks the number of timer ticks elapsed since the game launched (used for animations)
   private static int ticksElapsed = 0;
   private static boolean cursorBlink = false;
   
   //Stores error text to be displayed and the state of its opacity (as it fades in/out)
   private static String errorText = "This is a placeholder!";
   private static int errorAlpha = 0;
   private static int errorFade = 0;
   
   
   //Description of what (if any) debug toggles are enabled
   private static String debugText = "";
   
   //Used to track the player text "wiggle" animation when triggered
   private static int wiggleCount = 0;
   private static int wiggleSpeed = 0;
   private static int wiggleOffset = 0;   
   
   
   
   
//Initializes and launches the game  window
   public static void launchGame(){  
      //Populate hashset with all alpha characters (used later)
      for (char ch = 'A'; ch <= 'Z'; ch++)
         ALL_LETTERS.add(ch);
      
      //Validate and normalize the randomized hive letters
      char[] letters = GameLogic.initializeGame();
      validateNormalizeLetters(letters);
      
      //initialize all the various GUI components
      initHives();
      initWindow();
      initButtons();
      initWordList();
      initDebugText();
      initTimer();

      //Run the GameLogic warmup code before launching the game
      GameLogic.warmup();      
   }        
   
   
   
   
   //Validates and normalizes characters to be displayed in the hives (provided as an char array)
   private static void validateNormalizeLetters(char[] letters){
      
      //Check for proper length
      if (letters == null)
         throw new IllegalStateException("ERROR! GameLogic's initialzeGame() returned 'null' for its hive letters array!");
      else if (letters.length != GameLogic.HIVE_COUNT)
         throw new IllegalStateException("ERROR! GameLogic's initialzeGame() returned an array of the wrong size!:" + Arrays.toString(letters));         
      //Check for duplicates and non-alpha characters
      HashSet<Character> dupeCheck = new HashSet<Character>();
      for (int i = 0; i < letters.length; i++){
         letters[i] = charToUpperCase(letters[i]);
         if (!ALL_LETTERS.contains(letters[i]))
            throw new IllegalStateException("ERROR! The array returned by GameLogic's initialzeGame() contains a non-letter!:" + Arrays.toString(letters));         
         else if(!dupeCheck.add(letters[i]))
            throw new IllegalStateException("ERROR! The array returned by GameLogic's initialzeGame() contains a duplicate!:" + Arrays.toString(letters));                              
         if (i == 0)
            yellowLetter = letters[i];
         else
            grayLetters.add(letters[i]);
      }
      //check for at least one vowel
      for (int i = 0; i < letters.length; i++){
         if (GameLogic.VOWEL_CHARS.indexOf(letters[i]) >= 0)
            return;
      }
      throw new IllegalStateException("ERROR! The array returned by GameLogic's initialzeGame() contains no vowel!:" + Arrays.toString(letters));                                          
   }
   
   
   
   //Initializes the "hives" (hexes that store valid letters for the game)
   private static void initHives(){
      hives = new ArrayList<Hive>();
      int x = CENTER_HIVE_X; 
      int y = CENTER_HIVE_Y;
      
      //initialize yellow center hex
      hives.add(new Hive(x, y, yellowLetter, CENTER_HIVE_COLOR));
      
      Iterator<Character> graysIterator = grayLetters.iterator();
      //initialize top 3 gray outer hives (left to right)
      hives.add(new Hive(x - HIVE_X_OFFSET, y - (HIVE_Y_OFFSET/2), graysIterator.next(), HIVE_COLOR));
      hives.add(new Hive(x, y - HIVE_Y_OFFSET, graysIterator.next(), HIVE_COLOR));
      hives.add(new Hive(x + HIVE_X_OFFSET, y - (HIVE_Y_OFFSET/2), graysIterator.next(), HIVE_COLOR));      
      //initialize bottom 3 gray outer hives (left to right)
      hives.add(new Hive(x - HIVE_X_OFFSET, y + (HIVE_Y_OFFSET/2), graysIterator.next(), HIVE_COLOR));
      hives.add(new Hive(x, y + HIVE_Y_OFFSET, graysIterator.next(), HIVE_COLOR));
      hives.add(new Hive(x + HIVE_X_OFFSET, y + (HIVE_Y_OFFSET/2), graysIterator.next(), HIVE_COLOR));       
   }
   
   
   
   //Initializes the game window
   private static void initWindow(){                
      window = new JFrame(WINDOW_TITLE);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setSize(FRAME_WIDTH, FRAME_HEIGHT);
      //Wordle object needed for mouse/keyboard listeners
      canvas = new GameGUI();
      window.add(canvas);
      
      //Fix Windows listener bug(?)
      canvas.setFocusable(true);
      //window.requestFocus();
      canvas.requestFocusInWindow();      
      canvas.setBackground(BACKGROUND_COLOR);
      window.getContentPane().setBackground(BACKGROUND_COLOR);      
      canvas.setOpaque(false);
      
      window.setVisible(true);
      window.setResizable(false);
      
      //Allows game to use mouse/keyPressed methods implemented in GameGUI.java
      canvas.addKeyListener(canvas);
      canvas.addMouseListener(canvas);  
   }
   
   
   
   //Initialize enter and delete buttons
   private static void initButtons(){
      
      enterButton = new RoundRectangle2D.Double(ENTER_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC_W, BUTTON_ARC_H);
      deleteButton = new RoundRectangle2D.Double(DELETE_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC_W, BUTTON_ARC_H);      
   }
   
   
   
   //Initialize word list area (where previously entered words are displayed)
   private static void initWordList(){
      wordList = new JTextArea("You have found 0 words.");
      JScrollPane scrollBox = new JScrollPane(wordList);
      scrollBox.setBorder(BorderFactory.createEmptyBorder());
      scrollBox.setBounds(WORDLIST_X, WORDLIST_Y, WORDLIST_WIDTH, WORDLIST_HEIGHT);
      wordList.setBounds(WORDLIST_X, WORDLIST_Y, WORDLIST_WIDTH, WORDLIST_HEIGHT);
      wordList.setFont(BUTTON_FONT);      
      wordList.setEditable(false);
      wordList.setFocusable(false);
      canvas.add(scrollBox);     
      
   }
   
   
   
   //Initialize debug text, displaying which debug toggles (if any) are enabled
   private static void initDebugText(){
      
      debugText = "";
      if (JavaBeeLauncher.DEBUG_USE_HARDCODED_HIVES)
         debugText += DEBUGTXT_HARDCODE;
      if (JavaBeeLauncher.DEBUG_ALL_LETTERS_VALID)
         debugText += DEBUGTXT_LETTER;
      if (JavaBeeLauncher.DEBUG_NO_DICT_VERIFY)
         debugText += DEBUGTXT_WORD;
      if (debugText.length() > 0)
         debugText = "DEBUG(S) ENABLED: " +debugText.substring(0, debugText.length() - 2);                     
   }
   
   
   
   //Initializes timer for game window.  Most repaints are event driven, but
   //timer is needed to repaint game window for certain animations
   //(such as fading text, wiggles, etc)
   private static void initTimer(){
      Timer timer = new Timer(0, canvas);
      timer.setDelay(REPAINT_INTERVAL);
      timer.start(); 
   }
   
   
   
   //Called everytime the game window is "repainted"
   public void paintComponent(Graphics g) {
      Graphics2D g2D = (Graphics2D)g;
      
      drawHives(g2D);
      drawPlayerGuess(g2D);
      drawButtons(g2D);
      drawWordList(g2D);
      drawScoreInfo(g2D);
      drawErrorMessage(g2D);
      drawDebugText(g2D);
   }        
   
   
   
   //Draws all the hives (both center and outer) on the game window, along with their letters
   private void drawHives(Graphics2D g){
      for (Hive h : hives){
         h.paint(g);
      }
   }
   
   
   
   //Draws the text field where the player enters letters
   private void drawPlayerGuess(Graphics2D g2D){
      
      g2D.setFont(PLAYER_GUESS_FONT);   
      int fontX = PLAYER_PLAYER_GUESS_X + wiggleOffset;
      int fontY = PLAYER_PLAYER_GUESS_Y;
      
      //draw each letter one at a time, so as to color letters accordingly
      for (int i = 0; i < playerGuess.length(); i++){
         char c = playerGuess.charAt(i);
         if (yellowLetter == c)
            g2D.setPaint(PLAYER_GUESS_COLOR_CENTER);
         else if (grayLetters.contains(c))
            g2D.setPaint(PLAYER_GUESS_COLOR_OUTER);  
         else 
            g2D.setPaint(PLAYER_GUESS_COLOR_INVALID); 
         
         //increment x coordinate per the width of the letter just drawn
         String letterStr = c + "";
         g2D.drawString(letterStr, fontX, fontY);
         Rectangle2D textBounds = g2D.getFontMetrics().getStringBounds(letterStr, g2D);
         fontX += textBounds.getWidth() + TEXT_PADDING;
      }
      
      //Draw the blinking cursor at the end of the player text
      if (cursorBlink){
         g2D.setPaint(PLAYER_GUESS_COLOR_CENTER);
         g2D.fill(new Rectangle2D.Double(fontX, fontY - CURSOR_Y_OFFSET, CURSOR_WIDTH, CURSOR_HEIGHT));
      }      
   }
   
   
   
   //Draws the enter and delete buttons to the game window
   private void drawButtons(Graphics2D g2D){
      
      drawButton(g2D, deleteButton, DELETE_BUTTON_TEXT);
      drawButton(g2D, enterButton, ENTER_BUTTON_TEXT);
   }
   
   
   
   //Draw a single button in the gmae window
   private void drawButton(Graphics2D g2D, RoundRectangle2D button, String label){
      
      //Draw the button shape...
      g2D.setPaint(BUTTON_OUTLINE_COLOR);
      g2D.draw(button);
      g2D.draw(deleteButton);
      
      //...then draw the text on the buttons
      g2D.setFont(BUTTON_FONT);
      g2D.setColor(BUTTON_TEXT_COLOR);
      
      //get dimensions of text to center it in the button
      Rectangle2D textDim = g2D.getFontMetrics().getStringBounds(label, g2D);
      int fontX = (int)(button.getCenterX() - (textDim.getWidth() / 2));
      int fontY = (int)(button.getCenterY() + (textDim.getHeight() / 3));         
      g2D.drawString(label, fontX, fontY);        
   }
   
   
   
   //Draws the word list, which stores all the words the player has previously entered (along with their point values)
   private void drawWordList(Graphics2D g2D){
      
      //Draw the border around wordlist area
      RoundRectangle2D border = new RoundRectangle2D.Double(WORDLIST_X - WORDLIST_BORDER_PADDING, 
                                                            WORDLIST_Y - WORDLIST_BORDER_PADDING, 
                                                            WORDLIST_WIDTH + 1.5 * WORDLIST_BORDER_PADDING, 
                                                            WORDLIST_HEIGHT + 1.5 * WORDLIST_BORDER_PADDING,
                                                            BUTTON_ARC_W, BUTTON_ARC_H);      
      g2D.setPaint(BUTTON_OUTLINE_COLOR);
      g2D.draw(border);
      
      //only update the wordlist if a new word was added since last repaint
      //(modifying the text area jumps the scrollbar to the bottom)
      if (!wordAdded)
         return;
      //Draw the number of words found (with proper plural!)
      StringBuilder wordListText = new StringBuilder("You have found " + enteredWords.size() + " word");
      if (enteredWords.size() != 1)
         wordListText.append("s\n\n");
      else
         wordListText.append("\n\n");
      //...then each word and its respective point value
      for (int i = 0; i < enteredWords.size(); i++)
         wordListText.append(enteredWords.get(i) + "  (" + enteredWordsPoints.get(i) + ")\n");
      wordList.setText(wordListText.toString());
      wordAdded = false; 
   }
   
   
   
   //Draws the player's score and rank to the game window
   public static void drawScoreInfo(Graphics2D g2D){
      
      //Draw circle background      
      g2D.setColor(CENTER_HIVE_COLOR); 
      Ellipse2D scoreBG = new Ellipse2D.Double(SCORE_X, SCORE_Y, SCORE_CIRCLE_SIZE, SCORE_CIRCLE_SIZE);
      g2D.fill(scoreBG);
      
      //Draw score in circle
      g2D.setColor(SCORE_FONT_COLOR);
      g2D.setFont(SCORE_FONT); 
      String scoreStr = score + "";
      Rectangle2D textDim = g2D.getFontMetrics().getStringBounds(scoreStr, g2D);
      int fontX = (int)(scoreBG.getCenterX() - (textDim.getWidth() / 2));
      int fontY = (int)(scoreBG.getCenterY() + (textDim.getHeight() / 3));       
      g2D.drawString(scoreStr, fontX, fontY);
      
      //Draw rank text
      g2D.setFont(RANK_FONT);
      textDim = g2D.getFontMetrics().getStringBounds(rank, g2D); 
      fontX = (int)(SCORE_X - textDim.getWidth() - WORDLIST_BORDER_PADDING);
      g2D.drawString(rank, fontX, RANK_Y);     
   }
   
   
   
   //Draws error message text, when one is being displayed
   public static void drawErrorMessage(Graphics g2D){
      //use the error alpha value (ie opacity) to deterimine if an error is currently being displayed
      if (errorAlpha <= 0)
         return;
      g2D.setFont(ERROR_FONT);
      g2D.setColor(new Color(ERROR_R, ERROR_G, ERROR_B, Math.min(255, errorAlpha))); 
      g2D.drawString(errorText, ERROR_X, ERROR_Y);       
   }
   
   
   
   //Draw debug text (shows which debug toggles are enabled, if any)
   private static void drawDebugText(Graphics g2D){
      //only draw text if at least one toggle is enabled
      if (debugText.length() == 0)
         return;
      
      g2D.setFont(DEBUG_FONT);
      g2D.setColor(DEBUG_FONT_COLOR); 
      g2D.drawString(debugText, DEBUG_TEXT_X, DEBUG_TEXT_Y);       
   }
   
   
   
   //Handles updating opacity of fading text when an error message is being displayed
   private static void handleFadingText(){
      errorAlpha += errorFade;        
      if (errorAlpha >= FADE_STOP){
         errorFade = SPEED_FADE_OUT;
      }
      else if (errorAlpha <= 0){
         errorFade = 0; 
         errorAlpha = 0;
      }
   }
   
   
   
   //Called when "wiggle" has been triggered; handles moving the player text area
   private static void handleWiggle(){
      wiggleOffset += wiggleSpeed;
      //If the wiggle has gone too far right/left, starting wiggling the opposite direction
      if (wiggleOffset > WIGGLE_MAX)            
         wiggleSpeed *= -1;
      else if(wiggleOffset < -WIGGLE_MAX){
         wiggleSpeed *= -1;
         wiggleCount--;
      }
      //once a wiggle has gone through enough rotations, stop wiggling
      if (wiggleCount <= 0 && wiggleOffset >= 0){
         wiggleSpeed = 0;
         wiggleOffset = 0;
      }
   }   
   
   
   
   //Called automatically whenever a key on the keyboard is pressed   
   public void keyPressed(KeyEvent event) { 
      //Quit the game
      if (event.getKeyCode()  == KEY_QUIT_GAME)
         System.exit(0); 
      char keyChar = charToUpperCase(event.getKeyChar());
      //Forward the pressed key on to GameLogic, so long as it is an alpha or Backspace/Enter key
      if (ALL_LETTERS.contains(keyChar) || keyChar == GameLogic.BACKSPACE_KEY || keyChar == GameLogic.ENTER_KEY){
         GameLogic.reactToKey(keyChar);      
         repaint();
      }
   }
   
   
   
   //Called automatically whenever the mouse is clicked inside the game window
   public void mousePressed(MouseEvent event) { 
      //Get the mouse click location
      int clickX = event.getX();
      int clickY = event.getY(); 
      
      //if buttons were clicked...
      if (enterButton.contains(clickX, clickY)){
         GameLogic.reactToKey(GameLogic.ENTER_KEY);
      }
      else if(deleteButton.contains(clickX, clickY)){
         GameLogic.reactToKey(GameLogic.BACKSPACE_KEY);
      }
      //if a hive was clicked...
      else{
         for (Hive h : hives){
            if (h.wasClicked(clickX, clickY)){
               GameLogic.reactToKey(h.getLetter());         
               break;
            }
         }
      }
      repaint();
   }    
   
   
   
   //Gets called everytime the timer "ticks"
   //Updates any in-progress animations and repaints window if necessary
   public void actionPerformed(ActionEvent ae){
      ticksElapsed++; //track total "ticks" 
      boolean repaint = false; //if anything is animating, trigger a repaint at the end
      
      //Determine if cursor needs to "blink" (become visible/invisible)
      if (ticksElapsed % CURSOR_BLINK_INTERVAL == 0){
         cursorBlink = !cursorBlink;
         repaint = true;
      }
      //...if an error message is fading
      if (errorFade != 0){
         handleFadingText();
         repaint = true;
      }
      //...if the player text is in the process of wiggling
      if (wiggleSpeed != 0){
         handleWiggle();
         repaint = true;                                 
      }
      if (repaint)
         repaint();
   }   
   
   
   
   //converts a character to upper case
   //if char argument provided is not a letter, returns -1;
   private static char charToUpperCase(char ch){              
      return ((ch + "").toUpperCase()).charAt(0);
   }   
   
   
   
   
   
   
   
   
   //Inner Hive class, representing a single "hive" in the game window
   static class Hive extends JComponent{
      
      private char letter;      //letter inside the hive
      private Color hiveColor;  //background color of hive
      private Polygon hivePoly; //
      private int x, y;         //coordinate of centerpoint of hive
      
      public Hive(int x, int  y, char letter, Color c){
         this.x = x;
         this.y = y;
         this.letter = charToUpperCase(letter);
         this.hiveColor = c;
         this.hivePoly = new Polygon();
         //calculate the points of the corners of hex (some trig is happenning...)
         for(int i = 0; i < HIVE_SIDES; i++ ) {
            int tempX = (int) (x + HIVE_SIDE_LEN * Math.cos(THETA*i));
            int tempY = (int) (y + HIVE_SIDE_LEN * Math.sin(THETA*i));
            hivePoly.addPoint(tempX, tempY);
         }                           
      }
      
      
      //Determines if a mouse click at argument x and y has clicked on this hive
      public boolean wasClicked(int x, int y){
         return hivePoly.contains(x, y);
      }
      
      //draws the hive (with its letter) in the game window
      public void paint(Graphics g){
         //first draw the hex...
         g.setColor(this.hiveColor);
         g.fillPolygon(this.hivePoly);
         //...then draw the letter inside
         Graphics2D g2D = (Graphics2D) g;
         g2D.setFont(HIVE_FONT);
         g2D.setColor(HIVE_FONT_COLOR);
         
         String letterStr = letter + "";
         //get dimensions of letter to center it in the hive
         Rectangle2D textDim = g2D.getFontMetrics().getStringBounds(letterStr, g);
         int letterX = (int)(this.x - (textDim.getWidth() / 2));
         int letterY = (int)(this.y + (textDim.getHeight() / 3));         
         g2D.drawString(letterStr, letterX, letterY);         
      }  
      
      
      //****  Accessors  ****
      public Polygon getPolygon(){
         return this.hivePoly;
      }
      
      public Color getColor(){
         return this.hiveColor;
      }
      
      public char getLetter(){
         return this.letter;
      }               
   }

  
   
   
   //These functions are required by various interfaces, but are not used
   public void mouseReleased(MouseEvent event) { }
   
   public void mouseClicked(MouseEvent event) { }
   
   public void mouseEntered(MouseEvent event) { }
   
   public void mouseExited(MouseEvent event) { }
   
   public void mouseMoved(MouseEvent event) { }
   
   public void keyReleased(KeyEvent event) { }
   
   public void keyTyped(KeyEvent event) { }     
}
