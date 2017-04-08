package warlords;

public class MockInput implements IUserInput {

	private InputType inputType;

	@Override
	public InputType getInputType() {
		return inputType;
	}

	@Override
	public String getCharInput() {
		return null;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
}
