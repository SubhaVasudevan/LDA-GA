package pkg1;


import java.io.*;


public class XmlHandler implements ITextExtractionHandler {

	private static XmlHandler instance = null;

	private XmlHandler() {
	}

	public static XmlHandler getInstance() {
		if (instance == null) {
			instance = new XmlHandler();
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
			   plainText = plainText.replaceAll("attribute", "")
			                        .replaceAll("property", "")
			                        .replaceAll("dataset[s]?", "");
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
