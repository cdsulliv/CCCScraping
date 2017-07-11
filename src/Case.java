import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.*;

public class Case {
	String crossRefNumber 				="";
    RelatedCaseTable rct 				= new RelatedCaseTable(); 
    PartyInformationTable pit 			= new PartyInformationTable();
    ChargeInformationTable chargeTable 	= new ChargeInformationTable();
    EventsTable eventTable 				= new EventsTable(); 
    
    public String getCrossRefNumber(){
    	return crossRefNumber; 
    }
    
    public RelatedCaseTable getRelatedCaseTable(){
    	return rct; 
    }
    public PartyInformationTable getPartyInformationTable(){
    	return pit;
    }
    public ChargeInformationTable getChargeInformationTable(){
    	return chargeTable; 
    }
    public EventsTable getEventsTable(){
    	return eventTable;
    }
    
    // Get information directly from Events Information Table
	public String getDispositionsString(){
		return getEventsTable().getDispositionsString(); 
	}
	public String getSentencesString(){
		return getEventsTable().getSentencesString();
	}
	public String getEventsString(){
		return getEventsTable().getEventsString();
	}
	public TreeMap<String, Sentence> getSentences(){
		return getEventsTable().getSentences();
	}
	public int isIncarcerated(){
		int inc = 0; 
		for(String k : getEventsTable().getSentences().keySet()){
			inc = getEventsTable().getSentences().get(k).isIncarcerated();
		}
		
		return inc;
	}
	public int isSuspended(){
		int susp = 0; 
		for(String k : getEventsTable().getSentences().keySet()){
			susp = getEventsTable().getSentences().get(k).isSuspended();
		}
		
		return susp;
	}
	
	public String getSuspendedText(){
		String susp = ""; 
		for(String k : getEventsTable().getSentences().keySet()){
			susp = susp + getEventsTable().getSentences().get(k).getSuspendedText();
		}
		
		return susp;
	}
	
	public String getMaxSentenceText(){
		String s = ""; 
		for(String k : getEventsTable().getSentences().keySet()){
			s = getEventsTable().getSentences().get(k).getMaxSentenceText(); 
		}
		
		return s;
	}
	
	public String getMinSentenceText(){
		String s = ""; 
		for(String k : getEventsTable().getSentences().keySet()){
			s = getEventsTable().getSentences().get(k).getMinSentenceText(); 
		}
		
		return s;
	}
	public String getSentencedToText(){
		String s = ""; 
		for(String k : getEventsTable().getSentences().keySet()){
			s = s + getEventsTable().getSentences().get(k).getSentencedToText(); 
		}
		
		return s;
		
	}
	
	public int getSentenceMin(){
		int min = -1; 
		for(String k : getEventsTable().getSentences().keySet()){
			System.out.println("Getting case-level min: " + getEventsTable().getSentences().get(k).getMin());
			min = getEventsTable().getSentences().get(k).getMin(); 
		}
		
		return min;	
	}
	
	public int getSentenceMax(){
		int max = -1; 
		for(String k : getEventsTable().getSentences().keySet()){
			max = getEventsTable().getSentences().get(k).getMax(); 
		}
		
		return max;	
	}
	
	public String getSentenceMinUnit(){
		String minUnit = ""; 
		for(String k : getEventsTable().getSentences().keySet()){
			minUnit = minUnit + getEventsTable().getSentences().get(k).getMinUnit(); 
		}
		
		return minUnit;	
	}
	
	public String getSentenceMaxUnit(){
		String maxUnit = ""; 
		for(String k : getEventsTable().getSentences().keySet()){
			maxUnit = maxUnit + getEventsTable().getSentences().get(k).getMaxUnit(); 
		}
		
		return maxUnit;	
	}
    
    // Get information directly from Party Information Table
	public String getDefendantName(){
		return getPartyInformationTable().getDefendantName();
	}
	public String getPlaintiffName(){
		return getPartyInformationTable().getPlaintiffName();
	}
	public String getDefenseAttorney(){
		return getPartyInformationTable().getDefenseAttorney(); 
	}
	public String getProsecutingAttorney(){
		return getPartyInformationTable().getProsecutingAttorney();
	}
	
	// Get information directly from Charge Information Table
	public ArrayList<String> getCharges(){
		return getChargeInformationTable().getCharges();
	}
	public ArrayList<String> getStatutes(){
		return getChargeInformationTable().getStatutes();
	}
	public ArrayList<String> getChargeLevels(){
		return getChargeInformationTable().getLevels();
	}
	public ArrayList<String> getDates(){
		return getChargeInformationTable().getDates();
	}
	public String getChargesString(){
		return getChargeInformationTable().getChargesString();
	}
	public String getStatutesString(){
		return getChargeInformationTable().getStatutesString();
	}
	public String getChargeLevelsString(){
		return getChargeInformationTable().getLevelsString();
	}
	public String getDatesString(){
		return getChargeInformationTable().getDatesString(); 
	}
	
	
    
	public Case(Document page){
		crossRefNumber = page.select(".ssCaseDetailCaseNbr").text();
		
	    Elements tables = page.select("body > table");

       String tableType = ""; 
       for(Element t : tables){
    	   tableType = t.select("caption").text();
    	   //  .println(tableType);
    	   if(tableType.equals("Related Case Information")){
    		   rct = new RelatedCaseTable(t);
   // 		   System.out.println("Related Case table created.\n");
    	   } else if (tableType.equals("Party Information")){
    		   pit = new PartyInformationTable(t);
  //  		   System.out.println("Party Info table created.\n");
    	   } else if (tableType.equals("Charge Information")){
    	       chargeTable = new ChargeInformationTable(t);
   // 	       System.out.println("Charge table created.\n");
   // 	       System.out.println(chargeTable.getCharges());
   // 	       System.out.println(chargeTable.getStatutes());
    	   } else if (tableType.equals("Events & Orders of the Court")){
    	       eventTable = new EventsTable(t);
   // 	       System.out.println("Events table created.\n");
    	   } else if (tableType.equals("Financial Information")){
//		       FinancialInformationTable financialTable = new FinancialInformationTable(t);
  //  		   System.out.println("Financial Information table created.\n");
    	   } else {
  //  		   System.out.println("Table does not match identified type; no table constructed in data");
    	   }
       }
       
       

	    
	}
}
