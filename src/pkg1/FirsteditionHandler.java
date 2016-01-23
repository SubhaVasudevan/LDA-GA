package pkg1;


import java.io.*;


public class FirsteditionHandler implements ITextExtractionHandler {

	private static FirsteditionHandler instance = null;

	private FirsteditionHandler() {
	}

	public static FirsteditionHandler getInstance() {
		if (instance == null) {
			instance = new FirsteditionHandler();
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
			   plainText = plainText.replaceAll("newlabel", "");
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
