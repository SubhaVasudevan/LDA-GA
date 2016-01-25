package WikiScraping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiScrape {
	
	int noOfArticles = 0;
	int sourceFileCount = 0;
	HashMap<String, Boolean> pageMap = new HashMap<>();
	
	Hashtable<String, ArrayList<String>> truthFile = new Hashtable<String, ArrayList<String>>();

	public WikiScrape(int i) {
		noOfArticles = i;
		
		//create a folder for the raw data
		new File("rawData").mkdir();
	}
	
	private void scrape() {
		Document doc;
		
		for(int i = 0; i < noOfArticles; ) {
            try {

                // This URL brings us to a random wiki article
                String url = Jsoup.connect("https://en.wikinews.org/wiki/Special:Random").followRedirects(true).execute().url().toExternalForm();

                // Connect to the new URL
                doc = Jsoup.connect(url).get();

                // Get the title, and removed invalid characters
                String article_title = doc.title();

                // Get rid of wiki title, if it exists
                if(article_title.contains("- Wikinews, the free news source"))
                    article_title = article_title.replace(" - Wikinews, the free news source", "");

                article_title = article_title.replaceAll("[^a-zA-Z0-9.-]", "_");

                // Perform a simple check to make sure we haven't attempted that page already
                if(pageMap.containsKey(article_title)){
                    continue;
                } else {
                    pageMap.put(article_title, true);
                }

                // Verify all of the sources

                ArrayList<String> sources = sourceCheck(doc);
                
                
                
                
                if(sources != null){
                	truthFile.put(article_title + "$AAA$.html.txt", sources);

                    //FXML_Handler.setUpdateText(Constants.SCRAPE_MODE, "Valid Source Found - " + article_title);

                    saveWikiArticle(doc, article_title);
                   // updateTurtleFile(article_title + ".html", sources);

                    i++;

                    //FXML_Handler.setStatusLabel("Gathering Test Set (" + i + "/" + numToScrape + ")");

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
		}

	}
	
	public ArrayList<String> sourceCheck(Document doc){

        Elements tLink = doc.select("li a.text");

        ArrayList<String> foundSources = new ArrayList<>();
        ArrayList<String> acceptedSources = new ArrayList<>();

        for(Element ele : tLink){
            foundSources.add(ele.attr("href"));
        }

        if(foundSources.isEmpty())
            return null;

        for(String url : foundSources){

            try {
                // See if we are on the page that is intended
                Connection.Response response = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000)
                        .followRedirects(false)
                        .execute();

                // Status code 200 is success
                int statusCode = response.statusCode();
                if(statusCode == 200) {
                   // acceptedSources.add(url);
                	acceptedSources.add(sourceFileCount + ".html.txt");
                    saveSourceFile(response.parse());
                }


            } catch (Exception e){ } // Execute generates exceptions if a connection hasn't been made
        }

        if(acceptedSources.isEmpty())
            return null;


        return acceptedSources;
    }
	
	public void saveSourceFile(Document doc){

        // Write the doc to the summary file with the number
		//commented for now
		//might need it later
       /* try(BufferedWriter w = new BufferedWriter(new FileWriter(summaryFile, true))) {
            w.write(sourceFileCount + " " + doc.baseUri() + "\n");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }*/

        // Write the source file
        try {
            File temp = new File("rawData/" + sourceFileCount + ".html");
            BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"));
            htmlWriter.write(doc.toString());
            htmlWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        sourceFileCount++;

    }
	
	public void saveWikiArticle(Document doc, String title){
        // Write the wiki file
        try {
            File temp = new File("rawData/" + title + "$AAA$.html");
            BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"));
            htmlWriter.write(doc.toString());
            htmlWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
	
	private void writeGroundTruth() throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter("truthfile.txt", "UTF-8");
		
		for(String key : truthFile.keySet()) {
			ArrayList<String> sources = truthFile.get(key);
			writer.print(key+"#");
			
			for(String source:sources) {
				writer.print(" "+source);
			}
			writer.println();
		}
		
		writer.close();
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		WikiScrape ws = new WikiScrape( 6 );
		ws.scrape();
		ws.writeGroundTruth();
		TextConversion.convertToTxt();
		
		
	}
}
