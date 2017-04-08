package warlords;

import java.io.Serializable;

/**
 * Data class representing a single name and score value pair for the high score board.
 */
public class Score implements Comparable<Score>, Serializable {

	private String name;
	private int scoreValue;

	/**
	 * Create a new Score instance.
	 * @param name The name of the player who holds the score.
	 * @param scoreValue The numeric score.
	 */
	public Score(String name, int scoreValue) {
		this.name = name;
		this.scoreValue = scoreValue;
	}

	/**
	 * @return The name of the player who holds the score.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The numeric score.
	 */
	public int getScoreValue() {
		return scoreValue;
	}

	@Override
	public int compareTo(Score o) {
		return o.scoreValue - this.scoreValue;
	}
}
