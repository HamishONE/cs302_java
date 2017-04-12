package warlords;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.Math.PI;

/**
 * Abstract class which dictates the core functionality for all of the objects in the game
 */
public abstract class GameObject {

	protected double x;
	protected double y;
	protected double width;
	protected double height;
	protected Ages age;
	protected HashMap<Ages, String> spritePaths = new HashMap<>();
	protected String soundPath;
	protected double rotationAngle = 0;
	protected PowerUp powerUp;

	/**
	 * Called only from child classes, basic constructor
	 *
	 * @param x			x location
	 * @param y			y location
	 * @param age		age the object needs to represent
	 * @param soundPath	Path to sound file
	 * @param theta		Angle object should be rotated by
	 */
	protected GameObject(int x, int y, Ages age, String soundPath, double theta) {

		this.x = x;
		this.y = y;
		this.rotationAngle = theta;
		powerUp = null;

		width = 24;
		height = 24;

		this.age = age;

		if (soundPath != null) {
			this.soundPath = soundPath;
		} else {
			this.soundPath = "/Sad-cat.mp3";
		}
	}

	/**
	 *  Set the horizontal position of the ball to the given value.
	 * @param x the new x position
	 */
	public void setXPos(int x) {
		this.x = x;
	}

	/**
	 * Set the vertical position of the ball to the given value.
	 * @param y the new y position
	 */
	public void setYPos(int y) {
		this.y = y;
	}

	/**
	 * @return the horizontal position of the ball.
	 */
	public int getXPos() {
		return (int)x;
	}

	/**
	 * @return the vertical position of the ball.
	 */
	public int getYPos() {
		return (int)y;
	}

	/**
	 * @return the horizontal position of the ball.
	 */
	public double getXPosReal() {
		return x;
	}

	/**
	 * @return the vertical position of the ball.
	 */
	public double getYPosReal() {
		return y;
	}

	/** Getter for the path to the location of the object's sound
	 * @return string of path to sound
	 */
	public String getSoundPath() {
		return soundPath;
	}

	/** Getter for the path to the location of the object's sprite
	 * @return string of path to image
	 */
	public String getSpritePath() {
		return spritePaths.get(age);
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

	/**
	 * @return The location of the object as a vector point.
	 */
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

	/**
	 * Set the width and height of the object.
	 * @param width the new width of the object
	 * @param height the new height of the object
	 */
	public void setDimensions(double width, double height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Set the power up attached to the game object.
	 * @param powerUp the power up to be attached.
	 */
	public void setPowerUp(PowerUp powerUp){
		this.powerUp = powerUp;
	}

	/**
	 * @return The power up attached to the game object.
	 */
	public PowerUp getPowerUp() {
		return powerUp;
	}
}
