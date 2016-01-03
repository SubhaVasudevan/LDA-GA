import java.util.HashSet;
import java.util.Set;


public class Article extends Document {

	
	//the article will have a unique set of keywords
	//this is used only in 2nd level filtering where the
	//words specific to an article is to be found
	Set<String> uniqueKeyWords = new HashSet<String>();
	
	public Article(String name, String keyWords) {
		// TODO Auto-generated constructor stub
		super(name, keyWords);
	}

}
