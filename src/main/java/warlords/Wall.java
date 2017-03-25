package warlords;

import warlordstest.IWall;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.PI;

public class Wall extends GameObject implements IWall {

	private double theta;
	private int health = 1;

	public Wall(int x, int y, double theta) {
		super(x, y, "/wal.png");
		this.theta = theta;
		width = 50;
		height = 50;
	}

	@Override
	public boolean isDestroyed() {
		return health <= 0;
	}

	@Override
	public void tick() {

	}

	@Override
	public Double getRotation() {
		return theta - PI/2;
	}

	@Override
	public Shape getRectangle() {

		Shape shape = new Rectangle2D.Double(x - width/2, y - height/2, width, height);
		AffineTransform at = AffineTransform.getRotateInstance(theta - PI/2, x, y);
		shape = at.createTransformedShape(shape);
		return shape;
	}

	public void causeDamage(int damage) {
		health -= 1;
	}
}
