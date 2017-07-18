import java.io.*;
import java.util.*;

public class CaseInput {
	private Scanner s;
	TreeMap<Integer, String> caseNumbers;
	
	public CaseInput() {
		caseNumbers = new TreeMap<Integer, String>();
	}
	
	// method to open file that is to be read and throw exception if unsuccessful 
	public void open() {
		try {
			// Name of input file: Change here for updated samples. Needs a file of courtnums without column header
			s = new Scanner(new File("MisDisJudgeName.txt"));
		}	
		catch (Exception E) {
			System.out.println("File not found!");
		}
	}
	
	// method to read in contents of file into treemap of casenumbers  
	public void read() {
		int count = 0;
		while(s.hasNext()) {
			String testString = s.next();
			caseNumbers.put(count, testString);
			count++;
		}
	}

	// method to close input file
	public void close() {
		s.close();
	}

}