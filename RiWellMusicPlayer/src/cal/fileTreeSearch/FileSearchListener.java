package cal.fileTreeSearch;

import java.util.Vector;

public interface FileSearchListener {
	public void onFolderFound(String lastfolderPath, long count);
	public void onProgress(long count, long max);
	public void onFileFound(String lastfilePath, long count);
	public void onStart();
	public void onFinish(Vector<String> files);
	public void onFileError(String errorFile);
}
