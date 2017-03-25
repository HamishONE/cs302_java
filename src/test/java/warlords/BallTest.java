package warlords;

import junit.framework.TestSuite;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class BallTest extends TestSuite {

	@Test
	public void testBasicInit() {

		Ball ball = new Ball(100, 150);
		ball.setXVelocity(-1);
		ball.setYVelocity(5);

		assertTrue("y velocity is as given", ball.getYVelocity() == 5);
		assertTrue("x velocity is as given", ball.getXVelocity() == -1);
		assertTrue("x position is as given", ball.getXPos() == 100);
		assertTrue("y position is as given", ball.getYPos() == 150);
		assertTrue("sprite should be rotated to stand upwards", ball.getRotation() == -Math.PI/2);
	}

	@Test
	public void testBallReboundOne() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(-1);
		ball.setYVelocity(0);
		ball.rebound(3*Math.PI/4);

		assertTrue("The ball is now travelling straight down", ball.getYVelocity() == 1);
		assertTrue("The ball is no longer moving in the x direction", ball.getXVelocity() == 0);
	}

	@Test
	public void testBallReboundTwo() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(0);
		ball.setYVelocity(-1);
		ball.rebound(3*Math.PI/4);

		assertTrue("The ball is now travelling to the right", ball.getXVelocity() == 1);
		assertTrue("The ball is no longer moving in the y direction", ball.getYVelocity() == 0);
	}

	@Test
	public void testBallReboundThree() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(0);
		ball.setYVelocity(1);
		ball.rebound(Math.PI/4);

		assertTrue("The ball is now travelling to the right", ball.getXVelocity() == 1);
		assertTrue("The ball is no longer moving in the y direction", ball.getYVelocity() == 0);
	}

	@Test
	public void testBallReboundFour() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(1);
		ball.setYVelocity(0);
		ball.rebound(3*Math.PI/4);

		assertTrue("The ball is now travelling upwards", ball.getYVelocity() == -1);
		assertTrue("The ball is no longer moving in the x direction", ball.getXVelocity() == 0);
	}

	@Test
	public void testBallReboundFive() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(0);
		ball.setYVelocity(1);
		ball.rebound(3*Math.PI/4);

		assertTrue("The ball is now travelling to the left", ball.getXVelocity() == -1);
		assertTrue("The ball is no longer moving in the y direction", ball.getYVelocity() == 0);
	}

	@Test
	public void testBallReboundSix() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(-1);
		ball.setYVelocity(-1);
		ball.rebound(3*Math.PI/4);

		assertTrue("The ball is now travelling to the right", ball.getXVelocity() == 1);
		assertTrue("The ball is now travelling downwards", ball.getYVelocity() == 1);
	}

	@Test
	public void testBallReboundSeven() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(1);
		ball.setYVelocity(4);
		ball.rebound(Math.PI/8);

		assertTrue("The ball is now travelling more to the right", ball.getXVelocity() > 3);
		assertTrue("The ball is now travelling upwards", ball.getYVelocity() < -1);
	}

	@Test
	public void testBallReboundLeftBoundaryOne() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(-1);
		ball.setYVelocity(0);
		ball.rebound(Math.PI/2);

		assertTrue("The ball is now travelling to the right", ball.getXVelocity() == 1);
		assertTrue("The ball is still not moving in the y direction", ball.getYVelocity() == 0);
	}

	@Test
	public void testBallReboundLeftBoundaryTwo() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(-1);
		ball.setYVelocity(5);
		ball.rebound(Math.PI/2);

		assertTrue("The ball is now travelling to the right", ball.getXVelocity() == 1);
		assertTrue("The ball is moving at the same rate in the y direction", ball.getYVelocity() == 5);
	}

	@Test
	public void testBallReboundBottomBoundary() {

		Ball ball = new Ball(100, 100);
		ball.setXVelocity(0);
		ball.setYVelocity(1);
		ball.rebound(0);

		assertTrue("The ball is now travelling upwards", ball.getYVelocity() == -1);
		assertTrue("The ball is still not moving in the x direction", ball.getXVelocity() == 0);
	}
}
