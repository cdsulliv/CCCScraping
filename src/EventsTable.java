import java.util.*;
import java.io.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
	
public class EventsTable {
	
	// TreeMaps indexed by charge
	TreeMap<String, String> dispositions 	= new TreeMap<String, String>();
	TreeMap<String, String> sentences	 	= new TreeMap<String, String>();
	ArrayList<String> events 				= new ArrayList<String>(Arrays.asList(""));
	
	public TreeMap<String, String> getDispositions(){
		return dispositions;
	}
	public TreeMap<String, String> getSentences(){
		return sentences;
	}
	public ArrayList<String> getEvents(){
		return events;
	}
	
	public String getDispositionsString(){
		String mapString = ""; 
		for (String k : dispositions.keySet()) {
			mapString += k + ": " + dispositions.get(k) + "; ";
		}
		return mapString;
	}
	public String getSentencesString(){
		String mapString = ""; 
		for (String k : sentences.keySet()) {
			mapString += k + ": " + sentences.get(k) + "; ";
		}
		return mapString;
	}
	public String getEventsString(){
		String listString = ""; 
		for (String s : events)
		{
		    listString += s + "; ";
		}
		return listString; 		
	}
	
	
	public static String getEventType(Element atd){
		String eventType = ""; 
		if(atd.attr("class").equals("ssEventsAndOrdersSubTitle")){
			eventType = "title";
		}
		
		Elements divs = atd.select("div > div");
		if(divs.size()==4){ 
			eventType = "dispo"; 
		} else if(divs.size()==3){ 
			eventType = "sentence"; 
		} else if(divs.size()==0 & eventType==""){ 
			eventType = "hearings"; 
		}
		

		return eventType; 
	}
	
	
	public static void parseEventDetails(Element atd){

		
		
	}
			
	
	public EventsTable(Element aTable){
		// Check that the table is the right one
		String caption= aTable.select("caption").text();
		assert caption == "Events & Orders of the Court";
	
		List<String> tableHeaders = Arrays.asList("date", "blank1", "blank2", "eventdetails");
		
		// eventInfo will hold the information from each line of charge data
		TreeMap<String, String> eventInfo 		= null;
		TreeMap<String, String> eventDetails 	= null; 
		Element eventInfoHTML					= null;	
		String charge 	= ""; 
		String dispo 	= "";
		
		// Loop through the rows of the table and create a new event for each line
		Elements rows = aTable.select("tr");
		for(Element row : rows){
			eventInfo 		= new TreeMap<String, String>();
			eventDetails	= new TreeMap<String, String>();
//			eventInfoHTML	= new Element;
			
			Elements cells = row.children();

			if(cells.size()==tableHeaders.size()){
				for (String col : tableHeaders){
					eventInfo.put(col, cells.get(tableHeaders.indexOf(col)).text());
				}

				eventInfoHTML = cells.get(tableHeaders.indexOf("eventdetails"));
			
				// Get event entry type and parse accordingly
				String eventType = getEventType(eventInfoHTML);
				
				if(eventType.equals("dispo")){
					dispositions.put(eventInfoHTML.select("div > div > div").first().ownText(), 
									eventInfoHTML.select("div > div > div > div").first().ownText());
				} else if(eventType=="sentence"){
					sentences.put(eventInfoHTML.select("div > div > div").first().ownText(),
								eventInfoHTML.select("div > div > div > div").first().text());
				} else if(eventType=="hearings") {
					events.add(eventInfoHTML.text());
					
				}
			//charge = cells.get(tableHeaders.indexOf("eventdetails")).select("div");
				
				
//				Elements divs = cells.get(tableHeaders.indexOf("eventdetails")).select("div>div");

				
//				List<String> eventHeaders = Arrays.asList("blank", "charge", "dispo");
				
//				System.out.println(divs.size());
//				if(divs.size()>=eventHeaders.size()){
//					
//					for (String ecol : eventHeaders){
//						eventDetails.put(ecol, divs.get(eventHeaders.indexOf(ecol)).ownText());
//						System.out.println("Adding " + ecol + ": " + divs.get(eventHeaders.indexOf(ecol)).ownText()); 
//					}
//					System.out.println(eventDetails.get("charge"));
//					System.out.println(eventDetails.get("dispo"));
//					dispositions.put(eventDetails.get("charge"), eventDetails.get("dispo"));

			}
			
			
//				int i = 0; 
//				for(Element div : divs){
//					charge = div.ownText();
//					System.out.println(i++);
//					System.out.println(div.ownText());	
//				}
				
				
		}
		
//		for(String k: eventInfo.keySet()){
//			eventInfo
//		}
			// Add charge information to the appropriate TreeMaps 
//			dispositions.put(chargeInfo.get("chargename"));
//		}
//		for(String k : dispositions.keySet()){
//			System.out.println(k + ": " + dispositions.get(k));
//		}
	}
	
}
