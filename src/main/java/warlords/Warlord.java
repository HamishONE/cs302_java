package warlords;

import warlordstest.IWarlord;

/**
 * Represents a single instance of a warlord in the game.
 * Also stores whether that player has won the game.
 */
public class Warlord extends GameObject implements IWarlord {

	private boolean hasWon = false;
	private int health = 1;

	/**
	 * Create a new Warlord at the given position with the given sprite
	 * @param x the x position on the screen
	 * @param y the y position on the screen
	 * @param age the age to base the sprite on
	 */
	public Warlord(int x, int y, Ages age) {
		super(x, y, age, "build/resources/main/CatScream.mp3", Math.PI/2);
		this.width = 60;
		this.height = 85;

		spritePaths.put(Ages.NEOLITHIC, "/cavemanBlue.png");
		spritePaths.put(Ages.MEDIEVAL, "/knightBlue.png");
		spritePaths.put(Ages.INDUSTRIAL, null);
		spritePaths.put(Ages.SPACE, null);
	}

	@Override
	public boolean isDead() {
		return health <= 0;
	}

	@Override
	public boolean hasWon() {
		return hasWon;
	}

	/**
	 * Causes damage to the warlord.
	 *
	 * @param damage the amount of damage to be dealt
	 */
	public void causeDamage(int damage) {
		health -= damage;
	}
	/**
	 * Sets the current warlord as the winning character
	 */
	public void setAsWinner() {
		hasWon = true;
	}
}
