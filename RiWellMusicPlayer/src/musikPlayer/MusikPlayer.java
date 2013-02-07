package musikPlayer;

public abstract class MusikPlayer{

	String filename;
	boolean bStopped = false;
	MusikPlayer mp;
	
	public MusikPlayer(String filename) {
		this.filename = filename;
		mp = this;
	}
	
	public void playADemo(final int ms){
		
		new Thread(){
			public void run(){
				mp.play();
				int counter = 0;
				if(bStopped)
					bStopped = false;
				while (!bStopped && (counter*10) < ms){
					counter++;
					System.out.println(counter*10 + "/" + ms);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}
				}
				mp.stop();
			}
		}.start();
	}

	/**
	 * Creates a new Player
	 */
	public abstract void play();
	
	/**
	 *  stoppes the player
	 */
	public void stop(){
		bStopped = true;
		close();
	}
	/**
	 * Closes the Player
	 */
	public abstract void close();
	
	public abstract boolean isDone();

}
