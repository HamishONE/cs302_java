package warlords;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {

	private static final double MULTIPLIER = 0.2;

	private Ball ball;
	private List<Paddle> paddles;
	private List<Wall> walls;
	private List<Warlord> warlords;
	private Game game;

	public CollisionDetector(Ball ball, List<Paddle> paddles, List<Wall> walls, List<Warlord> warlords, Game game) {
		this.ball = ball;
		this.paddles = paddles;
		this.walls = walls;
		this.warlords = warlords;
		this.game = game;
	}

	private void destroyObject(GameObject gameObject) {

		if (gameObject instanceof Wall) {
			Wall wall = (Wall) gameObject;
			wall.causeDamage(1);
			if (wall.isDestroyed()) {
				walls.remove(wall);
			}
		}
		else if (gameObject instanceof Warlord) {
			Warlord warlord = (Warlord) gameObject;
			warlord.causeDamage(1);
		}
	}

	public void moveBall(int num) {

		double dX = ball.getXVelocity() * MULTIPLIER;
		double dY = ball.getYVelocity() * MULTIPLIER;

		double x = ball.getXPos();
		double y = ball.getYPos();

		for (int i=0; i<num; i++) {

			x += dX;
			y += dY;

			ArrayList<GameObject> allObjects = new ArrayList<>(paddles);
			allObjects.addAll(walls);
			for (Warlord warlord : warlords) {
				if (!warlord.isDead()) {
					allObjects.add(warlord);
				}
			}
			for (GameObject gameObject : allObjects) {
				if (gameObject.getRectangle().intersects(x-ball.getWidth()/2, y-ball.getHeight()/2, ball.getWidth(),
						ball.getHeight())) {
					reboundBall(x, y, num - i, gameObject.getRotation());
					destroyObject(gameObject);
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

			ball.tick(MULTIPLIER);
		}
	}

	private void reboundBall(double x, double y, int remaining, double surfaceAngle) {

		ball.rebound(surfaceAngle);
		for (int i=0; i<remaining; i++) {
			ball.tick(MULTIPLIER);
		}
	}
}
