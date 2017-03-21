package warlords;

import warlordstest.*;

public class Ball implements IBall {
    
    private int x = 0, y = 0, dx = 0, dy = 0;

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

    /***
     *  Set the horizontal velocity of the ball to the given value.
     * @param dX
     */
    public void setXVelocity(int dX) {
        this.dx = dX;
    }

    /***
     *  Set the vertical velocity of the ball to the given value.
     * @param dY
     */
    public void setYVelocity(int dY) {
        this.dy = dY;
    }

    /***
     * @return the horizontal velocity of the ball.
     */
    public int getXVelocity() {
        return dx;
    }

    /***
     * @return the vertical velocity of the ball.
     */
    public int getYVelocity() {
        return dy;
    }

    public void tick() {
        x += dx;
        y += dy;
    }
}
