package warlords;

/**
 * Represents a single selectable item on a menu screen.
 */
public class MenuItem {

	private String text;
	private boolean isSelected = false;
	private IMenuCallback callback;
	private Menu submenu;

	/**
	 * Creates a new menu item with no submenu that is not currently selected.
	 * @param text the text to be displayed to the user
	 * @param callback will be called when the user selects the menu item
	 */
	public MenuItem(String text, IMenuCallback callback) {
		this.text = text;
		this.callback = callback;
	}

	/**
	 * Creates a new menu item with a submenu that is not currently selected.
	 * @param text the text to be displayed to the user
	 * @param submenu a list of all the menu items in the next menu
	 */
	public MenuItem(String text, Menu submenu) {
		this.text = text;
		this.submenu = submenu;
	}

	/**
	 * @return If this menu item has a submenu or not.
	 */
	public boolean hasSubmenu() {
		return submenu != null;
	}

	/**
	 * @return A list of menu items to be displayed when this item is selected.
	 */
	public Menu getSubmenu() {
		return submenu;
	}

	/**
	 * Run the callback
	 */
	public void runCallback() {
		callback.run();
	}

	/**
	 * @return The text to be shown to the user.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return Whether or not the menu item is selected.
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param selected Whether or not the menu item is selected.
	 */
	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}
