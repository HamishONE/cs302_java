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

		// Get a line representing the balls motion, continuing forward outside the game boundaries
		MathLine ballPath = new MathLine(ball.getXPosReal(), ball.getYPosReal(), ball.getXVelocityReal(), ball.getYVelocityReal());
		ballPath.extendEnd(1e5);

		// Get a line intersecting the paddles current location with an angle tangent to the centre point of the paddle motion
		MathLine tangentLine = new MathLine(new MathVector(paddle.getXPosReal(), paddle.getYPosReal()), 1,
				paddle.getStartingRotation() - Math.PI/4);
		tangentLine.extendBothEnds(1e5);

		// Find any intersection between these two lines
		MathVector intersection = ballPath.intersectPoint(tangentLine);
		if (intersection != null) {

			// Find the distance between the intersection and the paddle location in the x and y directions
			double xDiff = intersection.getX() - paddle.getXPosReal();
			double yDiff = intersection.getY() - paddle.getYPosReal();

			// Use the paddles angle to normalise the sign of the distance in the y direction to have the same meaning as the
			// x direction, as only left and right inputs are available
			if (Math.tan(paddle.getStartingRotation()) < 0) {
				yDiff = -yDiff;
			}

			// Use the greater of the x and y differences to choose whether to move left or right
			double maxDiff = Math.abs(yDiff) > Math.abs(xDiff) ? yDiff : xDiff;
			if (maxDiff < 0) {
				return InputType.LEFT;
			} else {
				return InputType.RIGHT;
			}
		}
		return null;
	}
}
