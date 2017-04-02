package warlords;

import warlordstest.IPaddle;
import static java.lang.Math.*;

/**
 * Represents a single instance of a paddle in the game.
 */
public class Paddle extends GameObject implements IPaddle {

	private Game game;
	private Double theta_init;
	private int x_init;
	private int y_init;
	private double ANGLE_DIFF = PI/150;

	/**
	 * Create a new paddle instance at a given position
	 * @param x the x position of the paddle
	 * @param y the y position of the paddle
	 * @param theta the lower bound for the rotation of the paddle
	 * @param game the game model used to get the dimensions of the game screen
	 */
	public Paddle(int x, int y, Double theta, Game game) {
		super(x, y, "/paddle.png", null, theta + PI/4);
		this.width = 100;
		this.height = 20;
		x_init = x;
		y_init = y;
		theta_init = theta;
		this.game = game;
		setPosition();
	}

	/**
	 * @return The initial angle provided in the paddle constructor.
	 */
	public double getStartingRotation() {
		return theta_init;
	}

	/**
	 * Set the rotation of the paddle
	 * @param theta the rotation in radians
	 */
	public void setRotation(double theta) {
		rotationAngle = theta;
	}

	/**
	 * Move the paddle in a arc to the left (from the user's perspective).
	 */
	public void moveLeft() {
		if (y_init < game.getWidth()/2) {
			moveCW();
		} else {
			moveCCW();
		}
	}

	/**
	 * Move the paddle in a arc to the right (from the user's perspective).
	 */
	public void moveRight() {
		if (y_init > game.getWidth()/2) {
			moveCW();
		} else {
			moveCCW();
		}
	}

	/**
	 * Move the paddle one increment in a clockwise arc.
	 */
	private void moveCW() {
		if (rotationAngle < theta_init+ PI/2) {
			rotationAngle += ANGLE_DIFF;
			setPosition();
		}
	}

	/**
	 * Move the paddle one increment in a counterclockwise arc.
	 */
	private void moveCCW() {
		if (rotationAngle > theta_init) {
			rotationAngle -= ANGLE_DIFF;
			setPosition();
		}
	}

	/**
	 * Set the position of the paddle to sit on a circular arc based on it's rotation.
	 * Generates the circular arc based on fixed ratios of the game window dimensions.
	 */
	private void setPosition() {
		x = (5.0/18.0)*game.getWidth()*cos(rotationAngle) + x_init;
		y = (5.0/12.0)*game.getHeight()*sin(rotationAngle) + y_init;
	}
}
