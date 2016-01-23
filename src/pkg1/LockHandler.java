package pkg1;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class LockHandler implements ITextExtractionHandler {

	private static LockHandler instance = null;

	private LockHandler() {
	}

	public static LockHandler getInstance() {
		if (instance == null) {
			instance = new LockHandler();
		}
		return instance;
	}
	
	@Override
	// Punctuation are not deleted here. 
	// GroundTruth shows difference comes from punctuation
	public void Extract(File from, File to){
		try {
			Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
