package SQL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;

/**				_.-'CLASS'-._
 * 
 * Author(en): Kay Wellinger
 * Datum : 5.11.2012
 * 
 * Beschreibung:
 * 		Stellt eine verbindung zu einem PostgresSQL server her.
 * 		Stellt Funktionen bereit um dieses Login aus einer Datei zu Laden,
 * 		und in eine Datei zu Speichern
 * 
 **/

public class PSQLConnection {
	public interface Defines{//damit einfach zu strg+leertaste und schnell erweiter und änderbar
		public static final String CONNECTIONNAME 	= "CONNECTIONNAME";
		public static final String CONNECTIONDB 	= "DATABASE";
		public static final String CONNECTIONPORT 	= "PORT";
		public static final String CONNECTIONUSER 	= "USER";
		public static final String CONNECTIONPW		= "PASSWORD";
		
		public static final String PROJEKTSTRING = "Database file search";
	}
	
	private Connection connection = null;
	private boolean connected = false;
	
	private String serverAdress;
	private String database;
	private int port;
	private String username; 
	private String password;
	
	public PSQLConnection(){
	}
	
	public PSQLConnection(String serverAdress, int port, String database, String username, String password) {
		connect(serverAdress, port, database, username, password);
	}
	public PSQLConnection(String path){
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(new File(path)));
		} catch (IOException e) {
			System.out.println("Datei " + path + "konnte nicht geöffnet oder gefunden werden.");
			System.err.println(e);
		}
		
		connect(prop.getProperty(Defines.CONNECTIONNAME, ""),
				Integer.parseInt(prop.getProperty(Defines.CONNECTIONPORT, "0")),
				prop.getProperty(Defines.CONNECTIONDB, ""),
				prop.getProperty(Defines.CONNECTIONUSER, ""), 
				prop.getProperty(Defines.CONNECTIONPW, "")
		);
		
	}
	
	
	/**				_.-'METHOD'-._
	* 
	* Author(en): Kay Wellinger
	* Datum : 5.11.2012
	* 
	* Beschreibung:
	* 		verbindet.... mit servername port username und passwort... selbsterklärend eigendlich
	* 
	**/
	public boolean connect(String serverAdress, int port, String database, String username, String password){
		//alles speichern
		this.serverAdress = serverAdress;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
		
		if(connected){
			disconnect();
		}
		
		try{
			//treiber laden
			
			Class.forName("org.postgresql.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:postgresql://" + serverAdress +":" + port + "/"+ database, username, password);
			//TODO datenbank selbsstädnig anlegen
			if(connection == null)
				return false;
			connected = true; //connection anzeigen
		}catch(Exception ex) {
			System.out.println("Verbindung zur Datenbank konnte nicht aufgebaut werden");
			System.out.println(ex);
			return false;
		}
		return true;
	}
	
	public void disconnect(){
		if(connected)
			try {
				connection.close(); //session schliesen
			} catch (SQLException e) {}
		
		connected = false;
	}
	
	/**				_.-'METHOD'-._
	* 
	* Author(en): Kay Wellinger
	* Datum : 5.11.2012
	* 
	* Beschreibung:
	* 		ist name == ""
	* 			speichert die verbindung unter verbindungsadresse.sqlini
	*		wenn != ""
	* 			wird name.sqlini als dateiname gewählt
	* 
	**/
	public boolean saveToFile(String filePath){
		Properties prop = new Properties();
		Calendar calendar = Calendar.getInstance(); //kallender erstellen für zeiten
		
		if(connected){
			//alles eintragen
			prop.setProperty(Defines.CONNECTIONNAME, serverAdress);
			prop.setProperty(Defines.CONNECTIONDB, database);
			prop.setProperty(Defines.CONNECTIONPORT, new Integer(port).toString());
			prop.setProperty(Defines.CONNECTIONUSER, username);
			prop.setProperty(Defines.CONNECTIONPW, password);
			
			if(filePath.equals("")){
				filePath = "./" + serverAdress + ".sqlini";
			}
			
			try { //datei speichern
				prop.store(new BufferedWriter(new FileWriter(new File(filePath))), 
																	"Projekt:" + Defines.PROJEKTSTRING + 
																	"\nErstellungsdatum: <" + 
																	calendar.get(Calendar.HOUR_OF_DAY)+":"+
																	calendar.get(Calendar.MINUTE)+":"+
																	calendar.get(Calendar.SECOND) + "> " + 
																	calendar.get(Calendar.DAY_OF_MONTH)+"."+
																	calendar.get(Calendar.MONTH) + "." + 
																	calendar.get(Calendar.YEAR)
				);
				
				
				
			} catch (IOException e) {
				System.err.println("Konnte nicht gepeichtert werden");
			}
		}else{
			return false;
		}
		return true;
	}
	public boolean isConnected(){
		return connected;
	}
	public Connection getConnection(){
		return connection;
	}
	
	/**				_.-'METHOD'-._
	* 
	* Author(en): Kay Wellinger
	* Datum : 6.11.2012
	* 
	* Beschreibung:
	* 		ein statement muss übergeben werden welches zuvor mit getConnection().createstatement()
	* 		geholt wurde.
	* 		auserdem muss ein für das projekt relevanter tabellenname eingegeben werden über den geprüft wird 
	**/
	public boolean checkIfDatabaseInstalled(Statement statement, String someTablename){
		try {
			//exception sollte hier geworfen werden... unschön aber geht wohl leider nicht anders
			if(statement.executeQuery("SELECT * FROM " + someTablename +" LIMIT 1;") != null){
				System.out.println("db vorhanden");
				return true; //wenn er hier hinkommt ist die datenbank vorhanden
			}
			System.err.println("Something went wrong");
			return false; //sollte hier nicht hinkommen aber wenn dann falsch
		} catch (SQLException e) {
			return false; 	//Wenn er hier hinkommt ist die db nicht vorhanden
		}
	}
	
	/**				_.-'METHOD'-._
	* 
	* Author(en): Kay Wellinger
	* Datum : 5.11.2012
	* 
	* Beschreibung:
	*	es muss ein statement übergeben werden welches zuvor mit .getconnection().createStatement() erstellt wurde
	*	ein gültiger dateipfad muss übergeben werden, die datei wird nur zum lesen geöffnet
	* 
	**/
	public boolean copyFileToDatabase(Statement statement, String filepath){
		int lines = 0;
		try {
			BufferedReader fi = new BufferedReader(new FileReader(new File(filepath)));
			
			String lineContent;
			System.out.println(fi.ready());
			String text = "";
			while(fi.ready()){ //solange zeilen vorhanden sind
				lines++;
				//zeilen aus daeti in übernehmen
				lineContent = fi.readLine();
				lineContent = lineContent.replace("\r", " ");
				lineContent = lineContent.replace("\n", " ");
				lineContent = lineContent.replace("\t", " ");
				text += lineContent;
			}
			
			//in zusammen geschriebene zeilen ausführen
			if(statement.execute(text)){
				System.err.println("Fehler beim ausführen der Datei");
				fi.close();
				return false;
			}else{
				System.out.println(lines + " Zeilen wurden eingelesen und erfolgreich ausgeführt");
			}
			fi.close(); //datei wieder zumachen sonst haben wir irgendwann zu viele handles offen
			
		} catch (SQLException e) {
			System.err.println("Fehler beim verwerten des scripts:");
			e.printStackTrace();
			System.err.println("Fehler: "+e.getNextException());
			return false;
		} catch (FileNotFoundException e) {
			System.err.println("Datei konnte nicht gefunden werden");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.err.println("Fehler beim lesen der datei");
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
	public boolean createUser(Statement statement, String username, String userpassword){
		System.out.println(statement);
		if(username.isEmpty())
			return false;
		String querry = "CREATE USER \"" + username + "\" WITH CREATEDB PASSWORD '"+ userpassword  +"';"; 
		
		try {
			statement.execute(querry);
			System.out.println(querry);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean createDatabase(Statement statement, String dbname, String owner){
		String querry = "CREATE DATABASE ";
		if(dbname.equals(""))
			return false;
		
		querry += "\"" + dbname + "\" ";
		
		if(!owner.isEmpty()){
			querry += "WITH OWNER \"" + owner + "\"";
		}
		querry += ";";
		try {
			System.out.println(querry);
			statement.execute(querry);
			System.out.println(querry);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
