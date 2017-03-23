package warlords;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private static Stage stage;
	private MainController mainController;

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

		mainController = new MainController();

		Timeline timeline = new Timeline(
			new KeyFrame(
				Duration.seconds(0),
				event -> mainController.runLoop()
			),
			new KeyFrame(Duration.millis(10))
		);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		mainController.beginGame();
	}
}
