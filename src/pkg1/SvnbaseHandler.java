package pkg1;


import java.io.*;


public class SvnbaseHandler implements ITextExtractionHandler {

	private static SvnbaseHandler instance = null;

	private SvnbaseHandler() {
	}

	public static SvnbaseHandler getInstance() {
		if (instance == null) {
			instance = new SvnbaseHandler();
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
					                  //.replaceAll("\\b\\w{1,2}\\b", " ") // remove 1-2 character words
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
