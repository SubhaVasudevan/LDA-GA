package pkg1;


import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.*;


public class PdfHandler implements ITextExtractionHandler {

	private static PdfHandler instance = null;

	private PdfHandler() {
	}

	public static PdfHandler getInstance() {
		if (instance == null) {
			instance = new PdfHandler();
		}
		return instance;
	}
	
	@Override
	public void Extract(File from, File to){
		BufferedReader br;
		BufferedWriter bw;
		try {
			PDDocument pd = PDDocument.load(from);
			PDFTextStripper stripper = new PDFTextStripper();
			File tempOutput = new File(to.getParent()+ "\\temp.txt"); // write to temp.txt
			bw = new BufferedWriter(new FileWriter(tempOutput));
			stripper.writeText(pd, bw);
	         if (pd != null) {
	             pd.close();
	         }
	        bw.close();
	        
	        // clean up temp.txt
			br = new BufferedReader(new FileReader(tempOutput));
			bw = new BufferedWriter(new FileWriter(to));
			String line;
			while ((line = br.readLine()) != null) {
			   String plainText = line.replaceAll("[^\\w]", " ")
					                  .replaceAll("\\b\\w{1,2}\\b", " ") // remove 1-2 character words
					   				  .replaceAll("\\s+", " ")
					                  .toLowerCase();
			   bw.write(plainText);
			   bw.newLine();
			}
			br.close();
			bw.close();
			tempOutput.delete(); // delete temp.txt
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
