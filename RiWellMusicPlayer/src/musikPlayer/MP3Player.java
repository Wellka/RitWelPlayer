package musikPlayer;

import javazoom.jl.player.Player;

import java.io.*;

public class MP3Player extends MusikPlayer{

	private Player player;

	/**
	 * MP3 constructor
	 * 
	 * @param filename
	 *            name of input file
	 */
	public MP3Player(String filename) {
		super(filename);
	}

	/**
	 * Creates a new Player
	 */
	public void play() {
		try {
			FileInputStream fis = new FileInputStream(this.filename);
			BufferedInputStream bis = new BufferedInputStream(fis);

			this.player = new Player(bis);
		} catch (Exception e) {
			System.err.printf("%s\n", e.getMessage());
		}

		new Thread() {
			@Override
			public void run() {
				try {
					player.play();
				} catch (Exception e) {
					System.err.printf("%s\n", e.getMessage());
				}
			}
		}.start();
	}

	/**
	 * Closes the Player
	 */
	public void close() {
		if (this.player != null) {
			this.player.close();
		}
	}
	
	public boolean isDone(){
		return !bStopped;
	}


	// ///////////////////////

	/**
	 * Plays '01 Maenam.mp3' in an infinite loop
	 */
	/*
	 * public static void playMaenam() { MP3 mp3 = new MP3("./01 Maenam.mp3");
	 * 
	 * mp3.play();
	 * 
	 * while (true) { if (mp3.player.isComplete()) { mp3.close(); mp3.play(); }
	 * } }
	 */

}
