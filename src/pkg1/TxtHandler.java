package pkg1;


import java.io.*;


public class TxtHandler implements ITextExtractionHandler {

	private static TxtHandler instance = null;

	private TxtHandler() {
	}

	public static TxtHandler getInstance() {
		if (instance == null) {
			instance = new TxtHandler();
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
			   String plainText = line.replaceAll("[^\\w]", " ")
					   				  .replaceAll("\\s+", " ")
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
