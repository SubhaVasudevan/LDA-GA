package pkg1;


import java.io.*;


public class ShHandler implements ITextExtractionHandler {

	private static ShHandler instance = null;

	private ShHandler() {
	}

	public static ShHandler getInstance() {
		if (instance == null) {
			instance = new ShHandler();
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
