package pkg1;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class SedHandler implements ITextExtractionHandler {

	private static SedHandler instance = null;

	private SedHandler() {
	}

	public static SedHandler getInstance() {
		if (instance == null) {
			instance = new SedHandler();
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
