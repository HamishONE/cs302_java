package warlords;

/**
 * Represents the game window itself
 */
public class Boundary extends GameObject {

	/**
	 * Creates a new instance of the game boundary.
	 * @param game The game model to get the window dimensions from.
	 */
	protected Boundary(Game game) {
		super(game.getWidth()/2, game.getHeight()/2, null, null, Math.PI/2);
		this.width = game.getWidth();
		this.height = game.getHeight();
	}
}
