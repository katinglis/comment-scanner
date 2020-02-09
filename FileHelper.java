import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHelper {
	public static List<String> readFile(String fileName) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName), Charset.forName("ISO-8859-1"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

}
