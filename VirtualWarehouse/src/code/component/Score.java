package code.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import code.hud.ScoringTimer;

/**
 * Keeps track of certain scoring objectives.
 * 
 * @author Virtual Warehouse Team (Jordan Hinshaw, Matt Kent, Aaron Ramsey)
 */
public class Score {

	public static String HIGH_SCORE_FILE = "highscores.dat";
	public static int HIGH_SCORES_TO_KEEP = 10;

	public static String CHECKSUM_STRING = "virtual warehouse!";
	
	private String name;
	
	private ScoringTimer timer;
	
	//total distance traveled
	private float distanceTraveled = 0;
	
	//number of boxes picked correctly 
	private int boxesPickedCorrect = 0;
	
	//number of picking errors committed
	private int boxesPickedWrong = 0;
	
	/**
	 * Constructor instantiates a new object for keeping track of objectives
	 * used to score the players progress. 
	 */
	public Score(ScoringTimer timer) {
		this.timer = timer;
	}
	
	/**
	 * Returns true if the player's score is high enough to make the high score table.
	 * (somewhat hastily thrown together)
	 * @return
	 */
	public boolean checkForHighScore() {
		
		try {
		
			float currentScore = timer.getTimeInSeconds();
			boolean newHighScore = false;
			
			File file = new File(HIGH_SCORE_FILE);
			if (!file.exists() || file.length() == 0)
			{
				newHighScore = true;
			}
			else
			{
				Scanner sc = new Scanner(file);
				int counter = 0;
				while (sc.hasNextLine())
				{
					String[] line = sc.nextLine().split(",");
					String score = line[1];
					
					if (currentScore <= Float.parseFloat(score))
					{
						newHighScore = true;
					}
					
					counter++;
				}
				
				if (counter < HIGH_SCORES_TO_KEEP)
				{
					newHighScore = true;
				}
				
				sc.close();
			}
			
			return newHighScore;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds the high score data to the high score file if applicable.
	 * (somewhat hastily thrown together)
	 */
	public void saveScoreData() {
		try {
			
			//get the values to add to the table
			//TODO: make sure these are all grabbed dynamically
			String currentName = name;
			if (currentName == null || currentName.equals(""))
			{
				currentName = "User";
			}
			float currentScore = timer.getTimeInSeconds();
			int currentScore2 = getBoxesPickedWrong();
			//use a checksum to prevent cheating
			String currentChecksum = getChecksum(currentName+currentScore+currentScore2);
			
			//read the scores into memory
			String[][] scoreData = new String[10][4];
			int counter = 0;
			
			File file = new File(HIGH_SCORE_FILE);
			if (!file.exists() || file.length() == 0)
			{
				file.createNewFile();				
				storeData(scoreData, counter, currentName, Float.toString(currentScore), Integer.toString(currentScore2), currentChecksum);
				counter++;
			}
			else
			{
				Scanner sc = new Scanner(file);
				boolean inserted = false;
				
				while (sc.hasNextLine() && counter < HIGH_SCORES_TO_KEEP)
				{
					String[] line = sc.nextLine().split(",");
					
					String name = line[0];
					String score = line[1];
					String score2 = line[2];
					String checksum = line[3];

					//insert the score into the array if it's the right spot
					if (currentScore <= Float.parseFloat(score) && inserted == false)
					{
						storeData(scoreData, counter, currentName, Float.toString(currentScore), Integer.toString(currentScore2), currentChecksum);
						counter++;
						inserted = true;
					}
					//make sure checksum is right to prevent cheating
					else if (checksum.equals(getChecksum(name+score+score2)))
					{
						storeData(scoreData, counter, name, score, score2, checksum);
						counter++;
					}
				}
				
				if (inserted == false && counter < HIGH_SCORES_TO_KEEP)
				{
					storeData(scoreData, counter, currentName, Float.toString(currentScore), Integer.toString(currentScore2), currentChecksum);
					counter++;
				}
				
				sc.close();
			}
			
			//write the data to file
			PrintWriter fileOutput = new PrintWriter(new FileWriter(HIGH_SCORE_FILE));
			
			for (int i=0; i<counter; i++)
			{
				String line = scoreData[i][0] + "," + scoreData[i][1] + "," + scoreData[i][2] + "," + scoreData[i][3];
				fileOutput.println(line);
			}
			
			fileOutput.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Helper function for saveScoreData()
	 */
	private void storeData(String[][] scoreData, int count, String name, String time, String errors, String checksum) {
		scoreData[count][0] = name;
		scoreData[count][1] = time;
		scoreData[count][2] = errors;
		scoreData[count][3] = checksum;
	}
	
	/**
	 * Creates a checksum for a string.
	 */
	private String getChecksum(String s) {
		String currentChecksum = "";
		try {
			MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			md5.update((s+CHECKSUM_STRING).getBytes());
		    currentChecksum = new BigInteger(1,md5.digest()).toString(16);
		    
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return currentChecksum;
	}
	
	/**
	 * Increases the record of total distance traveled.
	 * @param dist The distance to be added.
	 */
	public void addDistance(float dist) {
		distanceTraveled += dist;
	}
	
	public float getDistance() {
		return distanceTraveled;
	}
	
	public int getBoxesPickedCorrect() {
		return boxesPickedCorrect;
	}

	public void incrementBoxesPickedCorrect() {
		boxesPickedCorrect++;
	}

	public int getBoxesPickedWrong() {
		return boxesPickedWrong;
	}

	public void incrementBoxesPickedWrong() {
		boxesPickedWrong++;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String[] args) {
		Score s = new Score(new ScoringTimer());
		System.out.println(s.checkForHighScore());
		s.saveScoreData();
	}
	
}
