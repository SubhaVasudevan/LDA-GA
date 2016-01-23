package pkg1;


import java.io.*;

public class BibHandler implements ITextExtractionHandler {

	private static BibHandler instance = null;

	private BibHandler() {
	}

	public static BibHandler getInstance() {
		if (instance == null) {
			instance = new BibHandler();
		}
		return instance;
	}

	@Override
	public void Extract(File from, File to) {
		BufferedReader br;
		BufferedWriter bw;
		try {
			br = new BufferedReader(new FileReader(from));
			bw = new BufferedWriter(new FileWriter(to));
			String line;
			while ((line = br.readLine()) != null) {
				String plainText = line.replaceAll("^@\\w+\\{", "") 
						.replaceAll("^\\t[A-Za-z]+ = ", "") // remove left side of = 
						.replaceAll("[^\\w]", " ") // remove non-word character	
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
