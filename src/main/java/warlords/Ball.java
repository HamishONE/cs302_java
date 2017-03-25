package warlords;

import warlordstest.IBall;
import static java.lang.Math.*;

public class Ball extends GameObject implements IBall {

	private double dX = 0;
	private double dY = 0;

	public Ball(int x, int y) {
		super(x, y, "/green_circle.png", 0);
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
		return (int) round(dX);
	}

	@Override
	public int getYVelocity() {
		return (int) round(dY);
	}

	public void generateRandomMovement(double speed) {

		double MIN_ANGLE = 0.3;

		double angle = random() * (2*PI - MIN_ANGLE*8) + MIN_ANGLE;
		if (angle > PI/2 - MIN_ANGLE) {
			angle += MIN_ANGLE*2;
		}
		if (angle > PI - MIN_ANGLE) {
			angle += MIN_ANGLE*2;
		}
		if (angle > 3*PI/2 - MIN_ANGLE) {
			angle += MIN_ANGLE*2;
		}

		dX = speed * cos(angle);
		dY = speed * sin(angle);
	}

	/**
	 * Reflects the balls motion against a surface at a given angle in space
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
