
import java.io.*;
import java.util.*;

public class CaseInput {
	private Scanner s;
	private String[] caseNumbers; 
	
	public CaseInput() {
		caseNumbers = new String[100];
	}

	public void open() {
		try {
			s = new Scanner(new File("CourtDataSample_MissDispo.txt"));
		}	
		catch (Exception E) {
			System.out.println("File not found!");
		}
	}
	
	public void read() {
		int count = 0;
		while(s.hasNext()) {
			String testString = s.next();
			caseNumbers[count] = testString; 
//			System.out.println(count + ". " + caseNumbers[count]);
			count++;
		}
	}
	
	public void initializeArray(String[] caseNumbers) {
		for (int i = 0; i < 100; i++) {
			caseNumbers[i] = this.caseNumbers[i];
//			System.out.println(caseNumbers[i]);
		}
	}
	
	public void close() {
		s.close();
	}

}