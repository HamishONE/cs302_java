package warlords;

import warlordstest.IWall;

public class Wall extends GameObject implements IWall {

	public Wall(int x, int y) {
		super(x, y);
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	@Override
	public void tick() {

	}
}
