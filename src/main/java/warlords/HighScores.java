package warlords;

import java.util.ArrayList;

/**
 * Manages the storage of the high scores list.
 */
public class HighScores {

	private DatabaseCommunications dbComs = new DatabaseCommunications();

	/**
	 * Add a new score to the list.
	 * @param name See {@link Score#getName}.
	 * @param scoreValue See {@link Score#getScoreValue()}.
	 */
	public void addScore(String name, int scoreValue) {
		dbComs.putValues(name, scoreValue);
	}

	/**
	 * @return A sorted list of the top ten scores.
	 */
	public ArrayList<Score> getScores() {
		return dbComs.getValues();
	}

	/**
	 * Determine if a score is large enough to be added to the scoreboard.
	 * @param scoreValue The numeric score value to be tested.
	 * @return If the score is large enough.
	 */
	public boolean isTopTenScore(int scoreValue) {
		ArrayList<Score> scores = dbComs.getValues();
		if (scores.size() < 10) {
			return true;
		}
		else {
			int lowestScore = scores.get(scores.size() - 1).getScoreValue();
			return scoreValue > lowestScore;
		}
	}
}
