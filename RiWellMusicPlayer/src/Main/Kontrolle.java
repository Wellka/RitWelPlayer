package Main;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JOptionPane;


import musikData.MusikInformation;

import cal.Encriptions;

import Frames.DlgAccountCreation;
import Frames.DlgLogIn;
import SQL.PSQLConnection;


public class Kontrolle {

	private PSQLConnection psqlCon;
	private Connection connection;
	private Statement statement;	
	
	public Kontrolle(){
		boolean loggedIn = false;
		//Anmeldefenster anzeigen
		//MusikInformation mInformation = new MusikInformation("C:\\Musik\\We Came As Romans - What I Wished I Never Had - Understanding What We've Grown to Be.mp3");
		/*MusikPlayer mp3 = new MP3Player("C:\\Musik\\wait and bleed - slipknot.mp3");
		mp3.playADemo(10000); //TODO zeit aus optionen übernehmen
		while (mp3.isDone()){
			try {
				System.out.println();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		//datenbankverbindung herstellen
		if(!init()){
			JOptionPane.showMessageDialog(null, "Es konnte keine Verbindung zur Datenbank hergestellt werden", "Fehler", 0);
			return;
		}
		
		
		DlgLogIn logInDLG = new DlgLogIn();
		//loggin prüfen
		while(!loggedIn){
			switch(logInDLG.getResult()){
			case -1: System.exit(0); //programm abbruch
			case  1:
				//TODO db abfrage
				String MD5DBpw;
				try {
					ResultSet res = statement.executeQuery("SELECT passwort FROM Benutzer WHERE Benutzer.benutzer = '" + logInDLG.getUsername() + "';");
					res.next();
					MD5DBpw = res.getString(0);
					System.out.println();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if("test".equals(logInDLG.getPassword())){
					if(Encriptions.StringToMD5("test").equals(Encriptions.StringToMD5(logInDLG.getPassword()))){
						loggedIn = true;
					}
				}
				if(!loggedIn){
					logInDLG.setStatus("Falsches Passwort", Color.red);
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
		//TODO programmstart
 		
	}
	
	public boolean init(){
		//an DB anmelden
		if(!new File("./connection.sqllog").exists()){//prüfen ob login datei vorhanden
			//TODO daten angeben fenster, dieses mit fertigen daten füllen
			psqlCon = new PSQLConnection("localhost", 5432, "MusikPlayer", "postgres", "postgres");
			psqlCon.saveToFile("./connection.sqllog");
		}
		else
		{	//wenn ja dann laden
			psqlCon = new PSQLConnection("./connection.sqllog"); 
		}
		
		//DB prüfen
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
			
			//prüfen ob datenbank vorhanden
		}else{
			return false; //fehler
		}
		return true;
	}
	
	public boolean createNewUser(){
		DlgAccountCreation dlg = new DlgAccountCreation(statement);
		return dlg.getResult();
	}
	
	public boolean checkIfPasswordIsCorrect(){
		
		return false;
	}
}
