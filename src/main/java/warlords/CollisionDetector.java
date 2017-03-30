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

	/**
	 * Performs any extra processing needed for the type of game object
	 * @param gameObject The game object to process.
	 */
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

		// Create a list of all objects (excluding dead warlords and null paddles) including the game window boundary
		ArrayList<GameObject> allObjects = new ArrayList<>(paddles);
		allObjects.addAll(walls);
		allObjects.removeIf(Objects::isNull);
		for (Warlord warlord : warlords) {
			if (!warlord.isDead()) {
				allObjects.add(warlord);
			}
		}
		allObjects.add(new Boundary(game));

		// Get the path of the ball as a line
		MathLine ballPath = new MathLine(ball.getXPosReal(), ball.getYPosReal(), ball.getXVelocityReal(), ball.getYVelocityReal());

		// Loop through each game object to find the side with the closest point intersecting the balls velocity vector
		MathLine closestPath = null;
		GameObject closestObject = null;
		double shortestDistance = Double.MAX_VALUE;
		for (GameObject gameObject : allObjects) {

			// Get the bounding box of the object expanded by the radius of the ball as lines representing the sides
			// In the case of the game boundary we instead contract the bounding box as we expect the ball to be inside it
			ArrayList<MathLine> objectPaths;
			if (gameObject instanceof Boundary) {
				objectPaths = gameObject.getSideVectors(-ball.getWidth()/2);
			} else {
				objectPaths = gameObject.getSideVectors(ball.getWidth()/2);
			}

			// Loop through all four sides of the bounding box
			for (int j=0; j<4; j++) {
				MathLine objectPath = objectPaths.get(j);

				// Find the point of intersection between the side of the bounding box and the balls velocity vector
				MathVector intersection = ballPath.intersectPoint(objectPath);

				// If the intersection exists is closer than previous ones found store the side path and the game object
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

		// If an intersection has been found move the ball up to the object then rebound it for the remainder of it's velocity
		if (closestPath != null) {
			MathVector intersection = ballPath.intersectPoint(closestPath);
			double initialMovement = intersection.distanceTo(ball.getPointVector());
			double afterMovement = ball.getSpeed() - initialMovement;
			reboundBall(closestPath.getRotation(), initialMovement, afterMovement);
			destroyObject(closestObject);
		}
		// Otherwise ball the ball forward one unit of it's velocity
		else {
			ball.tick();
		}
	}

	/**
	 * Move the ball forward for some distance, rebound it, then move it some more distance.
	 * @param surfaceAngle The angle of the surface to rebound on.
	 * @param movementBeforeRebound The distance to move the ball before it rebounds.
	 * @param movementAfterRebound The distance to move the ball after it rebounds.
	 */
	private void reboundBall(double surfaceAngle, double movementBeforeRebound, double movementAfterRebound) {
		ball.tick(movementBeforeRebound);
		ball.rebound(surfaceAngle);
		ball.tick(movementAfterRebound);
	}
}
