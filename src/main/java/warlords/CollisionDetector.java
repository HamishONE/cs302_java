package warlords;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {

	private Ball ball;
	private List<Paddle> paddles;
	private List<Wall> walls;
	private Game game;

	public CollisionDetector(Ball ball, List<Paddle> paddles, List<Wall> walls, Game game) {
		this.ball = ball;
		this.paddles = paddles;
		this.walls = walls;
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

			ArrayList<GameObject> allObjects = new ArrayList<>(paddles);
			allObjects.addAll(walls);
			for (GameObject gameObject : allObjects) {
				if (gameObject.getRectangle().intersects(x-ball.getWidth()/2, y-ball.getHeight()/2, ball.getWidth(),
						ball.getHeight())) {
					reboundBall(x, y, num - i, gameObject.getRotation());
					if (gameObject instanceof Wall) {
						Wall wall = (Wall) gameObject;
						wall.causeDamage(1);
						if (wall.isDestroyed()) {
							walls.remove(wall);
						}
					}
					return;
				}
			}

			if (x + ball.getWidth()/2 >= game.getWidth()) {
				reboundBall(x, y, num - i, Math.PI/2);
				return;
			}
			else if (x <= ball.getWidth()/2) {
				reboundBall(x, y, num - i, Math.PI/2);
				return;
			}
			else if (y + ball.getHeight()/2 >= game.getHeight()) {
				reboundBall(x, y, num - i, 0);
				return;
			}
			else if (y <= ball.getHeight()/2) {
				reboundBall(x, y, num - i, 0);
				return;
			}

			ball.tick();
		}
	}

	private void reboundBall(double x, double y, int remaining, double surfaceAngle) {

		ball.rebound(surfaceAngle);
		for (int i=0; i<remaining; i++) {
			ball.tick();
		}
	}
}
