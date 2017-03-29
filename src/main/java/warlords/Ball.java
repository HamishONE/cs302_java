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
	 */
	public Ball(int x, int y) {
		super(x, y, "/cannonBall.png", 0);
	}

	/**
	 * Move the ball along it's path by one unit of velocity
	 */
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
		return (int) round(dX);
	}

	@Override
	public int getYVelocity() {
		return (int) round(dY);
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
	public void rebound(double phi) {

		// Transform the balls motion into a coordinate system where dU is parallel to the surface to rebound off
		double dU = dX*cos(phi) + dY*sin(phi);
		double dV = -1*dX*sin(phi) + dY*cos(phi);

		// Reverse the component of the balls motion normal to the surface it is rebounding off
		dV = -dV;

		// Transform dU and dV back into absolute coordinates
		dX = dU*cos(phi) - dV*sin(phi);
		dY = dU*sin(phi) + dV*cos(phi);
	}
}
