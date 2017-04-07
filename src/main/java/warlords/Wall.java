package warlords;

import warlordstest.IWall;

/**
 * Represents a single instance of a wall/brick in the game.
 */
public class Wall extends GameObject implements IWall {

	private int health = 1;
	private int owner;


	/**
	 * Create a new wall at a given position with a given owner
	 * @param x the x position of the wall
	 * @param y the y position of the wall
	 * @param theta the rotation of the wall
	 * @param owner the index number of the player who's wall this is
	 */
	public Wall(int x, int y, double theta, int owner, Ages age) {
		super(x, y, age, null, theta);
		width = 44.6;
		height = 20.9;
		this.owner = owner;
		spritePaths.put(Ages.NEOLITHIC, "/cavemanWall.png");
		spritePaths.put(Ages.MEDIEVAL, "/wal.png");
		spritePaths.put(Ages.INDUSTRIAL, null);
		spritePaths.put(Ages.SPACE, null);
	}

	public Wall(int x, int y, double theta, int owner, Ages age, PowerUp powerUp) {
		super(x, y, age, null, theta);
		width = 44.6;
		height = 20.9;
		this.owner = owner;
		this.powerUp = powerUp;
		spritePaths.put(Ages.NEOLITHIC, "/cavemanWall.png");
		spritePaths.put(Ages.MEDIEVAL, "/wal.png");
		spritePaths.put(Ages.INDUSTRIAL, null);
		spritePaths.put(Ages.SPACE, null);
	}

	@Override
	public boolean isDestroyed() {
		return health <= 0;
	}

	/**
	 * Reduce the health of the wall potentially destroying it.
	 * @param damage the amount to reduce health by
	 */
	public void causeDamage(int damage) {
		health -= damage;

	}

	/**
	 * Get the owner of the wall.
	 * @return the index number of the wall's owner
	 */
	public int getOwner() {
		return owner;
	}
}
