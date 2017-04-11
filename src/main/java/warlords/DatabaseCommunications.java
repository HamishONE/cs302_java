package warlords;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DatabaseCommunications {

	private String putUrl = "http://romana.co.nz/CS302/insert.php";
	private String getUrl = "http://romana.co.nz/CS302/getScores.php";
	private ArrayList<Score> scores = new ArrayList<>();

	public DatabaseCommunications() {
		loadValues();
	}

	public boolean putValues(String name, int score) {
		try {
			URLConnection connection = new URL(putUrl + "?name=" + name.replace(" ", "%20") + "&score=" + score).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
			InputStream response = connection.getInputStream();

			Scanner scanner = new Scanner(response);
			String responseBody = scanner.next();
			if(responseBody.equals("Success")) {
				loadValues();
				return true;
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	
	public void loadValues() {
		try {
			URLConnection connection = new URL(getUrl).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
			InputStream response = connection.getInputStream();

			Scanner scanner = new Scanner(response).useDelimiter("\\A");
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
			ex.printStackTrace();
		}
	}

	public ArrayList<Score> getValues() {
		return scores;
	}



}
