import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


public class Article extends Document {

	
	//the article will have a unique set of keywords
	//this is used only in 2nd level filtering where the
	//words specific to an article is to be found
	Set<String> uniqueKeyWordSet = new HashSet<String>();
	
	//trying the hashtable technique for cleaning cluster approach 2
	Hashtable<String, Integer> uniqueKeyWords = new Hashtable<String, Integer>();
	int totalWordCount = 0;
	
	public Article(String name, String keyWords) {
		// TODO Auto-generated constructor stub
		super(name, keyWords);
	}

}
