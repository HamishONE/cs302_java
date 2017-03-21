package warlords;

import warlordstest.*;

public class Game implements IGame {

    private Ball ball;

    public Game(Ball ball) {
        this.ball = ball;
    }

    /***
     * A tick advances the state of the game world by a small time increment, i.e. one frame. The ball (and all other
     *  objects) should move according to their velocity, player inputs should be handled (but are not tested here),
     *  collisions should be detected and processed, game end conditions should be checked, and so on.
     */
    public void tick() {
        ball.tick();
    }

    /***
     * Determine if the game has finished. Results need only be valid before the start and after the end of a game tick.
     *
     * @return true if no more than one warlord remains alive, or if the time remaining is less than or equal to zero. Otherwise, return false.
     */
    public boolean isFinished() {
        return false;
    }

    /***
     * Set the time remaining in the game to the given value in seconds.
     *
     * @param seconds
     */
    public void setTimeRemaining(int seconds) {

    }
}