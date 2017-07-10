import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import java.util.*;


public class CaseLooper {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "caseid,filename,defendantname,defenseattorney,prosecutor,relatedcases,"
    		+ "numcharges,charges,levels,disposition,incarcerated,suspended,sentence,"
    		+ "minimum,maximum";

	public static void main(String[] args) throws Exception {
		
	    // TreeMap will hold the filename and a Case object
		TreeMap<String,Case> Cases = new TreeMap<String,Case>();

		final String dir = System.getProperty("user.dir");
	    System.out.println("current dir = " + dir);
		   
	    String[] caseList = {"testpage.html", "testpage2.html", "testpage3.html", "testpage4.html", "testpage5.html", 
	    		"testpage6.html", "testpage7.html", "testpage8.html", "testpage9.html", "testpage10.html"};
	   
	    for(String s : caseList){
	    	Cases.put(s, new Case(Jsoup.parse(new File(s), "utf-8"))); 
	    }	    
	    System.out.println("Number of Cases created: " + Cases.size());
	    
	       
	    String fileName = "CaseOutput.csv"; 
	    FileWriter fileWriter = null;
	    try {
	    	fileWriter = new FileWriter(fileName);
	        
	        //Write the CSV file header
	    	fileWriter.append(FILE_HEADER.toString());

	    	//Add a new line separator after the header
	    	fileWriter.append(NEW_LINE_SEPARATOR);

	    	//Loop over Cases - create new object and write to the CSV file
	    	for(String k: Cases.keySet()){
	    		System.out.println(k);
	    		
	    		// Cross Reference Number
	    		fileWriter.append(Cases.get(k).getCrossRefNumber()); 
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// File Name
	    		fileWriter.append(k); 
	    		fileWriter.append(COMMA_DELIMITER);

	    		// Defendant Name
	    		fileWriter.append(Cases.get(k).getDefendantName());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Defense Attorney
	    		fileWriter.append(Cases.get(k).getDefenseAttorney());
	    		fileWriter.append(COMMA_DELIMITER);

	    		// Prosecutor
	    		fileWriter.append(Cases.get(k).getProsecutingAttorney());
	    		fileWriter.append(COMMA_DELIMITER);

	    		// Related Cases
	    		fileWriter.append(Cases.get(k).getRelatedCaseTable().getRelatedCasesString());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Number of charges
	    		fileWriter.append(Cases.get(k).getCharges().size()+"");
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// List of charges
	    		fileWriter.append(Cases.get(k).getChargesString());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// List of charge level
	    		fileWriter.append(Cases.get(k).getChargeLevelsString());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Dispositions
	    		fileWriter.append(Cases.get(k).getDispositionsString());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Sentences - incarcerated
	    		fileWriter.append(Cases.get(k).isIncarcerated()+"");
	    		fileWriter.append(COMMA_DELIMITER);

	    		// Sentences - suspended
	    		fileWriter.append(Cases.get(k).isSuspended()+"");
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Sentences text
	    		fileWriter.append(Cases.get(k).getSentencesString());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Sentences Min
	    		fileWriter.append(Cases.get(k).getMaxSentenceText());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Sentences Max
	    		fileWriter.append(Cases.get(k).getMinSentenceText());
	    		
	    		fileWriter.append(NEW_LINE_SEPARATOR);
	    	}
	    	System.out.println("CSV file was created successfully !!!");
	    } catch (Exception e) {

	    	System.out.println("Error in CsvFileWriter !!!");
	    	e.printStackTrace();
	    } finally {
	    	try {
	    		fileWriter.flush();
	    		fileWriter.close();
	    	} catch (IOException e) {
	    		System.out.println("Error while flushing/closing fileWriter !!!");
	    		e.printStackTrace();
	    	}
	    }
	       
	
	}
}
