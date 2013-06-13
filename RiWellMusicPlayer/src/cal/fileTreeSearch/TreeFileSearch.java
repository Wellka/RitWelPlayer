package cal.fileTreeSearch;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.SwingWorker;


/**
 * 
 * Used for fast File search and browsing
 * 
 * WARNING!
 * this class is not meant to be used repeadly.
 * it is nor indexing or listing the found files, if this functionallity is needed, is has to be done by the user.
 * 
 * @author Kay Wellinger
 */

public class TreeFileSearch {
	
	SwingWorker<Object, Object> worker;
	Vector<String> fileList;
	FileSearchListener fsListener;
	
	public static final int RECURSION_UNLIMITED = -1;
	public static final int RECURSION_NONE = 0;
	
	/**
	 * @param fsListener Listener for events. CAN BE null
	 * @param startPath Start path, has to be != null
	 * @param filters filters are strings like ".mp3", ".mp4", "filename" 
	 * WARNING this class doesnt make use from wildcards. filters must be sepperated by ;
	 * each filter is used to search IN the filename. For ex.: 
	 * Filters are: ".mp3", ".mp4", "filename"
	 * Files in the folder are: 
	 * "filename.mp3"
	 * "filename.txt"
	 * "anotherfile.txt"
	 * "anotherfile.mp3.mp4"
	 * "anotherfile.mp3"
	 *  
	 *  results in:
	 *  "filename.mp3"
	 *  "filename.txt"
	 *  "anotherfile.mp3.mp4"
	 *  "anotherfile.mp3"
	 */
	public TreeFileSearch(FileSearchListener fsListener,String startPath, int maxRekursiv, String... filters) {
		//prüfen ob datei existiert
		if(!new File(startPath).exists()){
			fsListener.onFileError(startPath);
			return;
		}
		
		fileList = new Vector<String>();
		this.fsListener = fsListener;
		if(fsListener != null)
			fsListener.onStart();
		worker = new FileTreeWorker(maxRekursiv,startPath, fileList, filters);
		worker.execute();
		holdWorkerAlive();
	}
	
	public boolean isDone(){
		if(worker != null){
			return worker.isDone();
		}
		else
		{
			return false;
		}
	}
	
	public Vector<String> getFileList(){
		if(isDone())
			return fileList;
		return null;
	}
	
	public void holdWorkerAlive(){
		//worker.notify(); // wirft war einfen fehler //TOTO fehler abfangen // aber läuft so wenigstens durch! kleiner hack?
	}
	
	class FileTreeWorker extends SwingWorker<Object, Object>{
		int currentLevel;
		int searchedDirs;
		int searchedFiles;
		int maxRekursiv;
		String[] filters;
		Vector<String> strlist;
		
		public FileTreeWorker(int maxRekursiv, String path, Vector<String> strFileList,String... filters) {
			this.filters = filters;
			this.maxRekursiv = maxRekursiv;
			strlist = strFileList; // = new Vector<String>();
			try {
				walk(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fsListener.onFinish(fileList);
		}

		public void walk( String path ) throws IOException {
			currentLevel++;
			
	        File root = new File( path );
	        System.out.println(path);
	        File[] list = root.listFiles(); // alle möglichen dateien auflisten
	        
	        if(list != null){
	        	long progres = 0;
		        for ( File f : list ) {
		            if ( f.isDirectory()) {
		            	if(currentLevel <= maxRekursiv || maxRekursiv == RECURSION_UNLIMITED){
		            		searchedDirs ++;
		            		if(fsListener != null)
		            			fsListener.onFolderFound(f.getAbsolutePath(), searchedDirs);
		            		walk(f.getAbsolutePath());
		            	}	
		            }
		            else {
		            	if(filters != null){
		            		for (String string : filters) {
		            			if(f.getName().contains(string)){
					            	//System.out.println(f.getName());					            	
					            	searchedFiles++;
					            	fileList.add(f.getAbsolutePath());
				            		if(fsListener != null)
				            			fsListener.onFileFound(f.getAbsolutePath(), searchedFiles);					           
					            	break;
		            			}
							}
		            	}else{
		            		//System.out.println(f.getName());
			            	searchedFiles++;
			            	fileList.add(f.getAbsolutePath());
			            	if(fsListener != null)
		            			fsListener.onFileFound(f.getAbsolutePath(), searchedFiles);
		            	}
		            }
		            progres++;
		            fsListener.onProgress(progres, list.length);
		        }
		        currentLevel--;    
	        }
		}

		@Override
		protected Object doInBackground() throws Exception {
			return currentLevel;
		}	
	}
}