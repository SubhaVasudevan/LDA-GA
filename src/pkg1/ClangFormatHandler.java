package pkg1;


import java.io.*;

/**
 * @author William Pfeffer
 *         Created on 5/1/2015
 *         <p>
 *         Description:
 *         <p>
 *         Expected Use
 *         <p>
 *         Limitations:
 */
public class ClangFormatHandler implements ITextExtractionHandler {

    private static ClangFormatHandler instance = null;

    private ClangFormatHandler() {
    }

    public static ClangFormatHandler getInstance() {
        if (instance == null) {
            instance = new ClangFormatHandler();
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
