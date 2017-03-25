package warlords;

import warlordstest.IWall;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.PI;

public class Wall extends GameObject implements IWall {

	private int health = 1;

	public Wall(int x, int y, double theta) {
		super(x, y, "/wal.png", theta);
		width = 50;
		height = 50;
	}

	@Override
	public boolean isDestroyed() {
		return health <= 0;
	}

	public void causeDamage(int damage) {
		health -= damage;
	}
}
