package warlords;

public class MenuItem {

	private String text;
	private boolean isSelected = false;
	private IMenuCallback callback;

	public MenuItem(String text, IMenuCallback callback) {
		this.text = text;
		this.callback = callback;
	}

	public void runCallback() {
		callback.run();
	}

	public String getText() {
		return text;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}
