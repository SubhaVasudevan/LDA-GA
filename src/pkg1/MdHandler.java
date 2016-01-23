package pkg1;


import java.io.*;


public class MdHandler implements ITextExtractionHandler {

	private static MdHandler instance = null;

	private MdHandler() {
	}

	public static MdHandler getInstance() {
		if (instance == null) {
			instance = new MdHandler();
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
			   	
			   String plainText = line.replaceAll("[^\\w]", " ") // remove non-alphabetic character			                  
					                  //.replaceAll("\\b\\w{1,2}\\b", " ")
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
