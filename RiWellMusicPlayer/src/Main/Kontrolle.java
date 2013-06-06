package Main;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import java.sql.Statement;

import javax.swing.JOptionPane;

import musikData.MusikInformation;
import musikPlayer.MP3Player;
import musikPlayer.MusikPlayer;

import cal.Encriptions;

import Frames.FrmMain;
import Frames.Dialogs.DlgAccountCreation;
import Frames.Dialogs.DlgConnectionOption;
import Frames.Dialogs.DlgLogIn;
import SQL.PSQLConnection;


public class Kontrolle {

	private PSQLConnection psqlCon;
	private Connection connection;
	private Statement statement;	 
	
	public Kontrolle(){
		boolean loggedIn = false;
		
		//datenbankverbindung herstellen
		if(!init()){
			JOptionPane.showMessageDialog(null, "Es konnte keine Verbindung zur Datenbank hergestellt werden", "Fehler", 0);
			return;
		}
		
		//anmeldefenster anzeigen
		DlgLogIn logInDLG = new DlgLogIn();
		//loggin pr�fen
		while(!loggedIn){
			switch(logInDLG.getResult()){
			case -1: System.exit(0); //programm abbruch
			case  1:
				//TODO db abfrage
				String MD5DBpw = getPasswordByUsername(logInDLG.getUsername());
				if(!MD5DBpw.equals("")){
					if(MD5DBpw.equals(Encriptions.StringToMD5(logInDLG.getPassword()))){
						loggedIn = true;
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
		FrmMain frmMain = new FrmMain(this);
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
	
	public boolean init(){
		//an DB anmelden
		psqlCon =  connectToDB();
		
		if(!psqlCon.isConnected()){
			setupDatabase();
		}
		
		//DB pr�fen
		if(psqlCon.isConnected()){
			connection = psqlCon.getConnection();
			try {
				statement = connection.createStatement();
				if(!psqlCon.checkIfDatabaseInstalled(statement, "Benutzer")){
					if(!psqlCon.copyFileToDatabase(statement, ClassLoader.getSystemResource("./scripts/setupscript.sql").getPath())){
						System.err.println("Lasst alle hoffnung fahren");
						return false;
					}
				}
			} catch (SQLException e) {
				System.out.println("Statement konnte nicht erstellt werden");
				e.printStackTrace();
				return false; //fehler
			}
			
			//pr�fen ob datenbank vorhanden
		}else{
			return false; //fehler
		}
		return true;
	}
	
	private PSQLConnection connectToDB(){
		PSQLConnection con = null;
		if(!new File("./connection.login").exists()){//pr�fen ob login datei vorhanden
			//TODO daten angeben fenster, dieses mit fertigen daten f�llen
			con = new PSQLConnection("localhost", 5432, "MusikPlayerDB", "RWMP", "RWMP");
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
			psqlCon = new PSQLConnection(dlg.getServerName(), dlg.getServerPort(), dlg.getLogin(), dlg.getLogin(), dlg.getPassword());
			if(psqlCon.isConnected()){
				try {
					statement = psqlCon.getConnection().createStatement();
				} catch (SQLException e) {
				}
				if(JOptionPane.showConfirmDialog(null, "Im n�chsten Schritt werden Benutzer und Datenbanken f�r das programm angelegt, Sind sie sich sicher?", "Best�tigung", 1) == JOptionPane.YES_OPTION){
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
				JOptionPane.showMessageDialog(null, "Verbindung konnte nicht hergestellt werden. Datenbank daten �berpr�fen", "Fehler", 0);
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
		mp3Player.play(); //TODO zeit aus optionen �bernehmen
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
}
