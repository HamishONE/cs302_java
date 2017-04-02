package warlords;

/**
 * Represents a line with a starting position, magnitude and direction in 2D space.
 */
public class MathLine {

	private double x;
	private double y;
	private double dX;
	private double dY;

	/**
	 * Create a new line from it's starting position and displacement as x and y components.
	 * @param x the starting x position of the line
	 * @param y the starting y position of the line
	 * @param dX the displacement of the line in the x direction
	 * @param dY the displacement of the line in the y direction
	 */
	public MathLine(double x, double y, double dX, double dY) {
		this.x = x;
		this.y = y;
		this.dX = dX;
		this.dY = dY;
	}

	/**
	 * Create a new line from it's starting position and displacement as vectors.
	 * @param P1 The starting point of the line.
	 * @param P2 The displacement vector of the line.
	 */
	public MathLine(MathVector P1, MathVector P2) {
		this.x = P1.getX();
		this.y = P1.getY();
		this.dX = P2.getX() - P1.getX();
		this.dY = P2.getY() - P1.getY();
	}

	/**
	 * Create a new line from it's starting position as a vectors, it's length and it's angle.
	 * @param point The starting point of the line.
	 * @param length The length of the line.
	 * @param angle The angle of the line in radians.
	 */
	public MathLine(MathVector point, double length, double angle) {
		this.x = point.getX();
		this.y = point.getY();
		this.dX = length * Math.cos(angle);
		this.dY = length * Math.sin(angle);
	}

	/**
	 * @return The starting x position of the line.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return The starting y position of the line.
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return The displacement of the line in the x direction.
	 */
	public double getdX() {
		return dX;
	}

	/**
	 * @return The displacement of the line in the y direction.
	 */
	public double getdY() {
		return dY;
	}

	/**
	 * @return The starting point of the line as a vector.
	 */
	public MathVector getPointVector() {
		return new MathVector(x, y);
	}

	/**
	 * @return The displacement of the line as a vector.
	 */
	public MathVector getDirectionVector() {
		return new MathVector(dX, dY);
	}

	/**
	 * @return The rotation of the line in radians.
	 */
	public double getRotation() {
		return Math.atan2(dY, dX);
	}

	/**
	 * Find where the line intersects with another line.
	 * This does not extend the lines to find their intersection.
	 * @param line2 The other line to find the intersection with.
	 * @return The point of intersection as a vector.
	 */
	public MathVector intersectPoint(MathLine line2) {

		// Represent the two lines as p + t*r and q + u*s where p, r, q & s are vectors
		MathVector p = this.getPointVector();
		MathVector r = this.getDirectionVector();
		MathVector q = line2.getPointVector();
		MathVector s = line2.getDirectionVector();

		// Find t and u so the lines intersect as per the algorithm from http://stackoverflow.com/a/565282
		double t = q.subtract(p).cross(s) / r.cross(s);
		double u = q.subtract(p).cross(r) / r.cross(s);

		// If the lines intersect calculate and return the point otherwise return null
		if (r.cross(s) != 0 && t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			return p.add(r.scale(t));
		} else {
			return null;
		}
	}

	/**
	 * Extends the line at both ends.
	 * @param extension the distance to extend each end by
	 */
	public void extendBothEnds(double extension) {

		// Get the direction of the line in the x and y directions (+1 or -1)
		// Checks to prevent division by zero errors
		double xDirection = (dX == 0) ? 0 : dX/Math.abs(dX);
		double yDirection = (dY == 0) ? 0 : dY/Math.abs(dY);

		// Move the starting point back by the extension amount
		x -= xDirection * extension;
		y -= yDirection * extension;

		// Move the displacements forward by double the extension amount
		// This means the new line end point is one unit of extension amount further forward
		dX += xDirection * extension * 2;
		dY += yDirection * extension * 2;
	}

	/**
	 * Extends the line at the end only.
	 * @param extension the distance to extend the end by
	 */
	public void extendEnd(double extension) {

		// Get the direction of the line in the x and y directions (+1 or -1)
		// Checks to prevent division by zero errors
		double xDirection = (dX == 0) ? 0 : dX/Math.abs(dX);
		double yDirection = (dY == 0) ? 0 : dY/Math.abs(dY);

		// Move the displacements forward by the extension amount
		dX += xDirection * extension;
		dY += yDirection * extension;
	}
}
