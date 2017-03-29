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
		ballPath.extendEnd(ball.getWidth()/2);
		MathLine closestPath = null;
		GameObject closestObject = null;
		double shortestDistance = Double.MAX_VALUE;
		for (GameObject gameObject : allObjects) {
			ArrayList<MathLine> objectPaths = gameObject.getSideVectors();
			ArrayList<MathVector> objectVertices = gameObject.getVertices();

			for (int j=0; j<4; j++) {
				MathLine objectPath = objectPaths.get(j);
				objectPath.extendBothEnds(ball.getWidth()/2);
				MathVector intersection = ballPath.intersectPoint(objectPath);
				if (intersection != null) {
					double distanceAway = intersection.distanceTo(ballPath.getPointVector());
					if (distanceAway < shortestDistance) {
						shortestDistance = distanceAway;
						closestPath = objectPath;
						closestObject = gameObject;
					}
				}
			}
		}
		if (closestPath != null) {
			MathVector intersection = ballPath.intersectPoint(closestPath);
			double initialMovement = intersection.distanceTo(ball.getPointVector()) - ball.getWidth()/2;
			double afterMovement = ball.getSpeed() - initialMovement;
			reboundBall(closestPath.getRotation(), initialMovement, afterMovement);
			destroyObject(closestObject);
		} else {
			ball.tick();
		}
	}

	// Rebound the ball along the provided angle of a surface and complete it's motion
	private void reboundBall(double surfaceAngle, double movementBeforeRebound, double movementAfterRebound) {
		ball.tick(movementBeforeRebound);
		ball.rebound(surfaceAngle);
		ball.tick(movementAfterRebound);
	}
}
