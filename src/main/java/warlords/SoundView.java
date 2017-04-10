package warlords;

import javafx.scene.media.AudioClip;
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
	 * @return			instance of {@link AudioClip} with the requested sound within
	 */
	private AudioClip getSound(String path) {
		return soundCache.computeIfAbsent(path, p -> new AudioClip(getClass().getResource(path).toExternalForm()));
	}

	/**
	 * Plays the sound pointed to by the path passed in
	 *
	 * @param path path to the location of the sound to be played
	 */
	public void playSound(String path) {
		AudioClip audioClip = getSound(path);
		audioClip.play();
	}

	/**
	 * When called loads all the provided media paths to {@link AudioClip} objects to reduce delay at first playback.
	 *
	 * @param paths ArrayList of strings to the path of the sounds
	 */
	public void loadSounds(ArrayList<String> paths) {
		for(String path : paths) {
			soundCache.putIfAbsent(path, new AudioClip(getClass().getResource(path).toExternalForm()));
		}
	}
}
