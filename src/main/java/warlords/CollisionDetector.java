package warlords;

import java.util.List;

public class CollisionDetector {

	private Ball ball;
	private List<Paddle> paddles;
	private Game game;

	public CollisionDetector(Ball ball, List<Paddle> paddles, Game game) {
		this.ball = ball;
		this.paddles = paddles;
		this.game = game;
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
					ball.rebound(paddle.getRotation());
					return;
				}
			}

			if (ball.getXPos() >= game.getWidth()) {
				ball.rebound(Math.PI/2);
				return;
			}
			else if (ball.getXPos() <= 0) {
				ball.rebound(Math.PI/2);
				return;
			}
			else if (ball.getYPos() >= game.getHeight()) {
				ball.rebound(0);
				return;
			}
			else if (ball.getYPos() <= 0) {
				ball.rebound(0);
				return;
			}

			ball.tick();
		}
	}
}
