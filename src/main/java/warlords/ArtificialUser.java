package warlords;

/**
 * This class represents one AI user.
 */
public class ArtificialUser implements IUserInput {

	private final static double MOVEMENT_TOLERANCE = Math.PI/50;

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

	/**
	 * Use the paddles angle to determine if it needs to be moved left or right to achieve clockwise rotation.
	 * @param paddleAngle The angle the paddle is rotated by.
	 * @return The input to make (left or right).
	 */
	private InputType moveCW(double paddleAngle) {
		if (Math.sin(paddleAngle) > 0) {
			return InputType.RIGHT;
		} else {
			return InputType.LEFT;
		}
	}

	/**
	 * Use the paddles angle to determine if it needs to be moved left or right to achieve counterclockwise rotation.
	 * @param paddleAngle The angle the paddle is rotated by.
	 * @return The input to make (left or right).
	 */
	private InputType moveCCW(double paddleAngle) {
		if (Math.sin(paddleAngle) > 0) {
			return InputType.LEFT;
		} else {
			return InputType.RIGHT;
		}
	}

	@Override
	public InputType getInputType() {

		// Get the angle from the paddle to the ball and the angle of the paddle.
		double angleToBall = Math.atan2(ball.getYPos() - paddle.getYPos(), ball.getXPos() - paddle.getXPos());
		if (angleToBall < 0) {
			// Normalise the angle to be in the range 0 to 2pi
			angleToBall += 2*Math.PI;
		}
		double paddleAngle = paddle.getRotation() + Math.PI/2;

		// Move the paddle to make the two angles closer
		double difference = paddleAngle - angleToBall;
		if (difference < -MOVEMENT_TOLERANCE) {
			return moveCCW(paddleAngle);
		} else if (difference > MOVEMENT_TOLERANCE) {
			return moveCW(paddleAngle);
		}
		return null;
	}

	@Override
	public String getCharInput() {
		return null;
	}
}
