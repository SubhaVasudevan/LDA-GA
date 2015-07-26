import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


public class MalletInput {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		//pass the location and name of the processed data dir
		File dir = new File("processed-data");
		
		//the output is written to the file input.txt
		PrintWriter writer = new PrintWriter("input.txt", "UTF-8");

		String[] files = dir.list();
		
		for (String file : files) {
			File rd = new File("processed-data" + "/" + file);
			System.out.println(rd.getName());
			FileReader fr=new FileReader(rd);
			BufferedReader bufferedReader = new BufferedReader(fr);
            writer.print(rd.getName()+"\tX\t");
			String line;
	        while(( line = bufferedReader.readLine()) != null) {
	        	writer.print(line);
	        }    
	        bufferedReader.close();
			writer.println();
			

		}
		writer.close();
	}
	
}
