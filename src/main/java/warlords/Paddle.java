package warlords;

import warlordstest.IPaddle;

import static java.lang.Math.*;

public class Paddle extends GameObject implements IPaddle {
	private Game game;
	private Double theta = 0.0;
	private int x_init;
	private int y_init;

	private double ANGLE_DIFF = PI/150;

	public Paddle(int x, int y, Game game) {
		super(x, y);
		x_init = x;
		y_init = y;
		this.game = game;
	}

	@Override
	public void tick() {
	}

	public void moveLeft() {
		theta += ANGLE_DIFF;
		setPosition();
	}

	public void moveRight() {
		theta -= ANGLE_DIFF;
		setPosition();
	}

	private void setPosition() {
		x = (int)((5.0/18.0)*game.getWidth()*cos(theta));
		y = (int)((5.0/12.0)*game.getHeight()*sin(theta));
	}
}
