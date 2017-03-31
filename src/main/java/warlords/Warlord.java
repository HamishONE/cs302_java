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
	 * @param imgPath the path to the sprite image file
	 */
	public Warlord(int x, int y, String imgPath) {
		super(x, y, imgPath, Math.PI/2);
		this.width = 60;
		this.height = 85;
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
