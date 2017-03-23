package warlords;

import warlordstest.IPaddle;

public class Paddle extends GameObject implements IPaddle {

	@Override
	public void tick() {

	}

	public Paddle(int x, int y) {
		setXPos(x);
		setYPos(y);
	}

	public void moveLeft() {
		x -= 1;
	}

	public void moveRight() {
		x += 1;
	}
}
