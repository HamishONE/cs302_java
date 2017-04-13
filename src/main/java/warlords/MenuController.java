package warlords;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Manages the displaying the menu screen and responds to user selections.
 */
public class MenuController {

	public static final String menuBackgroundPath = "/rock.png";
	private static int TRANSITION_TIME = 300; //ms

	private GameView gameView;
	private Menu currentMenu;
	private ArrayList<IUserInput> userInputs;
	private boolean doStartGame = false;
	private boolean doGoToScoreBoard = false;
	/**
	 * Boolean to contain state of credits visibility
	 */
	private boolean showCredits = false;
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
		gameStyleMenu.add(new MenuItem("Neolithic", () -> startGame(Ages.NEOLITHIC, false)));
		gameStyleMenu.add(new MenuItem("Medieval", () -> startGame(Ages.MEDIEVAL, false)));
		gameStyleMenu.add(new MenuItem("Industrial", () -> startGame(Ages.INDUSTRIAL, false)));
		gameStyleMenu.add(new MenuItem("Space", () -> startGame(Ages.SPACE, false)));

		Menu gameModeMenu = new Menu();
		gameModeMenu.add(new MenuItem("Single game", gameStyleMenu));
		gameModeMenu.add(new MenuItem("Campaign Mode", () -> startGame(Ages.values()[0], true)));

		Menu numPlayersMenu = new Menu();
		numPlayersMenu.add(new MenuItem("Single player", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(1)));
		numPlayersMenu.add(new MenuItem("Two players", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(2)));
		numPlayersMenu.add(new MenuItem("Three players", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(3)));
		numPlayersMenu.add(new MenuItem("Four players", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(4)));
		numPlayersMenu.add(new MenuItem("AI Demo", gameModeMenu).setCallback(() -> game.setNumHumanPlayers(0)));

		currentMenu = new Menu();
		currentMenu.setTitle("Age of Balls");
		currentMenu.add(new MenuItem("New game", numPlayersMenu));
		currentMenu.add(new MenuItem("High scores", () -> doGoToScoreBoard = true));
		currentMenu.add(new MenuItem("Instructions", Platform::exit));
		currentMenu.add(new MenuItem("Credits", () -> showCredits = true));
	}

	/**
	 * Tells the main controller to start the game.
	 * @param age The age to start with.
	 * @param campaignMode Whether or not to use campaign mode.
	 */
	private void startGame(Ages age, boolean campaignMode) {
		game.setAge(age);
		game.setCampaignMode(campaignMode);
		doStartGame = true;
	}

	/**
	 * Check and respond to any user inputs and redraw the menu items.
	 */
	public void runLoop() {

		//Show credits when selected
		if (showCredits) {
			checkCreditExit();
			gameView.drawStoryMessage("/rock.png", "" +
					"Thanks to those who made their work available to the public for reuse. \n" +
					"Credit for the music goes to:\n" +
					"  • Alexandr Zhelanov for 'Heroic Minority'\n" +
					"  • HorrorPen for 'Small Loss'\n" +
					"  • Matthew Pablo for 'Theme of Agrual'\n" +
					"  • Wolfgang for 'Battle Theme'\n" +
					"  • Telaron for 'Space Marines'\n" +
					"\n" +
					"Credit for the bounce sounds goes to:\n" +
					"  • theshaggyfreak for 'thump2' and 'thump3'\n" +
					"  • SamsterBirdies for 'Thump'\n" +
					"\n" +
					"Credit for the background images goes to:\n" +
					"  • Cuzco for the space background\n" +
					"  • Zeyu Ren for the menu background and neolithic background\n" +
					"  • OlKu for the medieval background\n" +
					"  • Austin Boucher for the industrial background\n" +
					"\n" +
					"\n" +
					"All other sprites were developed in house by Roman Amor");
			return;
		}

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
		if (doStartGame) {
			doStartGame = false;
			return true;
		}
		return false;
	}

	/**
	 * @return if the game should be started, going straight to the score board
	 */
	public boolean doGoToScoreBoard() {
		if (doGoToScoreBoard) {
			doGoToScoreBoard = false;
			return true;
		}
		return false;
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

	/**
	 * 	Checks if player has pressed any of the mapped keys
	 * 	Sets boolean {@link MenuController#showCredits} to false to close the credits
	 */
	private void checkCreditExit() {

		// Loop through each player's user input
		for (IUserInput userInput : userInputs) {
			InputType input = userInput.getInputType(true);
			// If the user has made an input check which type it is
			if (input != null) {
				showCredits = false;
			}
		}
	}
}
