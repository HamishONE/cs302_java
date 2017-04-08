package warlords;

import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import warlordstest.*;

import java.util.ArrayList;

import static org.junit.Assert.*;



public class BallBounceTest extends TestSuite {

	private IGame game;
	private IBall ball;
	private IPaddle paddle;
	//private IWall player1Wall;
	private IWarlord player1;
	private IWarlord player2;

	@Before
	public void setUp(){
		Ages age = Ages.MEDIEVAL;
		Game game = new Game(200, 200);
		Ball ball = new Ball(100, 100, age);
		Warlord player1 = new Warlord(0, 0, age, 0);
		Boundary boundary = new Boundary(age);

		ArrayList<Paddle> paddles = new ArrayList<>();
		ArrayList<Wall> walls = new ArrayList<>();
		ArrayList<Warlord> warlords = new ArrayList<>();
		warlords.add(player1);

		GameController gameController = new GameController(null, paddles, new ArrayList<>(), walls, warlords, game, ball, boundary);
		gameController.beginGame();

		this.game = gameController;
		this.ball = ball;
		this.paddle = paddle;
		//this.player1Wall = wall;
		this.player1 = player1;
		this.player2 = player2;
	}

	@Test
	public void TestCeilingBounce() {

		ball.setXPos(100);
		ball.setYPos(5);

		ball.setXVelocity(0);
		ball.setYVelocity(-10);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 0);
		assertTrue("y velocity is as given", ball.getYVelocity() == 10);
		assertTrue("x position is as given", ball.getXPos() == 100);
		assertTrue("y position is as given", ball.getYPos() == 5);
	}

	@Test
	public void TestLeftWallBounce() {

		ball.setXPos(50);
		ball.setYPos(100);

		ball.setXVelocity(-100);
		ball.setYVelocity(0);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 100);
		assertTrue("y velocity is as given", ball.getYVelocity() == 0);
		assertTrue("x position is as given", ball.getXPos() == 50);
		assertTrue("y position is as given", ball.getYPos() == 100);
	}

	@Test
	public void TestRightWallBounce() {

		ball.setXPos(195);
		ball.setYPos(100);

		ball.setXVelocity(10);
		ball.setYVelocity(0);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == -10);
		assertTrue("y velocity is as given", ball.getYVelocity() == 0);
		assertTrue("x position is as given", ball.getXPos() == 195);
		assertTrue("y position is as given", ball.getYPos() == 100);
	}

	@Test
	public void TestFloorBounce() {

		ball.setXPos(100);
		ball.setYPos(195);

		ball.setXVelocity(0);
		ball.setYVelocity(10);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 0);
		assertTrue("y velocity is as given", ball.getYVelocity() == -10);
		assertTrue("x position is as given", ball.getXPos() == 100);
		assertTrue("y position is as given", ball.getYPos() == 195);
	}

	@Test
	public void TestFloorSlowBounce() {

		ball.setXPos(100);
		ball.setYPos(199);

		ball.setXVelocity(0);
		ball.setYVelocity(1);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 0);
		assertTrue("y velocity is as given", ball.getYVelocity() == -1);
		assertTrue("x position is as given", ball.getXPos() == 100);
		assertTrue("y position is as given", ball.getYPos() == 200);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 0);
		assertTrue("y velocity is as given", ball.getYVelocity() == -1);
		assertTrue("x position is as given", ball.getXPos() == 100);
		assertTrue("y position is as given", ball.getYPos() == 199);
	}

	@Test
	public void TestFloorAngleBounce90() {

		ball.setXPos(100);
		ball.setYPos(195);

		ball.setXVelocity(10);
		ball.setYVelocity(10);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 10);
		assertTrue("y velocity is as given", ball.getYVelocity() == -10);
		assertTrue("x position is as given", ball.getXPos() == 110);
		assertTrue("y position is as given", ball.getYPos() == 195);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 10);
		assertTrue("y velocity is as given", ball.getYVelocity() == -10);
		assertTrue("x position is as given", ball.getXPos() == 120);
		assertTrue("y position is as given", ball.getYPos() == 185);
	}

	@Test
	public void TestFloorAngleBounce45() {

		ball.setXPos(100);
		ball.setYPos(195);

		ball.setXVelocity(5);
		ball.setYVelocity(10);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 5);
		assertTrue("y velocity is as given", ball.getYVelocity() == -10);
		assertTrue("x position is as given", ball.getXPos() == 105);
		assertTrue("y position is as given", ball.getYPos() == 195);

		game.tick();

		assertTrue("x velocity is as given", ball.getXVelocity() == 5);
		assertTrue("y velocity is as given", ball.getYVelocity() == -10);
		assertTrue("x position is as given", ball.getXPos() == 110);
		assertTrue("y position is as given", ball.getYPos() == 185);
	}
}
