package warlords;

import warlordstest.IPaddle;
import java.awt.geom.Rectangle2D;
import java.util.List;
import static java.lang.Math.*;

/**
 * Represents a single instance of a paddle in the game.
 */
public class Paddle extends GameObject implements IPaddle {

	private Game game;
	private List<Ball> balls;
	private Double thetaInit;
	private int xInit;
	private int yInit;
	private double paddleSpeed, initPaddleSpeed;
	private double modifierMagnitude = PI/300;

	/**
	 * Create a new paddle instance at a given position
	 * @param x the x position of the paddle
	 * @param y the y position of the paddle
	 * @param age the age to base the sprite on
	 * @param theta the lower bound for the rotation of the paddle
	 * @param game the game model used to get the dimensions of the game screen
	 * @param balls a list of balls to ensure never end up inside the paddle
	 */
	public Paddle(int x, int y, Ages age, Double theta, Game game, List<Ball> balls) {
		super(x, y, age, "/bounce3.mp3", theta + PI/4);
		this.width = 100;
		this.height = 20;
		xInit = x;
		yInit = y;
		thetaInit = theta;
		paddleSpeed = PI/150;
		initPaddleSpeed = paddleSpeed;
		this.game = game;
		this.balls = balls;
		setPosition();

		spritePaths.put(Ages.NEOLITHIC, "/paddle.png");
		spritePaths.put(Ages.MEDIEVAL, "/paddle.png");
		spritePaths.put(Ages.INDUSTRIAL, "/paddle.png");
		spritePaths.put(Ages.SPACE, "/paddle.png");
	}

	/**
	 * @return The initial angle provided in the paddle constructor.
	 */
	public double getStartingRotation() {
		return thetaInit;
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
		if (yInit < Game.backendWidth/2) {
			moveCW();
		} else {
			moveCCW();
		}
	}

	/**
	 * Move the paddle in a arc to the right (from the user's perspective).
	 */
	public void moveRight() {
		if (yInit > Game.backendWidth/2) {
			moveCW();
		} else {
			moveCCW();
		}
	}

	/**
	 * Move the paddle one increment in a clockwise arc.
	 */
	private void moveCW() {
		if (rotationAngle <= thetaInit + PI/2) {
			moveAvoidingBall(paddleSpeed);
		}
	}

	/**
	 * Move the paddle one increment in a counterclockwise arc.
	 */
	private void moveCCW() {
		if (rotationAngle >= thetaInit) {
			moveAvoidingBall(-paddleSpeed);
		}
	}

	/**
	 * @param ball The ball to be checked.
	 * @return True if the rectangle around the ball intersects or is contained in the paddles rectangle.
	 */
	private boolean isBallInPaddle(Ball ball) {
		Rectangle2D ballRect = new Rectangle2D.Double(ball.getXPosReal() - ball.getWidth()/2,
				ball.getYPosReal() - ball.getHeight()/2, ball.getWidth(), ball.getHeight());
		return getRectangle().intersects(ballRect) || getRectangle().contains(ballRect);
	}

	/**
	 * Move the ball by the angle given only if this will not cause the ball to be inside the paddle.
	 * @param angle The angle to move in radians with a positive sign meaning clockwise.
	 */
	private void moveAvoidingBall(double angle) {
		rotationAngle += angle;
		setPosition();
		for (Ball ball : balls) {
			if (isBallInPaddle(ball)) {
				rotationAngle -= angle;
				setPosition();
				break;
			}
		}
	}

	/**
	 * Set the position of the paddle to sit on a circular arc based on it's rotation.
	 * Generates the circular arc based on fixed ratios of the game window dimensions.
	 */
	private void setPosition() {
		x = (5.0/18.0)*Game.backendWidth*cos(rotationAngle) + xInit;
		y = (5.0/12.0)*Game.backendHeight*sin(rotationAngle) + yInit;
	}

	/**
	 * Increase the paddle speed as the result of a powerup.
	 */
	public void speedUpPaddle() {
		if (paddleSpeed <= initPaddleSpeed) {
			paddleSpeed += modifierMagnitude;
		}
	}

	/**
	 * Decrease the paddle speed as the result of a powerup.
	 */
	public void slowDownPaddle() {
		if (paddleSpeed >= (PI/150)) {
			paddleSpeed -= modifierMagnitude;
		}
	}

	/**
	 * @return The current speed of the paddle.
	 */
	public double getPaddleSpeed() {
		return paddleSpeed;
	}

	/**
	 * @return The initial speed of the paddle.
	 */
	public double getInitPaddleSpeed() {
		return initPaddleSpeed;
	}

	/**
	 * Increase/decease the paddles width of the paddle by the amount given.
	 * @param modifier the amount to change the width by.
	 */
	public void modifyWidth(double modifier) {
		width += modifier;
	}
}
