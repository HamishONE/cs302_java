package warlords;

import warlordstest.IWall;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.PI;

public class Wall extends GameObject implements IWall {

	private double theta;

	public Wall(int x, int y, double theta) {
		super(x, y, null);
		this.theta = theta;
	}

	@Override
	public boolean isDestroyed() {
		return false;
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
}
