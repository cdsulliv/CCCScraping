import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.TreeMap;

public class CaseLooper {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "original_caseid,scraped_caseid,casefound,defendantname,defenseattorney,prosecutor,relatedcases,"
    		+ "numcharges,charges,levels,disposition,incarcerated,suspended,suspendedtext,sentence,"
    		+ "minimum,maximum,sentencedtotext,minNo, minUnit, maxNo, maxUnit";
	private static String[] caseNumbers;
	private static String[] refinedCaseNumbers;
	private static SeleniumCCC seleniumInstance;

	public static void main(String[] args) throws Exception {
		// caseNumbers - array of Strings to store first 100 case numbers 
		caseNumbers = new String[100];
		refinedCaseNumbers = new String[100];
		CaseInput testInput = new CaseInput();
		testInput.open();
		testInput.read();
		testInput.initializeArray(caseNumbers);
		testInput.close();
		
		// code to refine all the case numbers that have been read in to caseNumbers
		CaseNumberRefiner refiner = new CaseNumberRefiner();
		for (int i = 0; i < caseNumbers.length; i++) {
//			System.out.println("Old: " + caseNumbers[i]);
			refinedCaseNumbers[i] = refiner.Refiner(caseNumbers[i]);
//			System.out.println("New: " + caseNumbers[i]);
		}
		
	    // TreeMap will hold the filename and a Case object
		TreeMap<String,Case> Cases = new TreeMap<String,Case>();

		final String dir = System.getProperty("user.dir");
	    System.out.println("current dir = " + dir);
	    
	    
		seleniumInstance = new SeleniumCCC();
		seleniumInstance.setUp();
		String page = "";
	    for(int i = 0; i < refinedCaseNumbers.length - 94; i++) {
	    	// 1) Get searchable string - DONE
	    	String currentCaseNumber = refinedCaseNumbers[i];
	    	
	    	// 2) Launch Selenium browser and navigate to page - DONE
	    	if (i == 0) {
				page = seleniumInstance.testFirstClark(currentCaseNumber, 0);
	    	}
	    	else {
	    		page = seleniumInstance.testFirstClark(currentCaseNumber, 1);
	    	}
	    	
	    	// 3) Get page source
//	    	String page = driver.getSource();
	    	
	    	// 4) Create new case
//	    	Cases.put(s, new Case(Jsoup.parse(new File(s), "utf-8")));
	    	Cases.put(currentCaseNumber, new Case(Jsoup.parse(page), caseNumbers[i]));
	    	if(Cases.get(currentCaseNumber).getCharges() == null) {
	    		System.out.println("CHARGES WERE INITIALLY NULL; RERUNNING");
	    		page = seleniumInstance.testFirstClark(currentCaseNumber, 1);
		    	Cases.put(currentCaseNumber, new Case(Jsoup.parse(page), caseNumbers[i]));
	    		
	    	}
	    	
	    	// 5) Close driver
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
	    		
	    		// Original case id
	    		fileWriter.append(Cases.get(k).getOriginalCaseNumber()); 
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Scraped case id
	    		fileWriter.append(Cases.get(k).getCrossRefNumber()); 
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Case Found
	    		fileWriter.append(Cases.get(k).getCaseFound() + ""); 
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
	    		fileWriter.append(Cases.get(k).getCharges().size() + "");
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
	    		fileWriter.append(Cases.get(k).isIncarcerated() + "");
	    		fileWriter.append(COMMA_DELIMITER);

	    		// Sentences - suspended
	    		fileWriter.append(Cases.get(k).isSuspended() + "");
	    		fileWriter.append(COMMA_DELIMITER);

	    		// Sentences - suspended
	    		fileWriter.append(Cases.get(k).getSuspendedText()+"");
	    		fileWriter.append(COMMA_DELIMITER);	    		
	    		// Sentences text
	    		fileWriter.append(Cases.get(k).getSentencesString());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Sentences Min
	    		fileWriter.append(Cases.get(k).getMaxSentenceText());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Sentences Max
	    		fileWriter.append(Cases.get(k).getMinSentenceText());
	    		fileWriter.append(COMMA_DELIMITER);
	    		// Sentenced-to Text
	    		fileWriter.append(Cases.get(k).getSentencedToText());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Min sentence number
	    		fileWriter.append(Cases.get(k).getSentenceMin()+"");
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Min sentence units
	    		fileWriter.append(Cases.get(k).getSentenceMax()+"");
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Max sentence number
	    		fileWriter.append(Cases.get(k).getSentenceMinUnit());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
	    		// Max sentence units 
	    		fileWriter.append(Cases.get(k).getSentenceMaxUnit());
	    		fileWriter.append(COMMA_DELIMITER);
	    		
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