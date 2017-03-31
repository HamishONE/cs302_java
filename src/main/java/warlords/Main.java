package warlords;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

/**
 * This class contains the main() method for the project that launches a JavaFX
 * stage and passes control off to the {@link MainController}.
 */
public class Main extends Application {

	private static Stage stage;
	private MainController mainController;

	/**
	 * Change the scene attached to the primary stage
	 * @param scene the JavaFX scene to be attached
	 */
	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}

	/**
	 * Starts the game by launching a JavaFX stage and passing control off to the {@link MainController}.
	 * @param args [unused]
	 */
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {

		//Use toolkit to get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int gameWidth;
		int gameHeight;

		//If screen is larger than a certain threshold, have a bigger game screen
		if(screenSize.getHeight() < 800 ) {
			gameWidth = 800;
			gameHeight = 600;
		}
		else {
			gameWidth = (int)(800*1.3333);
			gameHeight = (int)(600*1.3333);
		}

		// Show the primary window with a simple title
		stage = primaryStage;
		primaryStage.setTitle("CS302 Java Game");
		primaryStage.show();

		// Instantiate and start the main controller
		mainController = new MainController(gameHeight, gameWidth);
		mainController.start();

		// Run the MainController loop at the default 60fps (aprox.)
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				mainController.runLoop();
			}
		};
		timer.start();
	}
}
