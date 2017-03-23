package warlords;

import warlordstest.IBall;
import static java.lang.Math.*;

public class Ball extends GameObject implements IBall {

	private double dX = 0;
	private double dY = 0;

	public Ball(int x, int y) {
		super(x, y);
	}

	@Override
	public void tick() {
		x += round(dX);
		y += round(dY);
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

	public void generateRandomMovement(int speed) {

		double angle = random() * 2*PI;
		dX = speed * cos(angle);
		dY = speed * sin(angle);
	}

	/**
	 * Reflects the balls motion against a surface at a given angle in space
	 * @param phi the absolute angle of the surface in radians
	 */
	public void rebound(double phi) {

		// Transform the balls motion into a coordinate system where dU is parallel to the surface to rebound off
		double dU = dX*cos(phi) - dY*sin(phi);
		double dV = dX*sin(phi) + dY*cos(phi);

		// Reverse the component of the balls motion normal to the surface it is rebounding off
		dV = -dV;

		// Transform dU and dV back into absolute coordinates
		dX = dU*cos(phi) + dV*sin(phi);
		dY = -1*dU*sin(phi) + dV*cos(phi);

		// Move the ball forward one tick
		tick();
	}
}
