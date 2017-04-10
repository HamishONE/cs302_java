package warlords;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles playing sounds, also caches sound files to avoid too much file reading
 */
public class SoundView {

	private HashMap<String, AudioClip> soundCache = new HashMap<>();

	/**
	 * Helper function to get an image, this gets the image path from a cached Image instance, or if it does not already exist, creates it.
	 *
	 * @param path		Path to sound location
	 * @return			instance of Media with the requested sound within
	 */
	private AudioClip getSound(String path) {
		//return soundCache.computeIfAbsent(path, p -> new Media(new File(path).toURI().toString()));
		return soundCache.computeIfAbsent(path, p -> new AudioClip(getClass().getResource(path).toExternalForm()));
	}

	//AudioClip audioClip = new AudioClip(getClass().getResource("/Sad-cat.mp3").toExternalForm());

	/**
	 * Plays the sound pointed to by the path passed in
	 *
	 * @param path path to the location of the sound to be played
	 */
	public void playSound(String path) {
			//MediaPlayer mediaPlayer = new MediaPlayer(getSound(path));
			//mediaPlayer.play();
		AudioClip audioClip = getSound(path);
		audioClip.play();
	}

	/**
	 * When called loads all the provided media paths to Media objects to reduce delay at first playback
	 *
	 * @param paths ArrayList of strings to the path of the sounds
	 */
	public void loadSounds(ArrayList<String> paths) {
		//Preload all pro
		for(String path : paths) {
			soundCache.putIfAbsent(path, new AudioClip(getClass().getResource(path).toExternalForm()));
		}
	}
}
