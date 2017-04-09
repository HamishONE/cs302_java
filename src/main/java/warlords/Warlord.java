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
	 * @param index the index from 1-3 of the player, used to determine the sprite colour
	 */
	public Warlord(int x, int y, Ages age, int index) {
		super(x, y, age, "build/resources/main/CatScream.mp3", Math.PI/2);
		this.width = 60;
		this.height = 85;

		switch(index) {
			case 0:
				spritePaths.put(Ages.NEOLITHIC, "/cavemanBlue.png");
				spritePaths.put(Ages.MEDIEVAL, "/knightBlue.png");
				spritePaths.put(Ages.INDUSTRIAL, "/minerBlue.png");
				spritePaths.put(Ages.SPACE, null);
				break;
			case 1:
				spritePaths.put(Ages.NEOLITHIC, "/cavemanGreen.png");
				spritePaths.put(Ages.MEDIEVAL, "/knightGreen.png");
				spritePaths.put(Ages.INDUSTRIAL, "/minerGreen.png");
				spritePaths.put(Ages.SPACE, null);
				break;
			case 2:
				spritePaths.put(Ages.NEOLITHIC, "/cavemanRed.png");
				spritePaths.put(Ages.MEDIEVAL, "/knightRed.png");
				spritePaths.put(Ages.INDUSTRIAL, "/minerRed.png");
				spritePaths.put(Ages.SPACE, null);
				break;
			case 3:
				spritePaths.put(Ages.NEOLITHIC, "/cavemanYellow.png");
				spritePaths.put(Ages.MEDIEVAL, "/knightYellow.png");
				spritePaths.put(Ages.INDUSTRIAL, "/minerYellow.png");
				spritePaths.put(Ages.SPACE, null);
				break;
		}
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
