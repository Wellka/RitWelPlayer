package musikData;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

public class MusikInformation {

	private String interpet = "";
	private String album = "";
	private String titel = "";
	private String pfad = "";
	private String genre = "";

	private BufferedImage albumArt;
	
	public MusikInformation(String path){
		AudioFile af = null;
		try {
			af = new AudioFileIO().readFile(new File(path));
		} catch (CannotReadException e) {
			// TODO Berechtingungen fehler dialog
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			// TODO tag konnte nicht glesen werden dialog
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Es wurde keine gültige datei angegeben dialog
			e.printStackTrace();
		}
		
		Tag t = af.getTag();
		boolean first = true;
		interpet = "";
		for (TagField tagf : t.getFields(FieldKey.ARTIST)) {
			if(!first){
				interpet += ", ";
			}
			first = false;
			interpet += t.getFirst(tagf.getId());
		}
		
		genre = "";
		first = true;
		for (TagField tagf : t.getFields(FieldKey.GENRE)) {
			if(!first){
				genre += ", ";
			}
			first = false;
			genre += t.getFirst(tagf.getId());
		}	
		System.out.println("gerne: "+ genre);
		titel = t.getFirst(FieldKey.TITLE);
		this.pfad = path;
		if(t.getArtworkList().size() > 0){
			try {
				albumArt = t.getArtworkList().get(0).getImage();
				albumArt.flush();
				new JFrame(){
					private static final long serialVersionUID = 1L;
					
					public void init() {
						JLabel pnl = new JLabel();
						getContentPane().add(pnl, BorderLayout.CENTER);
						System.out.println(albumArt);
						pnl.setIcon(new ImageIcon(albumArt));//(albumArt, 0, 0, this);
						pack();
						setVisible(true);
					}
				}.init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public MusikInformation(String interpet, String album, String titel, String pfad, BufferedImage albumArt) {
		super();
		
		this.interpet = interpet;
		this.album = album;
		this.titel = titel;
		this.pfad = pfad;
		this.albumArt = albumArt;
	}

	public String getPfad() {
		return pfad;
	}

	public void setPfad(String pfad) {
		this.pfad = pfad;
	}

	public String getInterpet() {
		return interpet;
	}

	public void setInterpet(String interpet) {
		this.interpet = interpet;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public BufferedImage getAlbumArt() {
		return albumArt;
	}

	public void setAlbumArt(ImageIcon albumArt) {
		//TODO set albumart data
	}
	

}
