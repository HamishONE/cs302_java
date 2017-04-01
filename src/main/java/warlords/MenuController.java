package warlords;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Stack;

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
	private Stack<ArrayList<MenuItem>> previousMenus = new Stack<>();
	private GameState gameState;

	/**
	 * Create a new instance of the menu screen
	 * @param userInputs the {@link IUserInput} for each player with control of the menu
	 * @param width the width of the game window
	 * @param height the height of the game window
	 * @param gameView the view class used to draw the menu
	 * @param gameState the game state model for menu selections to update
	 */
	public MenuController(ArrayList<IUserInput> userInputs, int width, int height, GameView gameView, GameState gameState) {

		this.userInputs = userInputs;
		this.width = width;
		this.height = height;
		this.gameView = gameView;
		this.gameState = gameState;

		// Setup a submenu with items for selecting the number of players
		ArrayList<MenuItem> startGameMenu = new ArrayList<>();
		startGameMenu.add(new MenuItem("One player", () -> startGame(1)));
		startGameMenu.add(new MenuItem("Two players", () -> startGame(2)));
		startGameMenu.add(new MenuItem("Three players", () -> startGame(3)));
		startGameMenu.add(new MenuItem("Four players", () -> startGame(4)));

		// Setup the main menu items
		menuItems.add(new MenuItem("Start the game", startGameMenu));
		menuItems.add(new MenuItem("Do nothing", () -> System.out.println("Nothing has been done!")));
		menuItems.add(new MenuItem("Quit", Platform::exit));
		changeSelection(1);
	}

	/**
	 * Sets the number of players in the game model and tells the main controller to start the game.
	 * @param numHumanPlayers The number of human players.
	 */
	private void startGame(int numHumanPlayers) {
		gameState.setNumHumanPlayers(numHumanPlayers);
		doStartGame = true;
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
					// If the input is to select a menu item open it's submenu or run it's callback method if it is an end node
					case MENU_SELECT:
						MenuItem menuItem = menuItems.get(selectedItem);
						if (menuItem.hasSubmenu()) {
							// Store the current menu in a stack
							previousMenus.push(menuItems);
							menuItems = menuItem.getSubmenu();
							selectedItem = 0;
							menuItems.get(selectedItem).setSelected(true);
						} else {
							menuItem.runCallback();
						}
						break;
					// If the input to exit restore the previous menu from the stack
					case EXIT:
						if (!previousMenus.empty()) {
							menuItems = previousMenus.pop();
							selectedItem = 0;
							menuItems.get(selectedItem).setSelected(true);
						}
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
