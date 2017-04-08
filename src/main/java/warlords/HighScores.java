package warlords;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages the storage of the high scores list.
 */
public class HighScores {

	private ArrayList<Score> scores = new ArrayList<>();

	/**
	 * Load data (fake for now, in future might be from file or web service).
	 */
	public void loadData() {
		addScore("Hamish O'Neill", 100);
		addScore("Roman Amor", 105);
		addScore("John McGoose", 99);
	}

	/**
	 * Add a new score to the list.
	 * @param name See {@link Score#getName}.
	 * @param scoreValue See {@link Score#getScoreValue()}.
	 */
	public void addScore(String name, int scoreValue) {
		Score score = new Score(name, scoreValue);
		scores.add(score);
		Collections.sort(scores);
	}

	/**
	 * @return A sorted list of the top ten scores.
	 */
	public ArrayList<Score> getScores() {
		return scores; //TODO: only return the top ten scores
	}
}
