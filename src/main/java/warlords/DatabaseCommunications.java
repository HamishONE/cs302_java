package warlords;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the syncing of the high scores with the web server.
 */
public class DatabaseCommunications {

	private static final String putUrl = "http://romana.co.nz/CS302/insert.php";
	private static final String getUrl = "http://romana.co.nz/CS302/getScores.php";

	private ArrayList<Score> scores = new ArrayList<>();

	/**
	 * Create a new instance with high scores loaded from the server.
	 */
	public DatabaseCommunications() {
		loadValues();
	}

	/**
	 * Add a new high score to central database.
	 * @param name The name of the player who has won.
	 * @param score The score the player achieved.
	 */
	public void putValues(String name, int score) {
		try {
			URLConnection connection = new URL(putUrl + "?name=" + getURLSafeString(name) + "&score=" + score).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
			InputStream response = connection.getInputStream();

			Scanner scanner = new Scanner(response);
			String responseBody = scanner.next();
			if(responseBody.equals("Success")) {
				loadValues();
			}
			else {
				throw new IOException("The return status from the server was not of success.");
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Replace special characters with their equivalents for use in http addresses.
	 * @param input The string to be parsed.
	 * @return The corrected string.
	 */
	private String getURLSafeString(String input) {
		input = input.replace(" ", "%20");
		input = input.replace("$", "%24");
		input = input.replace("&", "%26");
		input = input.replace("`", "%60");
		input = input.replace(":", "%3A");
		input = input.replace("<", "%3C");
		input = input.replace(">", "%3E");
		input = input.replace("[", "%5B");
		input = input.replace("]", "%5D");
		input = input.replace("{", "%7B");
		input = input.replace("}", "%7D");
		input = input.replace("\"", "%22");
		input = input.replace("+", "%2B");
		input = input.replace("#", "%23");
		input = input.replace("%", "%25");
		input = input.replace("@", "%40");
		input = input.replace("/", "%2F");
		input = input.replace(";", "%3B");
		input = input.replace("=", "%3D");
		input = input.replace("?", "%3F");
		input = input.replace("\\", "%5C");
		input = input.replace("^", "%5E");
		input = input.replace("|", "%7C");
		input = input.replace("~", "%7E");
		input = input.replace("'", "%27");
		input = input.replace(",", "%2C");

		return input;
	}

	/**
	 * Refresh the list of high scores from the server.
	 */
	public void loadValues() {
		try {
			URLConnection connection = new URL(getUrl).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
			InputStream response = connection.getInputStream();

			Scanner scanner = new Scanner(response).useDelimiter("\\A"); // \A means the entire input will be read
			if (scanner.hasNext()) {
				String responseBody = scanner.next();
				List<String> tempList = Arrays.asList(responseBody.split("\\|"));
				ArrayList<String> scoreString = new ArrayList<>(tempList);

				if (scoreString.size() > 1) {
					ArrayList<Score> scores = new ArrayList<>();
					for (int i = 0; i < scoreString.size(); i += 2) {
						scores.add(new Score(scoreString.get(i), Integer.parseInt(scoreString.get(i + 1))));
					}
					this.scores = scores;
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Get the list of high scores from the local cache.
	 * @return the list of high scores
	 */
	public ArrayList<Score> getValues() {
		return scores;
	}
}
