package warlords;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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

	/**
	 * Getter for the path to the location of the object's sprite
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
}
