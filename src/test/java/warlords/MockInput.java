package warlords;

public class MockInput implements IUserInput {

	private InputType inputType;

	@Override
	public InputType getInputType() {
		return inputType;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
}
