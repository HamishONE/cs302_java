package warlords;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import static java.lang.Math.PI;

/**
 * Abstract class which dictates the core functionality for all of the objects in the game
 */
public abstract class GameObject {

	protected double x;
	protected double y;
	protected double width;
	protected double height;
	protected String spritePath;
	protected double rotationAngle = 0;

	/**
	 * Called only from child classes, basic constructor
	 *
	 * @param x			x location
	 * @param y			y location
	 * @param imgPath	Path to image file
	 * @param theta		Angle object should be rotated by
	 */
	protected GameObject(int x, int y, String imgPath, double theta) {

		this.x = x;
		this.y = y;
		this.rotationAngle = theta;

		width = 24;
		height = 24;
		if (imgPath != null) {
			spritePath = imgPath;
		} else {
			spritePath = "/test_sprite.png";
		}
	}

	/***
	 *  Set the horizontal position of the ball to the given value.
	 * @param x
	 */
	public void setXPos(int x) {
		this.x = x;
	}

	/***
	 * Set the vertical position of the ball to the given value.
	 * @param y
	 */
	public void setYPos(int y) {
		this.y = y;
	}

	/***
	 * @return the horizontal position of the ball.
	 */
	public int getXPos() {
		return (int)x;
	}

	/***
	 * @return the vertical position of the ball.
	 */
	public int getYPos() {
		return (int)y;
	}

	/***
	 * @return the horizontal position of the ball.
	 */
	public double getXPosReal() {
		return x;
	}

	/***
	 * @return the vertical position of the ball.
	 */
	public double getYPosReal() {
		return y;
	}

	/** Getter for the path to the location of the object's sprite
	 * @return string of path ot image
	 */
	public String getSpritePath() {
		return spritePath;
	}

	/**
	 * Getter for the rotation value of the object
	 *
	 * @return double of rotation value
	 */
	public Double getRotation() {
		return rotationAngle - Math.PI/2;
	}

	/**
	 * Getter for the width of the object
	 *
	 * @return double of width of the object
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Getter for the height of the object
	 *
	 * @return double of height of the object
	 */
	public double getHeight() {
		return height;
	}

	public MathVector getPointVector() {
		return new MathVector(x, y);
	}

	/**
	 * Getter for a rotated rectangle to represent the bounds of the sprite
	 *
	 * @return Shape of rectangle bounds
	 */
	public Shape getRectangle() {
		Shape shape = new Rectangle2D.Double(x - width/2, y - height/2, width, height);
		AffineTransform at = AffineTransform.getRotateInstance(rotationAngle - PI/2, x, y);
		shape = at.createTransformedShape(shape);
		return shape;
	}

	/**
	 * Get the four verities of the objects bounding box expanded by some amount.
	 * @param expansionWidth The amount to expand the bounding box by.
	 * @return A list of the four vertices as vectors.
	 */
	private ArrayList<MathVector> getVertices(double expansionWidth) {

		ArrayList<MathVector> points = new ArrayList<>();

		Shape shape = new Rectangle2D.Double(x - width/2-expansionWidth, y - height/2-expansionWidth,
				width+2*expansionWidth, height+2*expansionWidth);
		AffineTransform at = AffineTransform.getRotateInstance(rotationAngle - PI/2, x, y);
		PathIterator pathIterator = shape.getPathIterator(at);
		for (int i=0; i<4; i++) {
			double[] coordinates = new double[2];
			pathIterator.currentSegment(coordinates);
			points.add(new MathVector(coordinates[0], coordinates[1]));
			pathIterator.next();
		}

		return points;
	}

	/**
	 * Get the 4 sides of the objects bounding box expanded by some amount.
	 * @param expansionWidth The amount to expand the bounding box by.
	 * @return A list of the four sides as lines.
	 */
	public ArrayList<MathLine> getSideVectors(double expansionWidth) {

		ArrayList<MathVector> points = getVertices(expansionWidth);

		ArrayList<MathLine> vectors = new ArrayList<>();
		vectors.add(new MathLine(points.get(0), points.get(1)));
		vectors.add(new MathLine(points.get(1), points.get(2)));
		vectors.add(new MathLine(points.get(2), points.get(3)));
		vectors.add(new MathLine(points.get(3), points.get(0)));

		return vectors;
	}
}
