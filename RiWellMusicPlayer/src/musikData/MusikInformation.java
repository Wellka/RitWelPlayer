package musikData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.ImageIcon;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

import SQL.PSQLConnection;

public class MusikInformation {

	public static final int INVALID_ID = -1;
	private String interpet = "";
	private String album = "";
	private String titel = "";
	private String pfad = "";
	private String genre = "";
	private int id = INVALID_ID;	

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
				//albumart speichern
				albumArt = t.getArtworkList().get(0).getImage();
				albumArt.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public MusikInformation(String interpet, String album, String titel, String pfad, BufferedImage albumArt, int id) {
		super();
		
		this.interpet = interpet;
		this.album = album;
		this.titel = titel;
		this.pfad = pfad;
		this.albumArt = albumArt;
		this.id = id;
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
	
	public int getID(){
		return this.id;
	}
	
	public boolean setID(int id){
		if(this.id  == INVALID_ID){
			this.id = id;
			return true;
		}
		return false;
	}
	
	/**
	 * @author Kay Wellinger
	 * @param sql sql verbindung
	 * @return sql querry
	 */
	public String uploadToDatabase(PSQLConnection sql, int userID) throws SQLException{
		Connection con = sql.getConnection();
		//Statement statement = null;
		PreparedStatement prepState = null;
		
		ResultSet res;
		
		int idGenre = 0; 
		int idInterpret = 0;
		int idAlbum = 0;
		
		/*try {
			statement = con.createStatement();
		} catch (SQLException e) {
			return null;
		}*/
		//ab hier ist das meiste copy and paste, leider so gelöst weil sql eigenartig ist
		//Genre abfragen und ggf erstellen
		String query = "SELECT id FROM genre WHERE genre = ?;";
		prepState = con.prepareStatement(query);
		prepState.setString(1, this.genre);
		res = prepState.executeQuery();
		//res = statement.executeQuery(query);
		if(res.next()){
			idGenre = res.getInt(1);
		}else{
			String queryinner = "INSERT INTO genre (genre) VALUES (?);";
			PreparedStatement innerState = con.prepareStatement(queryinner);
			innerState.setString(1, this.genre);
			innerState.execute();
			
			res = prepState.executeQuery();
			if(res.next())
				idGenre = res.getInt(1);
		}
		
		//interpret
		query = "SELECT id FROM interpret WHERE interpret = ?;";
		prepState = con.prepareStatement(query);
		prepState.setString(1, this.interpet);
		res = prepState.executeQuery();
		if(res.next()){
			idInterpret = res.getInt(1);
		}else{
			String queryinner = "INSERT INTO interpret (interpret) VALUES (?);";
			PreparedStatement innerState = con.prepareStatement(queryinner);
			innerState.setString(1, this.interpet);
			innerState.execute();
			
			res = prepState.executeQuery();
			if(res.next())
				idInterpret = res.getInt(1);
		}
		
		//album
		query = "SELECT id FROM album WHERE albumname = ?;";
		//res = statement.executeQuery(query);
		prepState = con.prepareStatement(query);
		prepState.setString(1, this.album);
		res = prepState.executeQuery();
		
		if(res.next()){
			idAlbum = res.getInt(1);
		}else{
			String queryinner = "INSERT INTO album (albumname) VALUES (?);";
			PreparedStatement innerState = con.prepareStatement(queryinner);
			innerState.setString(1, this.album);
			innerState.execute();
			
			res = prepState.executeQuery();
			if(res.next())
				idAlbum = res.getInt(1);
		}
		
		query = "INSERT INTO musikdaten (titel, benutzer_id, timestamp) VALUES (?, '" + userID + "',?) RETURNING id;";
				
		//res = statement.executeQuery(query);
		//res = statement.getGeneratedKeys();
		prepState = con.prepareStatement(query);
		prepState.setString(1, this.titel);
		
		//Date(int year, int month, int day)
		prepState.setDate(2, new Date(Calendar.getInstance().get(Calendar.DATE)));
		res = prepState.executeQuery();
		
		if(res.next()){
			System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
			id = res.getInt(1);
			
			query = "INSERT INTO musikdaten_album (id_musik, id_album) VALUES (?, ?);";
			prepState = con.prepareStatement(query);
			prepState.setInt(1, id);
			prepState.setInt(2, idAlbum);
			prepState.execute();
			query = "INSERT INTO musikdaten_genre (id_musik, id_genre) VALUES (?, ?);";
			prepState = con.prepareStatement(query);
			prepState.setInt(1, id);
			prepState.setInt(2, idGenre);
			prepState.execute();
			query = "INSERT INTO musikdaten_interpret (id_musik, id_interpret) VALUES (?, ?);";
			prepState = con.prepareStatement(query);
			prepState.setInt(1, id);
			prepState.setInt(2, idInterpret);
			prepState.execute();
		}  
		
		return query;
	}
}
