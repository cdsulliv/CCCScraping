	import org.jsoup.Jsoup;
	import org.jsoup.nodes.Document;
	import org.jsoup.nodes.Element;
	import org.jsoup.select.Elements;
	import java.io.File;
	import java.io.IOException;
	import java.io.FileWriter;


public class Sandbox {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "caseid,relatedcases,charges,disposition,sentence";


    
    
	public static void main(String[] args) throws Exception {
       final String dir = System.getProperty("user.dir");
       System.out.println("current dir = " + dir);
	   
	   
       Document document = Jsoup.parse(new File("testpage.html"), "utf-8"); 
       
       Elements tbodies = document.select("tbody");
       System.out.println("Number of tbodies: " + tbodies.size());

       System.out.println("\n*** TABLES ***\n");
       Elements tables = document.select("body > table");
       System.out.println("Number of tables: " + tables.size());
       
       
       
       
       // Loop over the tables in the page and construct the appropriate type
       RelatedCaseTable rct = null; 
       PartyInformationTable pit = null;
       ChargeInformationTable chargeTable = null;
       EventsTable eventTable = null; 
       
       String tableType = ""; 
       for(Element t : tables){
    	   tableType = t.select("caption").text();
    	   System.out.println(tableType);
    	   if(tableType.equals("Related Case Information")){
    		   rct = new RelatedCaseTable(t);
    		   System.out.println("Related Case table created.");
    	   } else if (tableType.equals("Party Information")){
    		   pit = new PartyInformationTable(t);
    		   System.out.println("Party Info table created.");
    	   } else if (tableType.equals("Charge Information")){
    	       chargeTable = new ChargeInformationTable(t);
    	       System.out.println("Charge table created.");
    	       System.out.println(chargeTable.getCharges());
    	       System.out.println(chargeTable.getStatutes());
    	   } else if (tableType.equals("Events & Orders of the Court")){
    	       eventTable = new EventsTable(t);
    	       System.out.println("\nEvents table created.\n");
    	       System.out.println(eventTable.getDispositions());
    	       System.out.println(eventTable.getSentences()); 
    	   } else if (tableType.equals("Financial Information")){
//	    	       FinancialInformationTable financialTable = new FinancialInformationTable(t);
    		   System.out.println("Financial Information table created.");
    	   } else {
    		   System.out.println("Table does not match identified type; no table constructed in data");
    	   }
       }
       
       String fileName = "oneCaseOutput.csv"; 
       FileWriter fileWriter = null;
       try {
    	   fileWriter = new FileWriter(fileName);
        
           //Write the CSV file header
                   fileWriter.append(FILE_HEADER.toString());

                   //Add a new line separator after the header
                   fileWriter.append(NEW_LINE_SEPARATOR);

                   //Write a new student object list to the CSV file
                   fileWriter.append("1");
                   fileWriter.append(COMMA_DELIMITER);
                   fileWriter.append(rct.getRelatedCasesString());
                   fileWriter.append(COMMA_DELIMITER);
                   fileWriter.append(chargeTable.getChargesString());
                   fileWriter.append(COMMA_DELIMITER);
                   fileWriter.append(eventTable.getDispositionsString());
                   fileWriter.append(COMMA_DELIMITER);
                   fileWriter.append(eventTable.getSentencesString());
                   fileWriter.append(NEW_LINE_SEPARATOR);

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
