package warlords;

/**
 * This class represents one AI user.
 */
public class ArtificialUser implements IUserInput {

	private Ball ball;
	private Paddle paddle;

	/**
	 * Create a new AI user
	 * @param ball the ball the AI will try to hit
	 * @param paddle the paddle the AI is controlling
	 */
	public ArtificialUser(Ball ball, Paddle paddle) {
		this.ball = ball;
		this.paddle = paddle;
	}

	@Override
	public InputType getInputType() {

		// Get the absolute angle of the ball and paddle's velocities
		double ballAngle = Math.atan2(ball.getYVelocity(), ball.getXVelocity());
		double paddleAngle = Math.atan2(paddle.getYPos() - ball.getYPos(), paddle.getXPos() - ball.getYPos());

		// Move the paddle to make the paddle closer to the ball angle
		if (ballAngle > paddleAngle) {
			return InputType.LEFT;
		} else {
			return InputType.RIGHT;
		}
	}
}
