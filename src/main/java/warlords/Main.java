package warlords;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main /*extends Application*/ {

	public static void main(String[] args) {
		//launch();
		new MainController().beginGame();
	}

	//@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("CS302 Java Game");
		primaryStage.show();
	}
}
