 /* 
 *  Author: Katherine Inglis
 *  
 *  Written for Capital One technical challenge
 */
import java.util.ArrayList;
import java.util.List;

/*
 * CommentScanner class is to be used when a file is checked in. It's designed to scan
 * the file and report statistics about comment use and TODOs remaining in the comments.
 * 
 * The algorithm used is:
 *   1) Scan the file once to:
 *       i) record the locations (start and end lines and columns) of the comments 
 *       ii) count the number of single line comments 
 *       iii) count the number of block comments
 *       
 *   2) When numberOfTODOs() is called the comments are scanned to determine how many
 *      TODOs are in the comments.
 */
public class CommentScanner {
	private List<String> _lines;                          // An in-memory version of the file
	private List<Comment> _comments = new ArrayList<Comment>();
	private int _numSingleLineComments;                   // Number of single line comments in the file
	private int _numBlockComments;                        // Number of block comments in the file
	private boolean _isSupportedFileType;                 // Is the input file a recognized file type
	private boolean _supportsBlockComments;               // Does the file type support block comments 
	private String _singleLineCommentStartSymbol = "//";  // Single line comment symbol set to default value
	private String _blockCommentStartSymbol = "/*";       // Block symbol begin comment set to default value
	private String _blockCommentEndSymbol = "*/";         // End block symbol comment set to default value
	
	// Constructor: pass in the fileName to be scanned
	public CommentScanner(String fileName) {
		_isSupportedFileType = setCommentSymbols(fileName);
		if (_isSupportedFileType) {
			_lines = FileHelper.readFile(fileName);
			findComments();
		}
	}
	
	// Returns whether the input file is a recognized and supported filed type
	public boolean isSupportedFileType() {
		return _isSupportedFileType;
	}
	
	// Returns the number of lines in the input file
	public int numberOfLines() {
		return _lines.size();
	}
	
	/*
	 * numberOfCommentLines dynamically determines the number of lines with comments by
	 * walking the list of comments and totaling the number of line in each comment
	 */
	public int numberOfCommentLines() {
		int totalNumberOfCommentLines = 0;
		for(Comment c: _comments) {
			totalNumberOfCommentLines += c.endLineIndex - c.startLineIndex + 1;
		}
		return totalNumberOfCommentLines;
	}
	
	public int numberOfSingleLineComments() {
		return _numSingleLineComments;
	}
	
	public int numberOfCommentLinesInBlockComments() {
		return numberOfCommentLines() - _numSingleLineComments;
	}
	
	public int numberOfBlockComments() {
		return _numBlockComments;
	}
	
	/*
	 * numberOfTODOs: walk through the list of comments and search and count TODOs.
	 */
	public int numberOfTODOs() {
		int numTODOs = 0;
		for (Comment c: _comments) {
			for (int i = c.startLineIndex; i <= c.endLineIndex; i++) {
				String l = _lines.get(i).toUpperCase(); 	// call toUpperCase to make the search case-insensitive
				int beginIndex = (i == c.startLineIndex? c.startColumnIndex : 0);
				int endIndex = (i == c.endLineIndex? c.endColumnIndex : l.length()-1);
				if (l.substring(beginIndex, endIndex).contains("TODO")) {
					numTODOs++;
				}
			}
		}
		return numTODOs;
	}
	
	/*
	 * findComments goes line by line through the file and creates a list of Comment instances to
	 * record the start and end line and column number for each comment in the input file.
	 * 
	 * Limitation: Doesn't ignore comments inside a string, 
	 *             doesn't handle multiple comments on a single line
	 */
	private void findComments() {
		for (int i = 0; i < _lines.size(); i++) {
			int startLineIndex = i; // Keep track of what line we started on, because i may be incremented
			String l = _lines.get(i);
			if (l.trim().startsWith(_singleLineCommentStartSymbol)) { // Find full-line single line comments
				
				// Treat consecutive single line comments as a single block comment
				while (i+1 < _lines.size() && _lines.get(i + 1).trim().startsWith(_singleLineCommentStartSymbol)) {
					i = i + 1;
				}
				_comments.add(new Comment(startLineIndex, /*startColIndex*/0, /*endLineIndex*/i, _lines.get(i).length()));
				if (i == startLineIndex) {
					_numSingleLineComments++;
				} else {
					_numBlockComments++;
				}
			}
			else if (l.contains(_singleLineCommentStartSymbol)) {  // Find single line comments that start after some code
				_numSingleLineComments++;
				_comments.add(new Comment(i, l.indexOf(_singleLineCommentStartSymbol), i, l.length()));
			}
			else if (_supportsBlockComments && l.trim().contains(_blockCommentStartSymbol)) { // Find block comments
				int startColumnIndex = l.indexOf(_blockCommentStartSymbol);
				l = l.substring(startColumnIndex); // substring to remove the code preceding the comment
				int endColumnIndex = l.indexOf(_blockCommentEndSymbol);
				if (endColumnIndex == -1) { // Deal with the comment spanning multiple lines
					while (i+1 < _lines.size() && !_lines.get(++i).contains(_blockCommentEndSymbol))
						;		
					endColumnIndex = _lines.get(i).indexOf(_blockCommentEndSymbol);
					_comments.add(new Comment(startLineIndex, startColumnIndex, i, endColumnIndex));
				} 
				else {
					_comments.add(new Comment(i, startColumnIndex, i, startColumnIndex + endColumnIndex)); 
				}
				_numBlockComments++;	
			}
		}		
	}
	
	/*
	 * Comment class records the start and end locations of a comment
	 */
	public class Comment {
		public int startLineIndex, startColumnIndex, endLineIndex, endColumnIndex;
		public Comment(int sLine, int sCol, int eLine, int eCol) {
			startLineIndex = sLine;
			startColumnIndex = sCol;
			endLineIndex = eLine;
			endColumnIndex = eCol;
		}
	}
	
	/* 
	 * setCommentsSymbols sets language specific comment strings and returns true if the file type is supported
	 * 
	 * TODO: Add support for more language types.
	 */
	private boolean setCommentSymbols(String fileName) {
		if (fileName.charAt(0) == '.') {
			return false;  // Don't support fileNames starting with a period
		}
		
		int i = fileName.lastIndexOf("."); 
		if (i < 0) { 
			return false; // Don't support filenames without a suffix
		}
		
		String fileNameSuffix = fileName.substring(i + 1);
		boolean supportedFileType = true;
		switch (fileNameSuffix) {
			case "java":
			case "js":
				_supportsBlockComments = true;
				break;
			case "py":
				_singleLineCommentStartSymbol = "#";
				break;
			default:
				supportedFileType = false;
				break;
		}
		return supportedFileType;	
	}
}
