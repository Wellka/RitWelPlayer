package Main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.text.html.HTML.Tag;

import musikData.MusikInformation;
import musikPlayer.MP3Player;
import musikPlayer.MusikPlayer;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import Frames.DlgLogIn;
import SQL.PSQLConnection;


public class Kontrolle {

	private PSQLConnection psqlCon;
	private Connection connection;
	private Statement statement;
	
	//private Properties properties = new Properties();	
	public Kontrolle(){
		//Anmeldefenster anzeigen
		new MusikInformation("C:\\Musik\\We Came As Romans - What I Wished I Never Had - Understanding What We've Grown to Be.mp3");
		DlgLogIn logInDLG = new DlgLogIn(null, true);
		
		MusikPlayer mp3 = new MP3Player("C:\\Musik\\wait and bleed - slipknot.mp3");
		mp3.playADemo(10000); //TODO zeit aus optionen übernehmen
		while (mp3.isDone()){
			try {
				System.out.println();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
		
		if(init()){
			//TODO programmstart
		}else{
			//TODO dialog: datenbankverbindung konnte nicht hergestellt werden -> retry/programmende
			System.out.println("BUUUUUUUUUUUUUUUUUUUUUUUG");
			return;
		}
		
		/*try { //TODO für login und den anderen crap
			File f = new File(".properties");
			if(!f.exists()) 
				f.createNewFile();
			else	
				properties.load(new FileInputStream(f));
		} catch (IOException e1) {}*/
	}
	
	public boolean init(){
		//new File("./connection.sqllog").delete();
		
		if(! new File("./connection.sqllog").exists()){//prüfen ob login datei vorhanden
			//TODO daten angeben fenster, dieses mit fertigen daten füllen
			psqlCon = new PSQLConnection("localhost", 5432, "finder", "postgres", "postgres");
			psqlCon.saveToFile("./connection.sqllog");
		}
		else
		{	//wenn ja dann laden
			psqlCon = new PSQLConnection("./connection.sqllog"); 
			//TODO prüfen ob connected, wenn nicht dann dialog
		}
		
		if(psqlCon.isConnected()){
			connection = psqlCon.getConnection();
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				System.out.println("Statement konnte nicht erstellt werden");
				e.printStackTrace();
				return false;
			}
			
			//prüfen ob datenbank vorhanden
			if(!psqlCon.checkIfDatabaseInstalled(statement, "filesearch")){
				System.out.println("Erstellen"); //TODO debug
				System.out.println(ClassLoader.getSystemResource("./scripts/setupscript.sql").getPath());
				psqlCon.copyFileToDatabase(statement, ClassLoader.getSystemResource("./scripts/setupscript.sql").getPath());
			}
		}else{
			return false;
		}
		return true;
	}

}
