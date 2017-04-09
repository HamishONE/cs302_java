package warlords;

import warlordstest.IBall;
import static java.lang.Math.*;

/**
 * Represents a single ball in the game.
 */
public class Ball extends GameObject implements IBall {

	private double dX = 0;
	private double dY = 0;

	/**
	 * Create a new stationery ball
	 * @param x the x position of the ball
	 * @param y the y position of the ball
	 * @param age the age to base the sprite on
	 */
	public Ball(int x, int y, Ages age) {
		super(x, y, age, null, 0);

		spritePaths.put(Ages.NEOLITHIC, "/meatBall.png");
		spritePaths.put(Ages.MEDIEVAL, "/cannonBall.png");
		spritePaths.put(Ages.INDUSTRIAL, null);
		spritePaths.put(Ages.SPACE, null);
	}

	/**
	 * Move the ball along it's path by one unit of velocity
	 */
	public void tick() {
		x += dX;
		y += dY;
	}

	/**
	 * Move the ball along it's path by a specified amount
	 * @param magnitude the amount to move the ball by
	 */
	public void tick(double magnitude) {

		double multiplier = magnitude/getSpeed();
		x += dX * multiplier;
		y += dY * multiplier;
	}

	/**
	 * @return The speed of the ball.
	 */
	public double getSpeed() {
		return sqrt(pow(dX, 2) + pow(dY, 2));
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
		return (int) round(dX);
	}

	@Override
	public int getYVelocity() {
		return (int) round(dY);
	}

	/**
	 * @return the balls velocity in the x direction as a floating point
	 */
	public double getXVelocityReal() {
		return dX;
	}

	/**
	 * @return the balls velocity in the y direction as a floating point
	 */
	public double getYVelocityReal() {
		return dY;
	}

	/**
	 * Generate movement in a random direction that is not directly vertical or horizontal.
	 * @param speed the magnitude of the velocity
	 */
	public void generateRandomMovement(double speed) {

		double MIN_ANGLE = 0.3;

		// Generate a random number in the range of a full circle less an amount so it won't exceed 2pi later
		double angle = random() * (2*PI - MIN_ANGLE*8) + MIN_ANGLE;

		// Increase the angle according to it's range to distribute it away from pure vertical and horizontal
		if (angle > PI/2 - MIN_ANGLE) {
			angle += MIN_ANGLE*2;
		}
		if (angle > PI - MIN_ANGLE) {
			angle += MIN_ANGLE*2;
		}
		if (angle > 3*PI/2 - MIN_ANGLE) {
			angle += MIN_ANGLE*2;
		}

		// Set the x and y velocity according to this angle
		dX = speed * cos(angle);
		dY = speed * sin(angle);
	}

	/**
	 * Reflects the balls motion against a surface at a given angle in space.
	 * @param phi the absolute angle of the surface in radians
	 */
	public void rebound(double phi, GameObject object) {


		// Transform the balls motion into a coordinate system where dU is parallel to the surface to rebound off
		double dU = dX*cos(phi) + dY*sin(phi);
		double dV = -1*dX*sin(phi) + dY*cos(phi);

		// Reverse the component of the balls motion normal to the surface it is rebounding off
		dV = -dV;

		// Transform dU and dV back into absolute coordinates
		dX = dU*cos(phi) - dV*sin(phi);
		dY = dU*sin(phi) + dV*cos(phi);

		//If object is a paddle, then apply the ball's powerup to the paddle where appropriate
		if(object instanceof Paddle) {
			if(getPowerUp() != null) {
				switch (getPowerUp()){

					case PADDLE_FASTER:
						((Paddle) object).modifyPaddleSpeed(PI/300);
						break;
					case PADDLE_SLOWER:
						((Paddle) object).modifyPaddleSpeed(-PI/300);
						break;
					case PADDLE_GROW:
						((Paddle) object).modifyWidth(20);
						break;
					case PADDLE_SHRINK:
						((Paddle) object).modifyWidth(-20);
						break;
				}
				setPowerUp(null);
			}
		}
		else if( object != null) { //Get any powerups from object being broken
			if(object.getPowerUp() != null) {
				switch (object.getPowerUp()) {
					case BALL_FASTER:
						multiplySpeed(1.5);
						break;
					case BALL_SLOWER:
						multiplySpeed(0.5);
						break;
					default:
						setPowerUp(object.getPowerUp());
						break;
				}
			}
		}
	}

	@Override
	public Double getRotation() {
		return atan2(dY, dX);
	}

	public void multiplySpeed(double multiplier) {
		dY = dY*multiplier;
		dX = dX*multiplier;
	}
}
