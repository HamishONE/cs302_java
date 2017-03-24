package warlords;

import warlordstest.IPaddle;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;

public class Paddle extends GameObject implements IPaddle {
	private Game game;
	private Double theta;
	private Double theta_init;
	private int x_init;
	private int y_init;

	private double ANGLE_DIFF = PI/150;

	public Paddle(int x, int y, Double theta, Game game) {
		super(x, y, null);
		this.width = 100;
		this.height = 20;
		x_init = x;
		y_init = y;
		theta_init = theta;
		this.game = game;
		this.theta = theta + PI/4;
		setPosition();
	}

	public Double getAngle() {
		return theta;
	}

	@Override
	public void tick() {
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
		if (theta < theta_init+ PI/2) {
			theta += ANGLE_DIFF;
			setPosition();
		}
	}

	private void moveCCW() {
		if (theta > theta_init) {
			theta -= ANGLE_DIFF;
			setPosition();
		}
	}

	private void setPosition() {
		x = (int)((5.0/18.0)*game.getWidth()*cos(theta))+x_init;
		y = (int)((5.0/12.0)*game.getHeight()*sin(theta))+y_init;
	}

	@Override
	public Double getRotation() {
		return theta - PI/2;
	}

	public Shape getRectangle() {

		Shape shape = new Rectangle2D.Double(x - width/2, y - height/2, width, height);
		AffineTransform at = AffineTransform.getRotateInstance(theta - PI/2, x, y);
		shape = at.createTransformedShape(shape);
		return shape;
	}
}
