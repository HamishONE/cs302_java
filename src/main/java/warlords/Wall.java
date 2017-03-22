package warlords;

import warlordstest.IWall;

public class Wall extends GameObject implements IWall {

	@Override
	public boolean isDestroyed() {
		return false;
	}

	@Override
	public void tick() {

	}
}
