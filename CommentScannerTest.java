import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommentScannerTest {

	@Test
	void testJava() {
		CommentScanner cs = new CommentScanner("test.java");
		Assertions.assertEquals(cs.numberOfLines(), 60);
		Assertions.assertEquals(cs.numberOfCommentLines(), 28);
		Assertions.assertEquals(cs.numberOfSingleLineComments(), 6);
		Assertions.assertEquals(cs.numberOfCommentLinesInBlockComments(), 22);
		Assertions.assertEquals(cs.numberOfBlockComments(), 2);
		Assertions.assertEquals(cs.numberOfTODOs(), 1);
	}
	
	void testJs() {
		CommentScanner cs = new CommentScanner("test.js");
		Assertions.assertEquals(cs.numberOfLines(), 40);
		Assertions.assertEquals(cs.numberOfCommentLines(), 23);
		Assertions.assertEquals(cs.numberOfSingleLineComments(), 5);
		Assertions.assertEquals(cs.numberOfCommentLinesInBlockComments(), 18);
		Assertions.assertEquals(cs.numberOfBlockComments(), 4);
		Assertions.assertEquals(cs.numberOfTODOs(), 1);
	}
	void testPy() {
		CommentScanner cs = new CommentScanner("test.py");
		Assertions.assertEquals(cs.numberOfLines(), 61);
		Assertions.assertEquals(cs.numberOfCommentLines(), 19);
		Assertions.assertEquals(cs.numberOfSingleLineComments(), 9);
		Assertions.assertEquals(cs.numberOfCommentLinesInBlockComments(), 10);
		Assertions.assertEquals(cs.numberOfBlockComments(), 3);
		Assertions.assertEquals(cs.numberOfTODOs(), 3);
	}

}
