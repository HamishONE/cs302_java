package warlords;

import java.awt.geom.Point2D;

public class MathLine {

	private double x;
	private double y;
	private double dX;
	private double dY;

	public MathLine(double x, double y, double dX, double dY) {
		this.x = x;
		this.y = y;
		this.dX = dX;
		this.dY = dY;
	}

	public MathLine(Point2D.Double P1, Point2D.Double P2) {
		this.x = P1.getX();
		this.y = P1.getY();
		this.dX = P2.getX() - P1.getX();
		this.dY = P2.getY() - P1.getY();
	}

	public MathLine(MathVector P1, MathVector P2) {
		this.x = P1.getX();
		this.y = P1.getY();
		this.dX = P2.getX() - P1.getX();
		this.dY = P2.getY() - P1.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getdX() {
		return dX;
	}

	public double getdY() {
		return dY;
	}

	public MathVector getPointVector() {
		return new MathVector(x, y);
	}


	public MathVector getDirectionVector() {
		return new MathVector(dX, dY);
	}

	public double getRotation() {
		return Math.atan2(dY, dX);
	}

	public MathVector intersectPoint(MathLine line2) {

		/*
		* Using the algorithm from http://stackoverflow.com/a/565282
		* The two lines represented as p + t r and q + u s where p, r, 1 and s are vectors
		*/

		MathVector p = this.getPointVector();
		MathVector r = this.getDirectionVector();
		MathVector q = line2.getPointVector();
		MathVector s = line2.getDirectionVector();

		double t = q.subtract(p).cross(s) / r.cross(s);
		double u = q.subtract(p).cross(r) / r.cross(s);

		if (r.cross(s) != 0 && t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			return p.add(r.scale(t));
		} else {
			return null;
		}
	}

	/**
	 * Extends the line at both ends
	 * @param extension the distance to extend each end by
	 */
	public void extendBothEnds(double extension) {

		double xDirection = (dX == 0) ? 0 : dX/Math.abs(dX);
		double yDirection = (dY == 0) ? 0 : dY/Math.abs(dY);

		x -= xDirection * extension;
		y -= yDirection * extension;
		dX += xDirection * extension * 2;
		dY += yDirection * extension * 2;
	}

	public void extendEnd(double extension) {

		double xDirection = (dX == 0) ? 0 : dX/Math.abs(dX);
		double yDirection = (dY == 0) ? 0 : dY/Math.abs(dY);

		dX += xDirection * extension;
		dY += yDirection * extension;
	}
}
