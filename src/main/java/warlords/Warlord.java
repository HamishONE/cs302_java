package warlords;

import warlordstest.IWarlord;

public class Warlord extends GameObject implements IWarlord {

	private boolean hasWon = false;
	private int health = 1;

	public Warlord(int x, int y, String imgPath) {
		super(x, y, imgPath, Math.PI/2);
		this.width = 100;
		this.height = 100;
	}

	@Override
	public boolean isDead() {
		return health <= 0;
	}

	@Override
	public boolean hasWon() {
		return hasWon;
	}

	public void causeDamage(int damage) {
		health -= damage;

		if (isDead()) {
			this.spritePath = "/red.png";
		}
	}

	public void setAsWinner() {
		hasWon = true;
	}
}
