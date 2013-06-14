package Main;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import musikData.MusikInformation;
import musikPlayer.MP3Player;
import musikPlayer.MusikPlayer;
import Frames.FrmMain;
import Frames.Dialogs.DlgAccountCreation;
import Frames.Dialogs.DlgConnectionOption;
import Frames.Dialogs.DlgLogIn;
import SQL.PSQLConnection;
import cal.Encriptions;

/**
 * @author Kay Wellinger
 */
public class Kontrolle {

	private PSQLConnection psqlCon;
	private Connection connection;
	private Statement statement;	 
	private int activeUserID;
	private ArrayList<Integer> myFriends = null;
	
	public Kontrolle(){
		boolean loggedIn = false;
		
		setUIFont (new javax.swing.plaf.FontUIResource(new Font("Arial",Font.PLAIN, 12)));
		
		//datenbankverbindung herstellen
		if(!init()){
			JOptionPane.showMessageDialog(null, "Es konnte keine Verbindung zur Datenbank hergestellt werden", "Fehler", 0);
			return;
		}
		
		//anmeldefenster anzeigen
		DlgLogIn logInDLG = new DlgLogIn();
		//loggin prüfen
		while(!loggedIn){
			switch(logInDLG.getResult()){
			case -1: System.exit(0); //programm abbruch
			case  1:
				//TODO db abfrage
				String MD5DBpw = getPasswordByUsername(logInDLG.getUsername());
				if(!MD5DBpw.equals("")){
					if(MD5DBpw.equals(Encriptions.StringToMD5(logInDLG.getPassword()))){
						loggedIn = true;
						activeUserID = getIDByUsername(logInDLG.getUsername());
					}
				}	
				if(!loggedIn){
					logInDLG.setStatus("Falsches Passwort oder Benutzername", Color.red);
				}
			break;
			case  2: 
				if(createNewUser()){
					logInDLG.setStatus("Benutzer erstellt", Color.green);
				}
			break;
			}
			if(!loggedIn){
				logInDLG.setVisible(true);
			}
		}
		new FrmMain(this);
	}
	
	private String getPasswordByUsername(String username){
		try {
			ResultSet res = statement.executeQuery("SELECT passwort FROM Benutzer WHERE Benutzer.benutzer = '" + username + "';");
			res.next();
			return res.getString(1);
		} catch (SQLException e) {
			return "";
		}
	}
	
	private int getIDByUsername(String username){
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT id FROM Benutzer WHERE Benutzer.benutzer = ?;");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			return -1;
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public String getUserNameById(int id){
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT benutzer FROM Benutzer WHERE id = ?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString(1);
			return "";
		} catch (SQLException e) {
			return "";
		}
	}
	
	public boolean init(){
		//an DB anmelden
		psqlCon =  connectToDB();
		
		if(!psqlCon.isConnected()){
			setupDatabase();
		}
		
		//DB prüfen
		if(psqlCon.isConnected()){
			connection = psqlCon.getConnection();
			try {
				statement = connection.createStatement();
				if(!psqlCon.checkIfDatabaseInstalled(statement, "Benutzer")){
					if(!psqlCon.copyFileToDatabase(statement, new File("./setupscript.sql").getAbsolutePath())){
						System.err.println("Lasst alle hoffnung fahren");
						return false;
					}
				}
			} catch (SQLException e) {
				System.out.println("Statement konnte nicht erstellt werden");
				e.printStackTrace();
				return false; //fehler
			}
			
			//prüfen ob datenbank vorhanden
		}else{
			return false; //fehler
		}
		return true;
	}
	
	private PSQLConnection connectToDB(){
		PSQLConnection con = null;
		if(!new File("./connection.login").exists()){//prüfen ob login datei vorhanden
			//TODO daten angeben fenster, dieses mit fertigen daten füllen
			con = new PSQLConnection("localhost", 5432, "dbprojekt", "projekt", "geheim");
			con.saveToFile("./connection.login");
		}
		else
		{	//wenn ja dann laden
			con =  new PSQLConnection("./connection.login"); 
		}
		return con;
	}
	
	public boolean createNewUser(){
		DlgAccountCreation dlg = new DlgAccountCreation(statement);
		return dlg.getResult();
	}
	
	public boolean checkIfPasswordIsCorrect(){
		
		return false;
	}
	
