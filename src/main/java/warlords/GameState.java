package warlords;


public class GameState {

	public enum State {
		IDLE,
		MENU,
		GAME,
		FINISHED
	}

	private State state;

	public GameState() {
		this.state = State.IDLE;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
