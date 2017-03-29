package warlords;

import java.awt.geom.Point2D;

public class MathVector {

	private double x;
	private double y;

	public MathVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public MathVector(Point2D.Double point) {
		x = point.getX();
		y = point.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public MathVector add(MathVector arg2) {
		return new MathVector(x + arg2.getX(), y + arg2.getY());
	}

	public MathVector subtract(MathVector arg2) {
		return new MathVector(x - arg2.getX(), y - arg2.getY());
	}

	public MathVector scale(double factor) {
		return new MathVector(x * factor, y * factor);
	}

	public double dot(MathVector arg2) {
		return x*arg2.getX() + y*arg2.getY();
	}

	public double cross(MathVector arg2) {
		return x*arg2.getY() - y*arg2.getX();
	}

	public double distanceTo(MathVector arg2) {
		return Math.sqrt(Math.pow(x - arg2.getX(), 2) + Math.pow(y - arg2.getY(), 2));
	}

	public boolean isBetween(MathVector point1, MathVector point2, double extension) {

		boolean xInBetween1 = x >= point1.getX() - extension && x <= point2.getX() + extension;
		boolean xInBetween2 = x <= point1.getX() + extension && x >= point2.getX() - extension;
		boolean yInBetween1 = y >= point1.getY() - extension && y <= point2.getY() + extension;
		boolean yInBetween2 = y <= point1.getY() + extension && y >= point2.getY() - extension;

		return (xInBetween1 || xInBetween2) && (yInBetween1 || yInBetween2);
	}
}
