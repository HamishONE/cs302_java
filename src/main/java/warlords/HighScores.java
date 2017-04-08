package warlords;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages the storage of the high scores list.
 */
public class HighScores {

	static final private String dataFile = "high_scores.dat";

	private ArrayList<Score> scores;

	/**
	 * Load data from the file.
	 */
	@SuppressWarnings("unchecked")
	public void loadData() {

		try {
			FileInputStream fin = new FileInputStream(dataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			scores = (ArrayList<Score>) ois.readObject();
			ois.close();
		}
		catch (FileNotFoundException e) {
			scores = new ArrayList<>();
		}
		catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Add a new score to the list.
	 * @param name See {@link Score#getName}.
	 * @param scoreValue See {@link Score#getScoreValue()}.
	 */
	public void addScore(String name, int scoreValue) {

		// Add the new score
		Score score = new Score(name, scoreValue);
		scores.add(score);

		// Sort the scores and truncate the number to 10
		Collections.sort(scores);
		scores.removeIf(score1 -> scores.indexOf(score1) > 9);

		// Update the file on disk
		saveToFile();
	}

	/**
	 * @return A sorted list of the top ten scores.
	 */
	public ArrayList<Score> getScores() {
		if (scores == null) {
			throw new RuntimeException("The scores list has not been initialised.");
		}
		return scores;
	}

	/**
	 * Save the serialized list to a file.
	 */
	private void saveToFile() {
		try {
			FileOutputStream fos = new FileOutputStream(dataFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(scores);
			oos.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
