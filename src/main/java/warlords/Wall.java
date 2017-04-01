package warlords;

import warlordstest.IWall;

//TODO remove once sound is properly implemented
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


/**
 * Represents a single instance of a wall/brick in the game.
 */
public class Wall extends GameObject implements IWall {

	private int health = 1;
	private int owner;


	//TODO remove once sound is properly implemented
	private MediaPlayer mediaPlayer;


	/**
	 * Create a new wall at a given position with a given owner
	 * @param x the x position of the wall
	 * @param y the y position of the wall
	 * @param theta the rotation of the wall
	 * @param owner the index number of the player who's wall this is
	 */
	public Wall(int x, int y, double theta, int owner) {
		super(x, y, "/wal.png", theta);
		width = 44.6;
		height = 20.9;
		this.owner = owner;


		//TODO remove once sound is properly implemented
		String musicFile = "build/resources/main/Sad-cat.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
	}

	@Override
	public boolean isDestroyed() {
		return health <= 0;
	}

	/**
	 * Reduce the health of the wall potentially destroying it.
	 * @param damage the amount to reduce health by
	 */
	public void causeDamage(int damage) {
		health -= damage;

		//TODO remove once sound is properly implemented
		mediaPlayer.play();
	}

	/**
	 * Get the owner of the wall.
	 * @return the index number of the wall's owner
	 */
	public int getOwner() {
		return owner;
	}
}
