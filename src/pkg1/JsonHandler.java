package pkg1;


import java.io.*;


public class JsonHandler implements ITextExtractionHandler {

	private static JsonHandler instance = null;

	private JsonHandler() {
	}

	public static JsonHandler getInstance() {
		if (instance == null) {
			instance = new JsonHandler();
		}
		return instance;
	}
	
	@Override
	public void Extract(File from, File to){
		BufferedReader br;
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(from));
			bw = new BufferedWriter(new FileWriter(to));
			String line;
			while ((line = br.readLine()) != null) {
			   	
			   String plainText = line.replaceAll("[^\\w]", " ") // remove non-word character [^[a-zA-Z_0-9]]
					                  .replaceAll("\\s+", " ") // remove white space
					                  .toLowerCase(); 
			   bw.write(plainText);
			   bw.newLine();
			}
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
