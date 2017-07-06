import java.util.*;
import java.io.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
	
public class EventsTable {
	
	// TreeMaps indexed by charge
	TreeMap<String, String> dispositions 	= new TreeMap<String, String>();
	TreeMap<String, String> sentences	 	= new TreeMap<String, String>();
	String events 							= "";
	
	public TreeMap<String, String> getDispositions(){
		return dispositions;
	}
	public TreeMap<String, String> getSentences(){
		return sentences;
	}
	public String getEvents(){
		return events;
	}
	
	
	public EventsTable(Element aTable){
		// Check that the table is the right one
		String caption= aTable.select("caption").text();
		assert caption == "Events & Orders of the Court";
	
		List<String> tableHeaders = Arrays.asList("date", "blank1", "blank2", "eventdetails");
		
		// eventInfo will hold the information from each line of charge data
		TreeMap<String, String> eventInfo 		= null;
		TreeMap<String, String> eventDetails 	= null; 
		String charge 	= ""; 
		String dispo 	= "";
		
		// Loop through the rows of the table and create a new event for each line
		Elements rows = aTable.select("tr");
		for(Element row : rows){
			eventInfo 		= new TreeMap<String, String>();
			eventDetails	= new TreeMap<String, String>();

			
			Elements cells = row.children();

			if(cells.size()==tableHeaders.size()){
				for (String col : tableHeaders){
					eventInfo.put(col, cells.get(tableHeaders.indexOf(col)).text());
				}
			//charge = cells.get(tableHeaders.indexOf("eventdetails")).select("div");
				Elements divs = cells.get(tableHeaders.indexOf("eventdetails")).select("div>div");
				List<String> eventHeaders = Arrays.asList("blank", "charge", "dispo");
				
				for (String ecol : eventHeaders){
					eventDetails.put(ecol, divs.get(tableHeaders.indexOf(ecol)).ownText());
					System.out.println("Adding " + ecol + ": " + divs.get(tableHeaders.indexOf(ecol)).ownText()); 
				}
//				int i = 0; 
//				for(Element div : divs){
//					charge = div.ownText();
//					System.out.println(i++);
//					System.out.println(div.ownText());	
//				}
				
				
			}
			dispositions.put(eventDetails.get("charge"), eventInfo.get("dispo"));
			// Add charge information to the appropriate TreeMaps 
//			dispositions.put(chargeInfo.get("chargename"));
		}
		System.out.println(dispositions); 
	}
	
}
