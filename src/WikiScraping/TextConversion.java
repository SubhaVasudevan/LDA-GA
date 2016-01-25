package WikiScraping;

import java.io.File;
import java.util.HashMap;

import pkg1.BibHandler;
import pkg1.BowerrcHandler;
import pkg1.BstHandler;
import pkg1.CjsHandler;
import pkg1.ClangFormatHandler;
import pkg1.ConfHandler;
import pkg1.CssHandler;
import pkg1.CsvHandler;
import pkg1.DartHandler;
import pkg1.ES5Handler;
import pkg1.ES6Handler;
import pkg1.EditorConfigHandler;
import pkg1.ElHandler;
import pkg1.FirsteditionHandler;
import pkg1.GeneralHandler;
import pkg1.GitignoreHandler;
import pkg1.HtmlHandler;
import pkg1.ITextExtractionHandler;
import pkg1.IncludeHandler;
import pkg1.JavaHandler;
import pkg1.JsHandler;
import pkg1.JsonHandler;
import pkg1.LockHandler;
import pkg1.MdHandler;
import pkg1.NullHandler;
import pkg1.PdfHandler;
import pkg1.PyHandler;
import pkg1.ScssHandler;
import pkg1.SedHandler;
import pkg1.ShHandler;
import pkg1.StyHandler;
import pkg1.SvnbaseHandler;
import pkg1.TemplateHandler;
import pkg1.TexHandler;
import pkg1.TrigHandler;
import pkg1.TtlHandler;
import pkg1.TxtHandler;
import pkg1.VHandler;
import pkg1.XmlHandler;
import pkg1.YamlHandler;
import pkg1.YmlHandler;

public class TextConversion {
	
private static HashMap<String, ITextExtractionHandler> handlerMap;
	
	static {

		handlerMap = new HashMap<String, ITextExtractionHandler>();
		handlerMap.put("bib", BibHandler.getInstance());
		handlerMap.put("bst", BstHandler.getInstance());
		handlerMap.put("css", CssHandler.getInstance());
		handlerMap.put("csv", CsvHandler.getInstance());
		handlerMap.put("el", ElHandler.getInstance());
		handlerMap.put("firstedition", FirsteditionHandler.getInstance());
		handlerMap.put("gitignore", GitignoreHandler.getInstance());
		handlerMap.put("htm", HtmlHandler.getInstance());
		handlerMap.put("html", HtmlHandler.getInstance());
		handlerMap.put("js", JsHandler.getInstance());
		handlerMap.put("json", JsonHandler.getInstance());
		handlerMap.put("lock", LockHandler.getInstance());
		handlerMap.put("md", MdHandler.getInstance());
		handlerMap.put("null", NullHandler.getInstance());
		handlerMap.put("pdf", PdfHandler.getInstance());
		handlerMap.put("py", PyHandler.getInstance());
		handlerMap.put("sed", SedHandler.getInstance());
		handlerMap.put("sh", ShHandler.getInstance());
		handlerMap.put("sty", StyHandler.getInstance());
		handlerMap.put("svnbase", SvnbaseHandler.getInstance());
		handlerMap.put("tex", TexHandler.getInstance());
		handlerMap.put("trig", TrigHandler.getInstance());
		handlerMap.put("ttl", TtlHandler.getInstance());
		handlerMap.put("txt", TxtHandler.getInstance());
		handlerMap.put("xml", XmlHandler.getInstance());
		handlerMap.put("v", VHandler.getInstance());
		handlerMap.put("yml", YmlHandler.getInstance());

		handlerMap.put("java", JavaHandler.getInstance());
        handlerMap.put("bowerrc", BowerrcHandler.getInstance());
        handlerMap.put("cjs", CjsHandler.getInstance());
        handlerMap.put("clang-format", ClangFormatHandler.getInstance());
        handlerMap.put("dart", DartHandler.getInstance());
        handlerMap.put("editorconfig", EditorConfigHandler.getInstance());
        handlerMap.put("es5", ES5Handler.getInstance());
        handlerMap.put("es6", ES6Handler.getInstance());
        handlerMap.put("es6d", ES6Handler.getInstance());
        handlerMap.put("inc", IncludeHandler.getInstance());
        handlerMap.put("scss", ScssHandler.getInstance());
        handlerMap.put("template", TemplateHandler.getInstance());
        handlerMap.put("yaml", YamlHandler.getInstance());
        handlerMap.put("conf",ConfHandler.getInstance());

		handlerMap.put("c",GeneralHandler.getInstance());
		handlerMap.put("h",GeneralHandler.getInstance());
		handlerMap.put("cc",GeneralHandler.getInstance());
		handlerMap.put("android",GeneralHandler.getInstance());
		handlerMap.put("am",GeneralHandler.getInstance());
		handlerMap.put("adb",GeneralHandler.getInstance());
		handlerMap.put("ac",GeneralHandler.getInstance());
		handlerMap.put("7",GeneralHandler.getInstance());
		handlerMap.put("3",GeneralHandler.getInstance());
		handlerMap.put("1",GeneralHandler.getInstance());
		handlerMap.put("pl",GeneralHandler.getInstance());
		handlerMap.put("pod",GeneralHandler.getInstance());
		handlerMap.put("asm",GeneralHandler.getInstance());
		handlerMap.put("s",GeneralHandler.getInstance());

	}


	public static void convertToTxt() {
		new File("txtData").mkdir();
		
		// get teh html files from the directory
		File dir = new File("rawData");
		
		String[] files = dir.list();
		
		for (String file : files) {
			
			System.out.println(file);
			File from = new File("rawData/"+file);
	    	File to = new File("txtData/" + from.getName() +".txt");
	    	String ext = getExtension(from);
	    	
	    	ITextExtractionHandler handler = handlerMap.get(ext);
	    	handler.Extract(from,to);

		}
	}
	
	// return file extension 
	 	public static String getExtension(File f) {
	 		String filename = f.getName();
	 		int i = filename.lastIndexOf('.');
	 		if (i >= 0) {
	 		    return filename.substring(i + 1)
	 		    		.toLowerCase()	   
	 		    		.replace("-", ""); // remove hyphen
	 		}else{
	 			return "null";  // no suffix
	 		}
	 	}
}
