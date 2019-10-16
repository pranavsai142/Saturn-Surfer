
import java.util.*;
import java.io.*;

public class HighscoreManager {
	private ArrayList<Score> scores;

	// The name of the file where the highscores will be saved
	private static final String HIGHSCORE_FILE = "scores.bin";

	//Initializing an inputStream and outputStream for working with the file
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;

	public HighscoreManager() {
		//initializing the scores-arraylist
		scores = new ArrayList<Score>();
	}

	public ArrayList<Score> getScores() {
		loadScoreFile();
		sort();
		return scores;
	}
	//sorts scores from greatest to lowest
	private void sort() {
		ScoreComparator comparator = new ScoreComparator();
		Collections.sort(scores, comparator);
	}
	//add scores to file
	public void addScore(String myname, int totalPoints) {
		loadScoreFile();
		scores.add(new Score(myname, totalPoints));
		updateScoreFile();
	}
	//read scores from file
	public void loadScoreFile() {
		try {
			inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
			scores = (ArrayList<Score>) inputStream.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("[Load] FNF Error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("[Load] IO Error: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("[Load] CNF Error: " + e.getMessage());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Load] IO Error: " + e.getMessage());
			}
		}
	}
	//write new score to file
	public void updateScoreFile() {
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
			outputStream.writeObject(scores);
		} catch (FileNotFoundException e) {
			System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
		} catch (IOException e) {
			System.out.println("[Update] IO Error: " + e.getMessage());
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				System.out.println("[Update] Error: " + e.getMessage());
			}
		}
	}
	//toString method essentially
	public String getHighscoreString() {
		String highscoreString = "";
		int max = 10;

		ArrayList<Score> scores;
		scores = getScores();

		int i = 0;
		int x = scores.size();
		if (x > max) {
			x = max;
		}
		while (i < x) {
			highscoreString += (i + 1) + ".\t"  + "    " +  scores.get(i).getName() + "\t\t" +  "    " + scores.get(i).getPoints() + "\n";
			i++;
		}
		return highscoreString;
	}
}