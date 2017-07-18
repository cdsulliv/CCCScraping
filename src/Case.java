import java.util.ArrayList;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.*;

public class Case {
	String crossRefNumber 				= "";
	String originalCaseNumber           = "";
    RelatedCaseTable rct 				= new RelatedCaseTable(); 
    PartyInformationTable pit 			= new PartyInformationTable();
    ChargeInformationTable chargeTable 	= new ChargeInformationTable();
    EventsTable eventTable 				= new EventsTable(); 
    int caseFound; 
    
    public String getCrossRefNumber() {
    	return crossRefNumber; 
    }
    
    public RelatedCaseTable getRelatedCaseTable() {
    	return rct; 
    }
    
    public PartyInformationTable getPartyInformationTable() {
    	return pit;
    }
    
    public ChargeInformationTable getChargeInformationTable() {
    	return chargeTable; 
    }
    
    public EventsTable getEventsTable() {
    	return eventTable;
    }
    
    
    // Get information directly from Events Information Table
	public String getDispositionsString() {
		return getEventsTable().getDispositionsString(); 
	}
	
	public String getSentencesString() {
		return getEventsTable().getSentencesString();
	}
	
	public String getEventsString() {
		return getEventsTable().getEventsString();
	}
	
	public TreeMap<String, Sentence> getSentences() {
		return getEventsTable().getSentences();
	}
	
	public int isIncarcerated() {
		int inc = 0; 
		for(String k : getEventsTable().getSentences().keySet()) {
			inc = getEventsTable().getSentences().get(k).isIncarcerated();
		}
		
		return inc;
	}
	
	public int isSuspended() {
		int susp = 0; 
		for(String k : getEventsTable().getSentences().keySet()) {
			susp = getEventsTable().getSentences().get(k).isSuspended();
		}
		
		return susp;
	}
	
	public String getSuspendedText() {
		String susp = ""; 
		for(String k : getEventsTable().getSentences().keySet()) {
			susp = susp + getEventsTable().getSentences().get(k).getSuspendedText();
		}
		
		return susp;
	}
	
	public String getMaxSentenceText() {
		String s = ""; 
		for(String k : getEventsTable().getSentences().keySet()) {
			s = getEventsTable().getSentences().get(k).getMaxSentenceText(); 
		}
		
		return s;
	}
	
	public String getMinSentenceText() {
		String s = ""; 
		for(String k : getEventsTable().getSentences().keySet()) {
			s = getEventsTable().getSentences().get(k).getMinSentenceText(); 
		}
		
		return s;
	}
	
	public String getSentencedToText() {
		String s = ""; 
		for(String k : getEventsTable().getSentences().keySet()) {
			s = s + getEventsTable().getSentences().get(k).getSentencedToText(); 
		}
		
		return s;
		
	}
	
	public int getSentenceMin() {
		int min = -1; 
		for(String k : getEventsTable().getSentences().keySet()) {
			System.out.println("Getting case-level min: " + getEventsTable().getSentences().get(k).getMin());
			min = getEventsTable().getSentences().get(k).getMin(); 
		}
		
		return min;	
	}
	
	public int getSentenceMax() {
		int max = -1; 
		for(String k : getEventsTable().getSentences().keySet()) {
			max = getEventsTable().getSentences().get(k).getMax(); 
		}
		
		return max;	
	}
	
	public String getSentenceMinUnit() {
		String minUnit = ""; 
		for(String k : getEventsTable().getSentences().keySet()) {
			minUnit = minUnit + getEventsTable().getSentences().get(k).getMinUnit(); 
		}
		
		return minUnit;	
	}
	
	public String getSentenceMaxUnit() {
		String maxUnit = ""; 
		for(String k : getEventsTable().getSentences().keySet()) {
			maxUnit = maxUnit + getEventsTable().getSentences().get(k).getMaxUnit(); 
		}
		
		return maxUnit;	
	}
	
	public String getJudOfficerFinal() {
		return eventTable.getJudOfficer();
	}
    
	
    // Get information directly from Party Information Table
	public String getDefendantName() {
		return getPartyInformationTable().getDefendantName();
	}
	
	public String getPlaintiffName() {
		return getPartyInformationTable().getPlaintiffName();
	}
	
	public String getDefenseAttorney() {
		return getPartyInformationTable().getDefenseAttorney(); 
	}
	
	public String getProsecutingAttorney() {
		return getPartyInformationTable().getProsecutingAttorney();
	}
	
	
	// Get information directly from Charge Information Table
	public ArrayList<String> getCharges() {
		return getChargeInformationTable().getCharges();
	}
	
	public ArrayList<String> getStatutes() {
		return getChargeInformationTable().getStatutes();
	}
	
	public ArrayList<String> getChargeLevels() {
		return getChargeInformationTable().getLevels();
	}
	
	public ArrayList<String> getDates() {
		return getChargeInformationTable().getDates();
	}
	
	public String getChargesString() {
		return getChargeInformationTable().getChargesString();
	}
	
	public String getStatutesString() {
		return getChargeInformationTable().getStatutesString();
	}
	
	public String getChargeLevelsString() {
		return getChargeInformationTable().getLevelsString();
	}
	
	public String getDatesString() {
		return getChargeInformationTable().getDatesString(); 
	}
	
	public int getCaseFound() {
		return caseFound;
	}
	
	public String getOriginalCaseNumber() {
		return originalCaseNumber;
	}
	
    
	// constructor to decide type of table and create new instance accordingly
	public Case(Document page, String originalCaseNumber) {
		this.originalCaseNumber = originalCaseNumber;
		if(!page.text().equals("Sorry, case not found!")) {
			caseFound = 1;
			crossRefNumber = page.select(".ssCaseDetailCaseNbr").text();
		    Elements tables = page.select("body > table");
		    String tableType = ""; 
		    
		    for(Element t : tables) {
		    	tableType = t.select("caption").text();
		    	if(tableType.equals("Related Case Information")) {
		    		rct = new RelatedCaseTable(t);

	    	   } else if (tableType.equals("Party Information")) {
	    		   pit = new PartyInformationTable(t);

	    	   } else if (tableType.equals("Charge Information")) {
	    	       chargeTable = new ChargeInformationTable(t);

	    	   } else if (tableType.equals("Events & Orders of the Court")) {
	    	       eventTable = new EventsTable(t);

	    	   } else if (tableType.equals("Financial Information")) {
	    		   // leaving financial information for now
	    	   } else {

	    	   }
	       }
	       
		} else { 
			caseFound = 0;
			System.out.println("No case here!");
		}   
	}
}