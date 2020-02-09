import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommentScanner cs = new CommentScanner(getFileName());
		if (cs.isSupportedFileType()) {
			System.out.println("Total # of lines: " + cs.numberOfLines());
			System.out.println("Total # of comment lines " + cs.numberOfCommentLines());
			System.out.println("Total # of single line components: " + cs.numberOfSingleLineComments());
			System.out.println("Total # of comment lines within block components: " + cs.numberOfCommentLinesInBlockComments());
			System.out.println("Total # of block line components: " + cs.numberOfBlockComments());
			System.out.println("Total # of TODO's: " + cs.numberOfTODOs());
		}
		else
			System.out.println("Unsupported file type");
	}
	private static String getFileName() {
		String fileName = "test.java";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter file name to scan: ");
			fileName = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
