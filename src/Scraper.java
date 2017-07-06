import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;


public class Scraper {
	
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
       String tableType = ""; 
       for(Element t : tables){
    	   tableType = t.select("caption").text();
    	   System.out.println(tableType);
    	   if(tableType.equals("Related Case Information")){
    		   RelatedCaseTable rct = new RelatedCaseTable(t);
    		   System.out.println("Related Case table created.");
    	   } else if (tableType.equals("Party Information")){
    		   PartyInformationTable pit = new PartyInformationTable(t);
    		   System.out.println("Party Info table created.");
    	   } else if (tableType.equals("Charge Information")){
    	       ChargeInformationTable chargeTable = new ChargeInformationTable(t);
    	       System.out.println("Charge table created.");
    	       System.out.println(chargeTable.getCharges());
    	       System.out.println(chargeTable.getStatutes());
    	   } else if (tableType.equals("Events & Orders of the Court")){
    	       EventsTable eventTable = new EventsTable(t);
    	       System.out.println("Events table created.");
    	   } else if (tableType.equals("Financial Information")){
//    	       FinancialInformationTable financialTable = new FinancialInformationTable(t);
    		   System.out.println("Financial Information table created.");
    	   } else {
    		   System.out.println("Table does not match identified type; no table constructed in data");
    	   }
       }
       
       
       
       
    }
}
