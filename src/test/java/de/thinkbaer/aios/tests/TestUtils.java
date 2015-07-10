package de.thinkbaer.aios.tests;

import java.io.File;
import java.io.IOException;

public class TestUtils {

	public static void createDir(String path) throws IOException{
		createDir(path, false);
	}

	public static void clearAndCreateDir(String path) throws IOException{
		createDir(path, true);
	}

	public static void createDir(String path, boolean clearBefore) throws IOException{
		File f = new File(path);
		
		if(clearBefore && f.exists()){
			clearDirectory(path);
		}
		
		if(!f.exists()){
			f.mkdirs();
		}
	}

	public static void clearDirectory(String dir) throws IOException {
		File f = new File(dir);
		if (!f.exists()) {
			throw new IOException("Directory not present or not writable");			
		}
		if (f.isDirectory()) {
			deleteFolder(f);
		} else {
			throw new IOException("Is not a directory");
		}
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}

}
