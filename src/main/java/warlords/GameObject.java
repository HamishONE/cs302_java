package warlords;

public abstract class GameObject {

	protected double x;
	protected double y;
	protected double width;
	protected double height;
	protected String spritePath;

	public GameObject(int x, int y, String imgPath) {

		this.x = x;
		this.y = y;

		width = 24;
		height = 24;
		spritePath = "/test_sprite.png";
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
		return (int)x;
	}

	/***
	 * @return the vertical position of the ball.
	 */
	public int getYPos() {
		return (int)y;
	}

	public String getSpritePath() {
		return spritePath;
	}

	public Double getRotation() {
		return 0.0;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public abstract void tick();
}
