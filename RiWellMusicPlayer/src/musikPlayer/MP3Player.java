package musikPlayer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

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

		bStopped = false;
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
		if(player == null)
			return true;
		if(player.isComplete())
			bStopped = true;
		return bStopped;
	}
}
