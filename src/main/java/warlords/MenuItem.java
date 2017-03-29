package warlords;

/**
 * Represents a single selectable item on a menu screen.
 */
public class MenuItem {

	private String text;
	private boolean isSelected = false;
	private IMenuCallback callback;

	/**
	 * Creates a new menu item that is not currently selected.
	 * @param text the text to be displayed to the user
	 * @param callback will be called when the user selects the menu item
	 */
	public MenuItem(String text, IMenuCallback callback) {
		this.text = text;
		this.callback = callback;
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
