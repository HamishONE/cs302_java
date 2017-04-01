package warlords;

/**
 * Model class to store the current state, window dimensions and number of game players
 */
public class Game {

	/**
	 * Enum to keep track of the current state
	 * Can be IDLE, MENU, GAME or FINISHED
	 */
	public enum State {
		IDLE,
		MENU,
		GAME,
		FINISHED
	}

	private State state;
	private int numHumanPlayers = 1;
	private int width;
	private int height;

	/**
	 * Create a new instance of and initialize state to IDLE
	 * @param width width of the game window
	 * @param height height of the game window
	 */
	public Game(int width, int height) {
		this.width = width;
		this.height = height;
		this.state = State.IDLE;
	}

	/**
	 * Create a new instance and set the state as provided
	 * @param width width of the game window
	 * @param height height of the game window
	 * @param state the state to initialize to
	 */
	public Game(int width, int height, State state) {
		this.width = width;
		this.height = height;
		this.state = state;
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

	/**
	 * Gets current state
	 *
	 * @return state of game
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets state of game
	 *
	 * @param state state to set the game to
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return The number of human players for the game.
	 */
	public int getNumHumanPlayers() {
		return numHumanPlayers;
	}

	/**
	 * Sets the number of human players for the game.
	 * @param numHumanPlayers the new number of human players
	 */
	public void setNumHumanPlayers(int numHumanPlayers) {
		this.numHumanPlayers = numHumanPlayers;
	}
}
