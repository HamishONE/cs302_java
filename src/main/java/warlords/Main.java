package warlords;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class contains the main() method for the project that launches a JavaFX
 * stage and passes control off to the {@link MainController}.
 */
public class Main extends Application {

	private static Stage stage;
	private MainController mainController;
	private int counter = 0;

	public static boolean isDebugMode() {
		return System.getProperty("debug") != null && System.getProperty("debug").equalsIgnoreCase("true");
	}

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

		// Show the primary window with a simple title
		stage = primaryStage;
		primaryStage.setTitle("CS302 Java Game");
		primaryStage.show();

		// Instantiate and start the main controller
		mainController = new MainController(600, 800);
		mainController.start();

		// Run the MainController loop at the default 60fps [less in debug mode]
		int countMax = isDebugMode() ? 10 : 0;
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (counter++ > countMax) {
					counter = 0;
					mainController.runLoop();
				}
			}
		};
		timer.start();
	}
}
