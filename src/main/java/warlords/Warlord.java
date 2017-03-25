package warlords;

import warlordstest.IWarlord;

public class Warlord extends GameObject implements IWarlord {

	public Warlord(int x, int y) {
		super(x, y, null, 0);
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean hasWon() {
		return false;
	}

	@Override
	public void tick() {

	}
}
