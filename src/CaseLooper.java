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
    		+ "minimum,maximum,sentencedtotext,minNo, minUnit, maxNo, maxUnit, judOfficer";
	private static TreeMap<Integer, String> refinedCaseNumbers;
	private static SeleniumCCC seleniumInstance;

	public static void main(String[] args) throws Exception {
		refinedCaseNumbers = new TreeMap<Integer, String>();
		CaseInput testInput = new CaseInput();
		testInput.open();
		testInput.read();
		testInput.close();
		
		// code to refine all the case numbers that have been read in to caseNumbers
		CaseNumberRefiner refiner = new CaseNumberRefiner();
		for (int i = 0; i < testInput.caseNumbers.size(); i++) {
			refinedCaseNumbers.put(i, refiner.Refiner(testInput.caseNumbers.get(i)));
		}
		
	    // TreeMap will hold the filename and a Case object
		TreeMap<String,Case> Cases = new TreeMap<String,Case>();

		// stores current directory 
		final String dir = System.getProperty("user.dir");
	    System.out.println("current dir = " + dir);
	    String fileName = "CaseOutput2.csv"; 
	    FileWriter fileWriter = null;
    	fileWriter = new FileWriter(fileName);
        
        //Write the CSV file header
    	fileWriter.append(FILE_HEADER.toString());

    	//Add a new line separator after the header and flush so that it writes to the csv file
    	fileWriter.append(NEW_LINE_SEPARATOR);
    	fileWriter.flush();
	   
    	// sets up Selenium 
		seleniumInstance = new SeleniumCCC();
		
		String page = "";
		Case currcase;
		
		// loop to search each case and write to csv file on the go 
	    for(int i = 0; i < refinedCaseNumbers.size(); i++) {
	    	// 1) Get searchable string - DONE
	    	String currentCaseNumber = refinedCaseNumbers.get(i);
	    	
	    	// 2) Launch Selenium browser, navigate to page and get page source - DONE
	    	if (i % 300 == 0) {
	    		seleniumInstance.setUp();
				page = seleniumInstance.testFirstClark(currentCaseNumber, 0);
	    	}
	    	else {
	    		page = seleniumInstance.testFirstClark(currentCaseNumber, 1);
	    	}

	    	// 3) Create new case and add it to treemap of cases - DONE
	    	Cases.put(currentCaseNumber, new Case(Jsoup.parse(page), testInput.caseNumbers.get(i)));
	    	
	    	// re-do a case if it was searchable but variables were not assigned properly 
	    	if (Cases.get(currentCaseNumber).getDefendantName().equals("NULL") & Cases.get(currentCaseNumber).getCaseFound() == 1) {
	    		System.out.println("CASE WAS INITIALLY NULL; RERUNNING");
	    		page = seleniumInstance.testFirstClark(currentCaseNumber, 1);
		    	Cases.put(currentCaseNumber, new Case(Jsoup.parse(page), testInput.caseNumbers.get(i)));
	    	}

	
	    	// Create new object and write to the CSV file

	    	currcase = Cases.get(refinedCaseNumbers.get(i));
	    	System.out.println(currcase.getCrossRefNumber());   	
	    	
	    	// Original case id
	    	fileWriter.append(currcase.getOriginalCaseNumber()); 
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Scraped case id
	    	fileWriter.append(currcase.getCrossRefNumber()); 
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Case Found
	    	fileWriter.append(currcase.getCaseFound() + ""); 
	    	fileWriter.append(COMMA_DELIMITER);

	    	// Defendant Name
	    	fileWriter.append(currcase.getDefendantName());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Defense Attorney
	    	fileWriter.append(currcase.getDefenseAttorney());
	    	fileWriter.append(COMMA_DELIMITER);

	    	// Prosecutor
	    	fileWriter.append(currcase.getProsecutingAttorney());
	    	fileWriter.append(COMMA_DELIMITER);

	    	// Related Cases
	    	fileWriter.append(currcase.getRelatedCaseTable().getRelatedCasesString());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Number of charges
	    	fileWriter.append(currcase.getCharges().size() + "");
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// List of charges
	    	fileWriter.append(currcase.getChargesString());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// List of charge level
	    	fileWriter.append(currcase.getChargeLevelsString());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Dispositions
	    	fileWriter.append(currcase.getDispositionsString());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Sentences - incarcerated
	    	fileWriter.append(currcase.isIncarcerated() + "");
	    	fileWriter.append(COMMA_DELIMITER);

	    	// Sentences - isSuspended
	    	fileWriter.append(currcase.isSuspended() + "");
	    	fileWriter.append(COMMA_DELIMITER);

	    	// Sentences - suspended
	    	fileWriter.append(currcase.getSuspendedText()+"");
	    	fileWriter.append(COMMA_DELIMITER);	    		
	    		
	    	// Sentences text
	    	fileWriter.append(currcase.getSentencesString());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Sentences Min
	    	fileWriter.append(currcase.getMaxSentenceText());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Sentences Max
	    	fileWriter.append(currcase.getMinSentenceText());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Sentenced-to Text
	    	fileWriter.append(currcase.getSentencedToText());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Min sentence number
	    	fileWriter.append(currcase.getSentenceMin()+"");
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Min sentence units
	    	fileWriter.append(currcase.getSentenceMax()+"");
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Max sentence number
	    	fileWriter.append(currcase.getSentenceMinUnit());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Max sentence units 
	    	fileWriter.append(currcase.getSentenceMaxUnit());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	// Judicial Officer name
	    	fileWriter.append(currcase.getJudOfficerFinal());
	    	fileWriter.append(COMMA_DELIMITER);
	    		
	    	fileWriter.append(NEW_LINE_SEPARATOR);
	    	fileWriter.flush();
	    	}
	    
	        fileWriter.close();
	    	System.out.println("CSV file was created successfully !!!");
	    } 
}