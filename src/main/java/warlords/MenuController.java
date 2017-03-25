package warlords;

import javafx.application.Platform;
import java.util.ArrayList;

public class MenuController {

	private int width;
	private int height;
	private GameView gameView;
	private ArrayList<MenuItem> menuItems = new ArrayList<>();
	private ArrayList<IUserInput> userInputs;
	private int selectedItem = -1;
	private boolean doStartGame = false;

	public MenuController(ArrayList<IUserInput> userInputs, int width, int height, GameView gameView) {

		this.userInputs = userInputs;
		this.width = width;
		this.height = height;
		this.gameView = gameView;

		menuItems.add(new MenuItem("Start the game", () -> doStartGame = true));
		menuItems.add(new MenuItem("Do nothing", () -> System.out.println("Nothing has been done!")));
		menuItems.add(new MenuItem("Quit", Platform::exit));
		changeSelection(1);
	}

	public void runLoop() {
		checkUserInput();
		gameView.drawMenuItems(menuItems);
	}

	public boolean doStartGame() {
		return doStartGame;
	}

	private void checkUserInput() {

		InputType input = userInputs.get(0).getInputType();
		if (input != null) {
			switch (input) {
				case MENU_UP:
					changeSelection(-1);
					break;
				case MENU_DOWN:
					changeSelection(1);
					break;
				case MENU_SELECT:
					menuItems.get(selectedItem).runCallback();
					break;
			}
		}
	}

	private void changeSelection(int direction) {

		int newSelection = selectedItem + direction;
		if (newSelection < 0 || newSelection >= menuItems.size()) {
			return;
		}
		selectedItem = newSelection;
		for (MenuItem menuItem : menuItems) {
			menuItem.setSelected(false);
		}
		menuItems.get(selectedItem).setSelected(true);
	}
}
