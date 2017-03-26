package warlords;

import warlordstest.IPaddle;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;

public class Paddle extends GameObject implements IPaddle {
	private Game game;
	private Double theta_init;
	private int x_init;
	private int y_init;

	private double ANGLE_DIFF = PI/150;

	public Paddle(int x, int y, Double theta, Game game) {
		super(x, y, "/paddle.png", theta + PI/4);
		this.width = 100;
		this.height = 20;
		x_init = x;
		y_init = y;
		theta_init = theta;
		this.game = game;
		setPosition();
	}

	public void moveLeft() {
		if (y_init < game.getWidth()/2) {
			moveCW();
		} else {
			moveCCW();
		}
	}

	public void moveRight() {
		if (y_init > game.getWidth()/2) {
			moveCW();
		} else {
			moveCCW();
		}
	}

	private void moveCW() {
		if (rotationAngle < theta_init+ PI/2) {
			rotationAngle += ANGLE_DIFF;
			setPosition();
		}
	}

	private void moveCCW() {
		if (rotationAngle > theta_init) {
			rotationAngle -= ANGLE_DIFF;
			setPosition();
		}
	}

	private void setPosition() {
		x = (5.0/18.0)*game.getWidth()*cos(rotationAngle) + x_init;
		y = (5.0/12.0)*game.getHeight()*sin(rotationAngle) + y_init;
	}
}
