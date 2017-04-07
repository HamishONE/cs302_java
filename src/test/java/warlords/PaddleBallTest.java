package warlords;

import junit.framework.TestSuite;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import static org.junit.Assert.*;

public class PaddleBallTest extends TestSuite {

	private boolean isBallInPaddle(Ball ball, Paddle paddle) {
		Rectangle2D ballRect = new Rectangle2D.Double(ball.getXPosReal() - ball.getWidth()/2,
				ball.getYPosReal() - ball.getHeight()/2, ball.getWidth(), ball.getHeight());
		return paddle.getRectangle().intersects(ballRect) || paddle.getRectangle().contains(ballRect);
	}

	@Test
	public void test() {
		Ages age = Ages.MEDIEVAL;
		// Create a game with a ball, a paddle and two warlords
		Game game = new Game(500, 500);
		Ball ball = new Ball(100, 100, age);
		Paddle paddle = new Paddle(0, 0, age, 0D, game, Collections.singletonList(ball));
		Warlord player1 = new Warlord(500, 500, age);
		Warlord player2 = new Warlord(500, 500, age);
		Boundary boundary = new Boundary(age);

		// Create lists for the paddles, walls and warlords
		ArrayList<Paddle> paddles = new ArrayList<>();
		paddles.add(paddle);
		ArrayList<Wall> walls = new ArrayList<>();
		ArrayList<Warlord> warlords = new ArrayList<>();
		warlords.add(player1);
		warlords.add(player2);

		// Mock player 1 pressing left each game tick
		MockInput mockInput = new MockInput();
		mockInput.setInputType(InputType.LEFT);
		ArrayList<IUserInput> players = new ArrayList<>();
		players.add(mockInput);

		// Begin the game
		GameController gameController = new GameController(null, paddles, players, walls, warlords, game, ball, boundary);
		gameController.beginGame();

		// Rotate the paddle to be vertical
		for (int i=0; i<100000; i++) {
			paddle.moveRight();
		}

		// Move the location of the bottom of the paddle
		double paddleBottomY = paddle.getYPosReal() + paddle.getWidth()/2;
		double paddleBottomX = paddle.getXPosReal();

		// Place the ball just below the paddle
		ball.y = paddleBottomY + ball.getHeight()/2 + 1;
		ball.x = paddleBottomX;
		ball.setXVelocity(0);
		ball.setYVelocity(-5);

		// Move the paddle left/down and check the ball is never in the paddle
		assertFalse("ball not in paddle BEFORE game tick", isBallInPaddle(ball, paddle));
		gameController.tick();
		assertFalse("ball not in paddle AFTER game tick", isBallInPaddle(ball, paddle));
	}
}