	private boolean setupDatabase(){
		DlgConnectionOption dlg = new DlgConnectionOption();
		if(dlg.getResult() == 1){
			psqlCon = new PSQLConnection(dlg.getServerName(), dlg.getServerPort(), dlg.getDatabase(), dlg.getLogin(), dlg.getPassword());
			if(psqlCon.isConnected()){
				try {
					statement = psqlCon.getConnection().createStatement();
				} catch (SQLException e) {
				}
				if(JOptionPane.showConfirmDialog(null, "Im nächsten Schritt werden Benutzer und Datenbanken für das programm angelegt, Sind sie sich sicher?", "Bestätigung", 1) == JOptionPane.YES_OPTION){
					if(!psqlCon.createUser(statement, "RWMP", "RWMP")){
						JOptionPane.showMessageDialog(null, "Datenbank-Benutzer konnte nicht erstellt werden. \n" +
								"gegebenenfalls fehlen die notwendigen Rechte", "Fehler", 0);
						return false;
					}
					if(!psqlCon.createDatabase(statement, "MusikPlayerDB", "RWMP")){
						JOptionPane.showMessageDialog(null, "Datenbank-Benutzer konnte nicht erstellt werden. \n" +
								"gegebenenfalls fehlen die notwendigen Rechte", "Fehler", 0);
						return false;
					}
					
				}else{
					return false;
				}
				psqlCon.disconnect();
			}else{
				JOptionPane.showMessageDialog(null, "Verbindung konnte nicht hergestellt werden. Datenbank daten überprüfen", "Fehler", 0);
				return false;
			}
		}else{
			return false;
		}
		psqlCon = new PSQLConnection(dlg.getServerName(),dlg.getServerPort(), "MusikPlayerDB" ,dlg.getLogin(), dlg.getPassword());
		if(psqlCon.isConnected()){
			psqlCon.saveToFile("./connection.login");
			return true;
		}
		return false;
	}
	
	MusikPlayer mp3Player;
	public void playMusik(String file){
		if(mp3Player != null){
			if(!mp3Player.isDone())
				mp3Player.stop();
		}
		
		mp3Player = new MP3Player(file);
		mp3Player.play(); //TODO zeit aus optionen übernehmen
	}
	
	public void stopMusik(){
		if(mp3Player != null){
			if(!mp3Player.isDone()){
				mp3Player.stop();
			}
		}
	}
	
	public PSQLConnection getSqlConnection(){
		return psqlCon;
	}
	
	public int getActiveUserID(){
		return activeUserID;
	}
	public static final int ALL_USERS = -1;
	public ArrayList<MusikInformation> getAllMusikFromDB(int onlyFromUser, String search) throws SQLException{
		ArrayList<MusikInformation> musikinformations =  new ArrayList<MusikInformation>();
		/*"SELECT md.id,md.benutzer_id,md.titel,g.genre,a.albumname,i.interpret FROM musikdaten md "+
		"INNER JOIN musikdaten_album ma ON md.id = ma.id_musik "+
		"INNER JOIN musikdaten_genre mg ON md.id = mg.id_musik "+
		"INNER JOIN musikdaten_interpret mi ON md.id = mi.id_musik "+
		"INNER JOIN genre g ON g.id = mg.id_genre "+
		"INNER JOIN interpret i ON mi.id_interpret = i.id "+
		"INNER JOIN album a ON ma.id_album = a.id;"; */
		//SELECT * FROM alletitel WHERE interpret LIKE '%Jean%' OR titel LIKE '%Jean%' OR genre LIKE '%Jean%' OR albumname LIKE '%jean%';
		if(search != null)
			if(search.equals(""))
				search = null;
		
		String query = "SELECT * FROM alletitel";
		
		if(search != null){
			query += " WHERE interpret LIKE ? OR titel LIKE ? OR genre LIKE ? OR albumname LIKE ?";
		}
		query += ";";
		PreparedStatement ps = connection.prepareStatement(query);
		if(search != null){
			search = "%" + search + "%";
			System.out.println(query);
			for (int i = 1; i < 5; i++) {
				ps.setString(i, search);
			}
			System.out.println(search + " || " + ps.toString());
		}
		
		ResultSet result = ps.executeQuery();//statement.executeQuery(query);
	
		while(result.next()){
			int idUser = result.getInt(2); //id benutzer
			if( idUser == onlyFromUser || onlyFromUser == ALL_USERS)
				musikinformations.add(new MusikInformation(result.getString(6), result.getString(5), result.getString(3), "",result.getString(4), result.getInt(2), null, result.getInt(1)));
		}
		return musikinformations;
	}

	private static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	        {
	            UIManager.put(key, f);
	        }
	    }
	}
	
	public boolean updateUserFriends(){
		ArrayList<Integer> returns = null;
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT id_benutzer2 FROM benutzer_benutzer WHERE id_benutzer1 = ?");
			ps.setInt(1, activeUserID);
			ResultSet rs = ps.executeQuery();
			returns =  new ArrayList<Integer>();
			
			while(rs.next()){
				returns.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		myFriends = returns;
		if(returns == null)
			return false;
		return true;
	}
	
	public ArrayList<Integer> getUserFriends(){
		if(myFriends == null){
			updateUserFriends();
		}
		return myFriends;
	}
}
