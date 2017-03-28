package warlords;

/**
 * Model class to store data about the game window
 */
public class Game {

	private int width;
	private int height;

	/**
	 * Create a new instance of the game
	 * @param width width of the game window
	 * @param height height of the game window
	 */
	public Game(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the width of the game window
	 * @return the game window width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the game window
	 * @return the game window height
	 */
	public int getHeight() {
		return height;
	}
}
