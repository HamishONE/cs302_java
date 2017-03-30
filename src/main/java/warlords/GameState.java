package warlords;

/**
 * Class to hold enum and handle tracking of the current state of the game
 */
public class GameState {

	/**
	 * Enum to keep track of the state of the game
	 * Can be IDLE, MENU, GAME or FINISHED
	 */
	public enum State {
		IDLE,
		MENU,
		GAME,
		FINISHED
	}

	private State state;

	/**
	 * Constructor which initializes state to IDLE
	 */
	public GameState() {
		this.state = State.IDLE;
	}

	/**
	 * Constructor which initializes state with selected
	 *
	 * @param state the state to initialize to
	 */
	public GameState(State state) {
		this.state = state;
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

}
