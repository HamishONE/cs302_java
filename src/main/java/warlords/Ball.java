package warlords;

import warlordstest.IBall;

public class Ball extends GameObject implements IBall {

	private int dX = 0;
	private int dY = 0;

	public Ball(int x, int y) {
		super(x, y);
	}

	@Override
	public void tick() {
		x += dX;
		y += dY;
	}

	@Override
	public void setXVelocity(int dX) {
		this.dX = dX;
	}

	@Override
	public void setYVelocity(int dY) {
		this.dY = dY;
	}

	@Override
	public int getXVelocity() {
		return dX;
	}

	@Override
	public int getYVelocity() {
		return dY;
	}
}
