package warlords;

import warlordstest.IGame;

public class MainController implements IGame {

	private Ball ball;

	public Ball getBall() {
		return ball;
	}

	public void beginGame() {
		ball = new Ball();
	}

	@Override
	public void tick() {
		ball.tick();
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void setTimeRemaining(int seconds) {

	}
}
