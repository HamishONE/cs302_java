package warlords;

import warlordstest.IWarlord;

public class Warlord extends GameObject implements IWarlord {

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
