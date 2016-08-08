//$Id$
package pathTraverse;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

public class PathTraverse {
	
	private File curDir = null;
	private JSONArray pathTree = null;
	
	
	public PathTraverse(String path) {
		curDir = new File(path);
		pathTree = new JSONArray();
	}
	
	public static void main(String[] args) {
		String filePath = "/Users/venkat-zutk19/Downloads";
		PathTraverse path = new PathTraverse(filePath);
		path.listFilesRecursively(path.curDir,path.pathTree);
		System.out.println(path.pathTree);
	}
	
	public void listFilesRecursively(File mainfile,JSONArray curPath) {
		String[] files = listFiles(mainfile);
		files = files == null ? "".split(",") : files;
		for (String fileName : files) {
			File file = new File(mainfile.getAbsoluteFile()+File.separator+fileName);
			if (isFolder(file)) {
				System.out.println(file);
				JSONObject obj = new JSONObject();
				JSONArray curArr = new JSONArray();
				obj.put(fileName, curArr);
				curPath.put(obj);
				listFilesRecursively(file.getAbsoluteFile(),curArr);
			}
			else {
				curPath.put(fileName);
			}
		}
	}
	
	
	public boolean isFolder(File file) {
		return file.isDirectory() && file.canRead() && !file.getName().endsWith(".app");
	}
	
	public String[] listFiles(File file) {
		return file.list();
	}
	
	
}
