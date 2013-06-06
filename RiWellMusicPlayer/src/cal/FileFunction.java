package cal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import musikData.MusikInformation;

public final class FileFunction {

	public static boolean uploadFile(String table, String colName, Connection connection, String path, int iD){
		File file = new File(path);
		FileInputStream fis = null;
		PreparedStatement ps = null;
		
		//Datei öffnen
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("File could not be opened");
			e.printStackTrace();
			return false;
		}
		//datenbankverbindung öffnen
		try {
			ps = connection.prepareStatement("UPDATE "+table+" SET data=? WHERE id = " + iD);
			System.out.println(ps);
		} catch (SQLException e) {
			System.err.println("Database coldnt return Statement");
			e.printStackTrace();
			return false;
		}
		
		//hochladen.. Kann dauern
		try{
			ps.setBinaryStream(1, fis, (int)file.length());
			ps.execute();
			ps.close();
		}catch (SQLException e){
			System.err.println("Error while uploading file");
			e.printStackTrace();
			return false;
		}
		//datei schliesen
		try {
			fis.close();
		} catch (IOException e) {return false;}
		
		return true;
	}
	private final static String DEFAULT_PATH = "./Download/"; 
	public static boolean checkIfFileAlreadyDownloaded(MusikInformation mi){
		String path = DEFAULT_PATH + mi.getInterpet() +" - "+ mi.getAlbum() +" - " + mi.getTitel() +"mp3";
		if(new File(path).exists()){
			return true;
		}
		return false;
	}
	public static String downloadToFile(String table, String colName, Connection con, MusikInformation mi) throws SQLException{
		if(mi.getID() == MusikInformation.INVALID_ID)
			return "";
		String path = DEFAULT_PATH + mi.getInterpet() +" - "+ mi.getAlbum() +" - " + mi.getTitel() +"mp3";
		
		File f = new File(path);
		if(!f.exists()){
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
			}
			BufferedOutputStream bfos= new BufferedOutputStream(fos);
			PreparedStatement ps = con.prepareStatement("SELECT "+colName+" FROM "+table+" WHERE id = "+ mi.getID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
			    byte[] dataBytes = rs.getBytes(1);
			    try {
					bfos.write(dataBytes);
				} catch (IOException e) {
					System.err.println("Couldnt write data");
					e.printStackTrace();
				}
			}
			rs.close();
			ps.close();
		}else{
			System.out.println("Datei bereits vorhanden");
		}
		return path;			
	}
}
