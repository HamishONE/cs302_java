package warlords;

/**
 * Represents the game window itself
 */
public class Boundary extends GameObject {

	/**
	 * Creates a new instance of the game boundary.
	 */
	protected Boundary(Ages age) {
		super(Game.backendWidth/2, Game.backendHeight/2, age, null, Math.PI/2);
		this.width = Game.backendWidth;
		this.height = Game.backendHeight;

		spritePaths.put(Ages.NEOLITHIC, null);
		spritePaths.put(Ages.MEDIEVAL, null);
		spritePaths.put(Ages.INDUSTRIAL, null);
		spritePaths.put(Ages.SPACE, null);
	}
}
