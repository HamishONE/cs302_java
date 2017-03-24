package warlords;

public class ArtificialUser implements IUserInput {

	private Ball ball;
	private Paddle paddle;

	public ArtificialUser(Ball ball, Paddle paddle) {
		this.ball = ball;
		this.paddle = paddle;
	}

	@Override
	public InputType getInputType() {

		double ballAngle = Math.atan2(ball.getYVelocity(), ball.getXVelocity());
		double paddleAngle = Math.atan2(paddle.getYPos() - ball.getYPos(), paddle.getXPos() - ball.getYPos());

		if (ballAngle > paddleAngle) {
			return InputType.LEFT;
		} else {
			return InputType.RIGHT;
		}
	}
}
