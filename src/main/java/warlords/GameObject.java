package warlords;

import javafx.scene.image.Image;

public abstract class GameObject {

	protected int x;
	protected int y;
	protected Image image;

	public GameObject(int x, int y, String imgPath) {
		this.x = x;
		this.y = y;
		//TODO make it read from path again
		//image = new Image(getClass().getClassLoader().getResource(imgPath).toString(), true);
		image = new Image("http://vignette2.wikia.nocookie.net/pokemon/images/f/f2/Bag_Beast_Ball_Sprite.png/revision/latest?cb=20170127202459");
	}

	/***
	 *  Set the horizontal position of the ball to the given value.
	 * @param x
	 */
	public void setXPos(int x) {
		this.x = x;
	}

	/***
	 * Set the vertical position of the ball to the given value.
	 * @param y
	 */
	public void setYPos(int y) {
		this.y = y;
	}

	/***
	 * @return the horizontal position of the ball.
	 */
	public int getXPos() {
		return x;
	}

	/***
	 * @return the vertical position of the ball.
	 */
	public int getYPos() {
		return y;
	}

	public Image getSprite() {
		return image;
	}

	public Double getRotation() {
		return 0.0;
	}

	public abstract void tick();
}
