package warlordstest;

import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import warlords.*;

public class WarlordsTest extends TestSuite {

	private IGame game;
	private IBall ball;

	//private IPaddle paddle;
	//private IWall player1Wall;
	//private IWarlord player1;
	//private IWarlord player2;

	@Before
	public void setUp(){

		//Instantiate objects to initialise the fields - and preferably no other game objects, to minimise the possibility of conflicts
		//All game objects should be instantiated at coordinates (0,0) with zero velocity
		Ball ball = new Ball();
		this.ball = ball;
		this.game = new Game(ball);
	}

	@Test
	public void testBallMovement(){

		this.ball.setXPos(500);
		this.ball.setYPos(500);

		this.ball.setXVelocity(50);
		this.ball.setYVelocity(50);

		this.game.tick();

		assertTrue("If unimpeded, the ball should be moved by its velocity in each direction", this.ball.getXPos() == 550 && this.ball.getYPos() ==  550);
	}

	/*
	@Test
	public void testBallCollisionWithBoundary(){

		this.ball.setXPos(10);
		this.ball.setYPos(500);

		this.ball.setXVelocity(-50);
		this.ball.setYVelocity(50);

		this.game.tick();

		assertTrue("The ball should remain within bounds", this.ball.getXPos() >= 0);
		assertTrue("The ball's velocity should be reversed in the direction of the collision", this.ball.getXVelocity() == 50 && this.ball.getYVelocity() == 50);


	}

	@Test
	public void testBallCollisionWithPaddle(){

		this.ball.setXPos(500);
		this.ball.setYPos(495);
		this.ball.setXVelocity(10);
		this.ball.setYVelocity(10);

		this.paddle.setXPos(500);
		this.paddle.setYpos(500);

		this.game.tick();

		assertTrue("The ball should not travel through the paddle", this.ball.getYPos() <= 500);
		assertTrue("The ball's velocity should be reversed in the direction of the collision", this.ball.getXVelocity() == 10 && this.ball.getYVelocity() == -10);

	}

	@Test
	public void testBallCollisionWithWall(){

		this.ball.setXPos(500);
		this.ball.setYPos(495);
		this.ball.setXVelocity(10);
		this.ball.setYVelocity(10);

		this.player1Wall.setXPos(500);
		this.player1Wall.setYpos(500);

		assertFalse("The wall should not be destroyed yet", this.player1Wall.isDestroyed());

		this.game.tick();

		assertTrue("The ball should not travel through the wall", this.ball.getYPos() <= 500);
		assertTrue("The ball's velocity should be reversed in the direction of the collision", this.ball.getXVelocity() == 10 && this.ball.getYVelocity() == -10);
		assertTrue("The wall should be destroyed", this.player1Wall.isDestroyed());

	}

	@Test
	public void testBallCollisionWithWarlord(){

		this.ball.setXPos(500);
		this.ball.setYPos(495);
		this.ball.setXVelocity(10);
		this.ball.setYVelocity(10);

		this.player1.setXPos(500);
		this.player1.setYpos(500);

		assertFalse("The warlord should not be dead yet", this.player1.isDead());

		this.game.tick();

		assertTrue("The warlord should be dead", this.player1.isDead());

	}

	@Test
	public void testBallCollisionAtHighSpeed(){

		this.ball.setXPos(500);
		this.ball.setYPos(495);
		this.ball.setXVelocity(300);
		this.ball.setYVelocity(300);

		this.paddle.setXPos(500);
		this.paddle.setYpos(500);

		this.game.tick();

		assertTrue("The ball should not travel through the paddle, even though it isn't within its bounds on any frame", this.ball.getYPos() <= 500);
		assertTrue("The ball's velocity should be reversed in the direction of the collision", this.ball.getXVelocity() == 300 && this.ball.getYVelocity() == -300);

	}

	@Test
	public void testGameEndFromKnockout(){

		assertFalse("The game should not be finished yet", this.game.isFinished());
		assertFalse("Player 2 should not have won yet", this.player2.hasWon());

		this.ball.setXPos(500);
		this.ball.setYPos(495);
		this.ball.setXVelocity(10);
		this.ball.setYVelocity(10);

		this.player1.setXPos(500);
		this.player1.setYpos(500);

		this.game.tick();

		assertTrue("The game should be finished", this.game.isFinished());
		assertTrue("Player 2 should have won", this.player2.hasWon());

	}

	@Test
	public void testGameEndFromTimeout(){

		assertFalse("The game should not be finished yet", this.game.isFinished());
		assertFalse("Player 1 should not have won yet", this.player1.hasWon());

		this.game.setTimeRemaining(0);

		this.game.tick();

		assertTrue("The game should be finished", this.game.isFinished());
		assertTrue("Player 1 should have won", this.player1.hasWon());

	}
	*/


}
