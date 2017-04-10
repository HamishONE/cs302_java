package warlords;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Manages the displaying the menu screen and responds to user selections.
 */
public class MenuController {

	private static int TRANSITION_TIME = 300; //ms

	private GameView gameView;
	private Menu currentMenu;
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

		Menu gameStyleMenu = new Menu();
		gameStyleMenu.add(new MenuItem("Neolithic", () -> {
			game.setAge(Ages.NEOLITHIC);
			startGame();
		}));
		gameStyleMenu.add(new MenuItem("Medieval", () -> {
			game.setAge(Ages.MEDIEVAL);
			startGame();
		}));
		gameStyleMenu.add(new MenuItem("Industrial", () -> {
			game.setAge(Ages.INDUSTRIAL);
			startGame();
		}));
		gameStyleMenu.add(new MenuItem("Space", () -> {
			game.setAge(Ages.SPACE);
			startGame();
		}));

		Menu gameModeMenu = new Menu();
		gameModeMenu.add(new MenuItem("Single game", gameStyleMenu));
		gameModeMenu.add(new MenuItem("Campaign Mode", this::startGame));

		Menu numPlayersMenu = new Menu();
		numPlayersMenu.add(new MenuItem("Single player", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(1)));
		numPlayersMenu.add(new MenuItem("Two players", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(2)));
		numPlayersMenu.add(new MenuItem("Three players", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(3)));
		numPlayersMenu.add(new MenuItem("Four players", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(4)));
		numPlayersMenu.add(new MenuItem("AI Demo", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(0)));

		currentMenu = new Menu();
		currentMenu.add(new MenuItem("New game", numPlayersMenu));
		currentMenu.add(new MenuItem("High scores", () -> System.out.println("GOTO High Scores")));
		currentMenu.add(new MenuItem("Quit", Platform::exit));
	}

	/**
	 * Tells the main controller to start the game.
	 */
	private void startGame() {
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
	 * @return whether the game should be started
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
			InputType input = userInput.getInputType(true);
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
					case RIGHT:
					case MENU_SELECT:
						MenuItem menuItem = currentMenu.getSelectedItem();
						if (menuItem.hasCallback()) {
							menuItem.runCallback();
						}
						if (menuItem.hasSubmenu()) {
							// Store the current menu in a stack
							previousMenus.push(currentMenu);
							currentMenu = menuItem.getSubmenu();
							transitionTimeRemaining = TRANSITION_TIME;
							lastTimestamp = System.currentTimeMillis();
							transitionForward = true;
						}
						break;
					// If the input to exit restore the previous menu from the stack
					case LEFT:
					case EXIT:
						if (!previousMenus.empty()) {
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
