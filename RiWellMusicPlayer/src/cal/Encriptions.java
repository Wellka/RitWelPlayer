package cal;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kay Wellinger
 */
public abstract class Encriptions {
	public static String StringToMD5(String string){
		//einheitlich zu utf8 (sonst anderes MD5 weil linux)
		try {
			string = new String(string.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			return "";
		}
		
		//Hashing vorbereiten
	    MessageDigest md5 = null;
	    try {
	        md5 = MessageDigest.getInstance("MD5");
	    }
	    catch (NoSuchAlgorithmException e) {
	       return "";
	    }
	 
	    //hashing
	    md5.reset();
	    md5.update(string.getBytes());
	    byte[] result = md5.digest(); //result buffer 
	 
	    //String zusammenbauen
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < result.length; i++) {
	    	String s = Integer.toHexString(0xFF & result[i]);
	    	//beide teile des bytes geschrieben? nein? dann war das erste eine 0
	    	if(s.length() == 1){
	    		s = '0'+ s;
	    	}
	    	hexString.append(s);
	    }
	    return hexString.toString();
	}	
}
