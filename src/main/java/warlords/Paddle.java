package warlords;

import warlordstest.IPaddle;

public class Paddle extends GameObject implements IPaddle {

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
