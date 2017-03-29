package warlords;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	static private int countMax = 0;
	static {
		if (System.getProperty("debug") != null && System.getProperty("debug").equalsIgnoreCase("true")) {
			countMax = 10;
		}
	}

	private static Stage stage;
	private MainController mainController;
	private int counter = 0;

	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {

		stage = primaryStage;
		primaryStage.setTitle("CS302 Java Game");
		primaryStage.show();

		mainController = new MainController(600, 800);
		mainController.start();

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
