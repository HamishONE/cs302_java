package warlords;

import javafx.application.Platform;
import java.util.ArrayList;

/**
 * Manages the displaying the menu screen and responds to user selections.
 */
public class MenuController {

	private int width;
	private int height;
	private GameView gameView;
	private ArrayList<MenuItem> menuItems = new ArrayList<>();
	private ArrayList<IUserInput> userInputs;
	private int selectedItem = -1;
	private boolean doStartGame = false;

	/**
	 * Create a new instance of the menu screen
	 * @param userInputs the {@link IUserInput} for each player with control of the menu
	 * @param width the width of the game window
	 * @param height the height of the game window
	 * @param gameView the view class used to draw the menu
	 */
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

	/**
	 * Check and respond to any user inputs and redraw the menu items.
	 */
	public void runLoop() {
		checkUserInput();
		gameView.drawMenuItems(menuItems);
	}

	/**
	 * Checks if the game should be started
	 * @return whether the game shoudl be started
	 */
	public boolean doStartGame() {
		return doStartGame;
	}

	/**
	 * Resets the menu controller to it's initial state
	 */
	public void reset() {
		doStartGame = false;
	}

	/**
	 * Checks if any players have inputted a command and changes the menu selection or executes a menu item
	 * depending on the input.
	 */
	private void checkUserInput() {

		// Loop through each player's user input
		for (IUserInput userInput : userInputs) {
			InputType input = userInput.getInputType();
			// If the user has made an input check which type it is
			if (input != null) {
				switch (input) {
					// If the input is to move the menu up or down change the selection
					case MENU_UP:
						changeSelection(-1);
						break;
					case MENU_DOWN:
						changeSelection(1);
						break;
					// If the input is to select a menu item run it's callback method
					case MENU_SELECT:
						menuItems.get(selectedItem).runCallback();
						break;
				}
			}
		}
	}

	/**
	 * Change which menu item is currently selected
	 * @param direction if -1 move it to the previous item, if 1 move it to the next item
	 */
	private void changeSelection(int direction) {

		// Add the input to the current selected item index
		selectedItem += direction;

		// If this new index is out of bounds wrap it around
		if (selectedItem < 0) {
			selectedItem = menuItems.size() - 1;
		}
		if (selectedItem >= menuItems.size()) {
			selectedItem = 0;
		}

		// Set all the menu items to not selected
		for (MenuItem menuItem : menuItems) {
			menuItem.setSelected(false);
		}

		// Set the menu item at the new index as selected
		menuItems.get(selectedItem).setSelected(true);
	}
}
