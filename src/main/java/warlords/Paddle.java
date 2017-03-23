package warlords;

import warlordstest.IPaddle;

public class Paddle extends GameObject implements IPaddle {

	public Paddle(int x, int y) {
		super(x, y);
	}

	@Override
	public void tick() {

	}

	public void moveLeft() {
		x -= 1;
	}

	public void moveRight() {
		x += 1;
	}
}
