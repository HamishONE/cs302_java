package warlords;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Helper class for the game controller that detects collisions of the balls with other game objects and the game boundaries.
 */
public class CollisionDetector {

	private Ball ball;
	private List<Paddle> paddles;
	private List<Wall> walls;
	private List<Warlord> warlords;
	private Game game;

	/**
	 * Create a new collision detector instance
	 * @param ball the ball to check collisions with
	 * @param paddles a list of paddles the check collisions with
	 * @param walls the list of walls to check collisions with
	 * @param warlords the list of warlords to check collisions with
	 * @param game the game model used to get the game boundary dimensions
	 */
	public CollisionDetector(Ball ball, List<Paddle> paddles, List<Wall> walls, List<Warlord> warlords, Game game) {
		this.ball = ball;
		this.paddles = paddles;
		this.walls = walls;
		this.warlords = warlords;
		this.game = game;
	}

	// Performs any extra processing needed for the type of game object
	private void destroyObject(GameObject gameObject) {

		// If it is a wall cause one damage and if it is destroyed remove the wall from the list
		if (gameObject instanceof Wall) {
			Wall wall = (Wall) gameObject;
			wall.causeDamage(1);
			if (wall.isDestroyed()) {
				walls.remove(wall);
			}
		}
		// If it is a warlord cause one damage and if it is dead set the ball to null
		else if (gameObject instanceof Warlord) {
			Warlord warlord = (Warlord) gameObject;
			warlord.causeDamage(1);
			if (warlord.isDead()) {
				int index = warlords.indexOf(warlord);
				paddles.set(index, null);
			}
		}
	}

	/**
	 * Move the ball by one unit of it's velocity taking account of rebounds from collisions.
	 */
	public void moveBall() {

		ArrayList<GameObject> allObjects = new ArrayList<>(paddles);
		allObjects.addAll(walls);
		allObjects.removeIf(Objects::isNull);
		for (Warlord warlord : warlords) {
			if (!warlord.isDead()) {
				allObjects.add(warlord);
			}
		}
		allObjects.add(new Boundary(game));

		MathLine ballPath = new MathLine(ball.getXPos(), ball.getYPos(), ball.getXVelocity(), ball.getYVelocity());
		for (GameObject gameObject : allObjects) {
			ArrayList<MathLine> objectPaths = gameObject.getSideVectors();
			ArrayList<MathVector> objectVertices = gameObject.getVertices();

			for (int j=0; j<4; j++) {
				MathVector intersection = ballPath.intersectPoint(objectPaths.get(j));
				if (intersection == null) {
					continue;
				}
				int nextIndex = (j == 3) ? 0 : j+1;
				if (intersection.isBetween(objectVertices.get(j), objectVertices.get(nextIndex), ball.getWidth())) {
					reboundBall(objectPaths.get(j).getRotation());
					destroyObject(gameObject);
					return;
				}
			}
		}
		ball.tick();
	}

	// Rebound the ball along the provided angle of a surface and complete it's motion
	private void reboundBall(double surfaceAngle) {
		ball.rebound(surfaceAngle);
		ball.tick();
	}
}
