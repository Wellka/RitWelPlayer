package musikData;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.SwingWorker;

public class MusikDataSearch {
	private String[] splitString;
	public static final int UNLIMITED_DEPTH = -1;

	Vector<String> filePaths = new Vector<String>();
	
	FileTreeWorker ftw;
	
	public MusikDataSearch(String path, int maxDepth) {
		ftw = new FileTreeWorker(path, maxDepth, filePaths);	
		
		ftw.execute();
	}
	
	
	/**
	 * @return wenn fertig die liste der dateien, wenn nicht fertig null
	 */
	public Vector<String> getFilePaths(){
		if(isDone()){
			return filePaths;
		}else{
			return null;
		}
	}
	
	/**
	 * @return wenn fertig die liste der musikdaten(id3tags, pfade, ect), wenn nicht fertig null
	 */
	public Vector<MusikInformation> getMusikData(){
		if(isDone()){
			Vector<MusikInformation> musikInformations = new Vector<MusikInformation>();
			for (String s : filePaths) {
				//TODO ID3TAG HOLEN
				musikInformations.add(new MusikInformation(null, null, null, s, null)); //TODO
			}
			
			return musikInformations;
		}else{
			return null;
		}
	}
	
	public boolean isDone(){
		return ftw.isDone();
	}
	
	class FileTreeWorker extends SwingWorker<Object, Object>{

		int currentLevel;
		int searchedDirs;
		int searchedFiles;
		
		String myPath;
		int maxRekursiv;
		Vector<String> arrStr;
		
		public FileTreeWorker(String path, int maxDepth, Vector<String> arrStr){
			myPath = path;
			this.maxRekursiv = maxDepth;
			this.arrStr = arrStr;
		}
		
		public void walk( String path ) throws IOException {
			if(arrStr == null)
				return;
			
			currentLevel++;
	        File root = new File( path );
	        File[] list = root.listFiles();
	        
	        if(list != null)
	        for ( File f : list ) {
	            if ( f.isDirectory()) { //wenn ordner, weiter duchsuchen
	            	if(currentLevel <= maxRekursiv || maxRekursiv == UNLIMITED_DEPTH){ //wenn rekursiv an und maximalre rekursivität nicht erreicht
	            		searchedDirs ++;
	            		walk(f.getAbsolutePath() );
	            	}     		
	            }
	            else {
	            	if(splitString != null){
	            		for (String string : splitString) {
	            			if(f.getName().contains(string)){
				            	searchedFiles++;
				            	arrStr.add(f.getAbsolutePath());
				            	System.out.println(f.getAbsolutePath());
	            			}
						}
	            	}else{
		            	searchedFiles++;
		            	arrStr.add(f.getAbsolutePath());
		            		//statement.execute("INSERT INTO filesearch (path) VALUES ('"+ val +"');");
	            	}
	            }   
	        }
	        currentLevel--;
	        //return strlist;
	    }
		
		@Override
		protected Object doInBackground() throws Exception {
			walk(myPath);
			return null;
		}	
	}
}