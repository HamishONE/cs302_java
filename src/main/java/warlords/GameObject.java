package warlords;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.PI;

public abstract class GameObject {

	protected double x;
	protected double y;
	protected double width;
	protected double height;
	protected String spritePath;
	protected double rotationAngle = 0;

	public GameObject(int x, int y, String imgPath, double theta) {

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

	public String getSpritePath() {
		return spritePath;
	}

	public Double getRotation() {
		return rotationAngle - Math.PI/2;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public abstract void tick();

	public Shape getRectangle() {
		Shape shape = new Rectangle2D.Double(x - width/2, y - height/2, width, height);
		AffineTransform at = AffineTransform.getRotateInstance(rotationAngle - PI/2, x, y);
		shape = at.createTransformedShape(shape);
		return shape;
	}
}
