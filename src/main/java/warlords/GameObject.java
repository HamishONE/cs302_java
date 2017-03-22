package warlords;

public abstract class GameObject {

	protected int x = 0;
	protected int y = 0;

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

	public abstract void tick();
}
