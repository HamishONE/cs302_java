package warlords;

/**
 * Represents a vector in 2D space. Also represents a point in space well.
 */
public class MathVector {

	private double x;
	private double y;

	/**
	 * Create a new vector instance from coordinates
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public MathVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return The x coordinate of the vector.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return The y coordinate of the vector.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Generates a new vector from the addition of this vector with another vector.
	 * @param arg2 The other vector to be added.
	 * @return The new vector from the addition.
	 */
	public MathVector add(MathVector arg2) {
		return new MathVector(x + arg2.getX(), y + arg2.getY());
	}

	/**
	 * Generates a new vector from the subtraction of another vector from this vector.
	 * @param arg2 The other vector to be subtracted.
	 * @return The new vector from the subtraction.
	 */
	public MathVector subtract(MathVector arg2) {
		return new MathVector(x - arg2.getX(), y - arg2.getY());
	}

	/**
	 * Generates a new vector by scaling this vector (i.e. multiplying by a constant).
	 * @param factor The factor to scale the vector by.
	 * @return The new vector after scaling.
	 */
	public MathVector scale(double factor) {
		return new MathVector(x * factor, y * factor);
	}

	/**
	 * The dot product of this vector with another vector.
	 * @param arg2 The other vector to be dotted.
	 * @return The result of the dot product.
	 */
	public double dot(MathVector arg2) {
		return x*arg2.getX() + y*arg2.getY();
	}

	/**
	 * The cross product of this vector with another vector (defined based on the magnitude of the 3D cross product).
	 * @param arg2 The other vector to be crossed with.
	 * @return The result of the cross product.
	 */
	public double cross(MathVector arg2) {
		return x*arg2.getY() - y*arg2.getX();
	}

	/**
	 * The distance from this vector point to another vector point.
	 * @param arg2 The vector point to find the distance to.
	 * @return The distance the other vector is away.
	 */
	public double distanceTo(MathVector arg2) {
		return Math.sqrt(Math.pow(x - arg2.getX(), 2) + Math.pow(y - arg2.getY(), 2));
	}

	/**
	 * Checks weather this vector point is between two other points with an extension margin.
	 * @param point1 The 1st other point.
	 * @param point2 The 2nd other point.
	 * @param extension The extension margin.
	 * @return Whether the vector point is in between the others.
	 */
	public boolean isBetween(MathVector point1, MathVector point2, double extension) {

		// Find if the vectors x and y positions lie in between the other points x and y positions
		// Do this check assuming point 1 is larger and assuming point 2 is larger
		boolean xInBetween1 = x >= point1.getX() - extension && x <= point2.getX() + extension;
		boolean xInBetween2 = x <= point1.getX() + extension && x >= point2.getX() - extension;
		boolean yInBetween1 = y >= point1.getY() - extension && y <= point2.getY() + extension;
		boolean yInBetween2 = y <= point1.getY() + extension && y >= point2.getY() - extension;

		// If under either assumption both the x and y are within range return true
		return (xInBetween1 || xInBetween2) && (yInBetween1 || yInBetween2);
	}

	/**
	 * @return The length of the vector.
	 */
	public double getLength() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
