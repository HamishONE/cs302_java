package warlords;

import java.util.ArrayList;

/**
 * Represents a collection of {@link MenuItem} instances that make up a single menu screen.
 */
public class Menu {

	private ArrayList<MenuItem> menuItems = new ArrayList<>();
	private String title;

	/**
	 * @return An ordered list of menu items in the collection.
	 */
	public ArrayList<MenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * @return The index of the menu item currently selected.
	 */
	private int getSelected() {
		int i = 0;
		for (MenuItem menuItem : menuItems) {
			if (menuItem.isSelected()) {
				return i;
			}
			++i;
		}
		return -1;
	}

	/**
	 * @return The {@link MenuItem} currently selected.
	 */
	public MenuItem getSelectedItem() {
		return menuItems.get(getSelected());
	}

	/**
	 * Add a menu item to the collection.
	 * The first item added will be selected by default.
	 * @param item The menu item to add.
	 */
	public void add(MenuItem item) {
		menuItems.add(item);
		if (menuItems.size() == 1) {
			menuItems.get(0).setSelected(true);
		}
	}

	/**
	 * Change which menu item is currently selected
	 * @param direction if -1 move it to the previous item, if 1 move it to the next item
	 */
	public void changeSelection(int direction) {

		int selectedItem = getSelected();

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

	/**
	 * Getter for the title of the menu instance
	 *
	 * @return title of the menu
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for the menu instance
	 *
	 * @param title title of the menu
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
