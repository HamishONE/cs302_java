package warlords;

import java.util.List;

public class CollisionDetector {

	private Ball ball;
	private List<Paddle> paddles;

	public CollisionDetector(Ball ball, List<Paddle> paddles) {
		this.ball = ball;
		this.paddles = paddles;
	}

	public void moveBall(int num) {

		double dX = ball.getXVelocity();
		double dY = ball.getYVelocity();

		double x = ball.getXPos();
		double y = ball.getYPos();

		for (int i=0; i<num; i++) {

			x += dX;
			y += dY;

			for (Paddle paddle : paddles) {
				if (paddle.getRectangle().contains(x, y)) {
					ball.rebound(paddle.getRotation(), true);
					return;
				}
			}
			ball.tick();
		}
	}
}
