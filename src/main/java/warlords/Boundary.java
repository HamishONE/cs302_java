package warlords;

public class Boundary extends GameObject {

	protected Boundary(Game game) {
		super(game.getWidth()/2, game.getHeight()/2, null, Math.PI/2);
		this.width = game.getWidth();
		this.height = game.getHeight();
	}
}
