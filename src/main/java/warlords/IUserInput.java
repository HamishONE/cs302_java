package warlords;

/**
 * Interface for finding the input operation made by a player or AI.
 */
public interface IUserInput {

	/**
	 * Get the last input operation performed by the user.
	 * Should return null after the input is stopped.
	 * @return the input operation
	 */
	InputType getInputType();

	/**
	 * Get the last input from the user as a string.
	 * Should return null after one read.
	 * @return the input.
	 */
	String getCharInput();
}
