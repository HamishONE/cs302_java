package warlords;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Manages the displaying the menu screen and responds to user selections.
 */
public class MenuController {

	private static int TRANSITION_TIME = 300; //ms

	private GameView gameView;
	private Menu currentMenu;
	private Menu previousMenuForward;
	private ArrayList<IUserInput> userInputs;
	private boolean doStartGame = false;
	private Stack<Menu> previousMenus = new Stack<>();
	private Game game;
	private int transitionTimeRemaining = 0;
	private long lastTimestamp;
	private boolean transitionForward = true;

	/**
	 * Create a new instance of the menu screen
	 * @param userInputs the {@link IUserInput} for each player with control of the menu
	 * @param gameView the view class used to draw the menu
	 * @param game the game model for menu selections to update
	 */
	public MenuController(ArrayList<IUserInput> userInputs, GameView gameView, Game game) {

		this.userInputs = userInputs;
		this.gameView = gameView;
		this.game = game;

		// Setup a submenu with items for selecting the number of players
		Menu startGameMenu = new Menu();
		startGameMenu.add(new MenuItem("One player", () -> startGame(1)));
		startGameMenu.add(new MenuItem("Two players", () -> startGame(2)));
		startGameMenu.add(new MenuItem("Three players", () -> startGame(3)));
		startGameMenu.add(new MenuItem("Four players", () -> startGame(4)));
		startGameMenu.add(new MenuItem("AI only demo", () -> startGame(0)));

		// Setup the main menu items
		currentMenu = new Menu();
		currentMenu.add(new MenuItem("Start the game", startGameMenu));
		currentMenu.add(new MenuItem("Do nothing", () -> System.out.println("Nothing has been done!")));
		currentMenu.add(new MenuItem("Quit", Platform::exit));
	}

	/**
	 * Sets the number of players in the game model and tells the main controller to start the game.
	 * @param numHumanPlayers The number of human players.
	 */
	private void startGame(int numHumanPlayers) {
		game.setNumHumanPlayers(numHumanPlayers);
		doStartGame = true;
	}

	/**
	 * Check and respond to any user inputs and redraw the menu items.
	 */
	public void runLoop() {
		checkUserInput();

		if (transitionTimeRemaining > 0) {
			transitionTimeRemaining -= (System.currentTimeMillis() - lastTimestamp);
			lastTimestamp = System.currentTimeMillis();
			if (transitionTimeRemaining < 0) {
				transitionTimeRemaining = 0;
			}
		}

		Menu oldMenu = previousMenus.isEmpty() ? new Menu() : previousMenus.peek();
		Menu newMenu = currentMenu.getSelectedItem().hasSubmenu() ? currentMenu.getSelectedItem().getSubmenu() : new Menu();

		if (transitionForward) {
			gameView.drawAnimatedMenu(oldMenu, currentMenu, newMenu, (double) -transitionTimeRemaining / TRANSITION_TIME);
		} else {
			gameView.drawAnimatedMenu(oldMenu, currentMenu, newMenu, (double) transitionTimeRemaining / TRANSITION_TIME);
		}
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
						currentMenu.changeSelection(-1);
						break;
					case MENU_DOWN:
						currentMenu.changeSelection(1);
						break;
					// If the input is to select a menu item open it's submenu or run it's callback method if it is an end node
					case MENU_SELECT:
						MenuItem menuItem = currentMenu.getSelectedItem();
						if (menuItem.hasSubmenu()) {
							// Store the current menu in a stack
							previousMenus.push(currentMenu);
							currentMenu = menuItem.getSubmenu();
							transitionTimeRemaining = TRANSITION_TIME;
							lastTimestamp = System.currentTimeMillis();
							transitionForward = true;
						} else {
							menuItem.runCallback();
						}
						break;
					// If the input to exit restore the previous menu from the stack
					case EXIT:
						if (!previousMenus.empty()) {
							previousMenuForward = currentMenu;
							currentMenu = previousMenus.pop();
							transitionTimeRemaining = TRANSITION_TIME;
							lastTimestamp = System.currentTimeMillis();
							transitionForward = false;
						}
				}
			}
		}
	}
}
