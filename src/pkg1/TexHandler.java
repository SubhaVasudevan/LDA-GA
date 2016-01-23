package pkg1;


import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TexHandler implements ITextExtractionHandler {
	private static TexHandler instance = null;

	private TexHandler() {
	}

	public static TexHandler getInstance() {
		if (instance == null) {
			instance = new TexHandler();
		}
		return instance;
	}
	
	@Override
	public void Extract(File from, File to) {
		try {
			String currentDir = System.getProperty("user.dir");
			String relativePath = GetRelativePath(currentDir, from.getPath());
			// System.out.println(relativePath);
			// detex.exe only works with relative Path
			ProcessBuilder pb = new ProcessBuilder("detex.exe", "-n", "-w", relativePath);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectOutput(to);
			pb.start();
		} catch (IOException e) {
			System.out.println("Converting " + from.getAbsolutePath() + " error.");
			e.printStackTrace();
		}

	}
	// helper
	// convert absolute target Path to relative Path (relative to base path)
	private String GetRelativePath(String basePath, String targetPath){
		Path pathTarget = Paths.get(targetPath);
	    Path pathBase = Paths.get(basePath);
	    Path pathRelative = pathBase.relativize(pathTarget);
	    return pathRelative.toString();
	}
}
