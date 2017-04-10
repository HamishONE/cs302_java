package warlords;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DatabaseCommunications {

	private String putUrl = "http://romana.co.nz/CS302/insert.php";
	private String getUrl = "http://romana.co.nz/CS302/getScores.php";

	public boolean putValues(String name, int score) {
		try {
			URLConnection connection = new URL(putUrl + "?name=" + name + "&score=" + score).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
			InputStream response = connection.getInputStream();

			try (Scanner scanner = new Scanner(response)) {
				String responseBody = scanner.next();
				if(responseBody == "New record created successfully") {
					return true;
				}
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	
	public ArrayList<Score> getValues() {
		try {
			URLConnection connection = new URL(getUrl).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());
			InputStream response = connection.getInputStream();

			try (Scanner scanner = new Scanner(response)) {
				if (scanner.hasNext() == false) {
					return new ArrayList<Score>();
				}
				String responseBody = scanner.next();
				List<String> tempList = Arrays.asList(responseBody.split("\\|"));
				ArrayList<String> scoreString = new ArrayList<>(tempList);
				if (scoreString.size() > 1) {
					ArrayList<Score> scores = new ArrayList<>();
					for (int i = 0; i < scoreString.size(); i += 2) {
						scores.add(new Score(scoreString.get(i), Integer.parseInt(scoreString.get(i + 1))));
					}
					return scores;
				}
			}
		} catch (Exception ex) {
			//DO NOTHING useful
			System.out.println(ex);
		}
		return new ArrayList<Score>();
	}

}
