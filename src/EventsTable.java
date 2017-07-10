import java.util.*;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
	
public class EventsTable {
	
	// TreeMaps indexed by charge
	TreeMap<String, String> dispositions 	= new TreeMap<String, String>();
	TreeMap<String, Sentence> sentences	 	= new TreeMap<String, Sentence>();
	ArrayList<String> events 				= new ArrayList<String>(Arrays.asList(""));
	
	public TreeMap<String, String> getDispositions(){
		return dispositions;
	}
	public TreeMap<String, Sentence> getSentences(){
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
		return mapString.replaceAll("[,]", ";");
	}
	
	// Get all the text from the sentence element
	public String getSentencesString(){
		String mapString = ""; 
		for (String k : sentences.keySet()) {
			mapString += k + ": " + sentences.get(k).getSentenceAllText() + "; ";
		}
		return mapString.replaceAll("[,]", ";");
	}
	
	// Get the string of hearings information
	public String getEventsString(){
		String listString = ""; 
		for (String s : events)
		{
		    listString += s + "; ";
		}
		return listString.replaceAll("[,]", ";"); 		
	}
	
	
	public static String getEventType(Element atd){
		String eventType = ""; 
		if(atd.attr("class").equals("ssEventsAndOrdersSubTitle")){
			eventType = "title";
		}
		
		Elements divs = atd.select("div > div");
		
		if(divs.size()==4 | divs.text().contains("Guilty") | divs.text().contains("Charges Amended") | divs.text().contains("Dismissed")){ 
			eventType = "dispo"; 
		} else if(divs.size()==3){ 
			eventType = "sentence"; 
		} else if(divs.size()==0 & eventType==""){ 
			eventType = "hearings"; 
		}
		
		
		
		return eventType; 
	}
	
	public EventsTable(){
		dispositions.put("NULL", "NULL");
		sentences.put("NULL", new Sentence());
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
					
					// Select the divs with the disposition information
					Elements dispoDivs = eventInfoHTML.select("div > div > div");
					String chargeName = dispoDivs.first().ownText();
					int ndispos= dispoDivs.size()/2;
				
					// Split the html at each div to separate different charges within a div
					String text = dispoDivs.first().html();
					String[] textSplitResult = text.split("<div.*>");	
					
//					Element[] elArray = new Element[textSplitResult.length];
//					String[] StrArray = new String[textSplitResult.length];
					
					for(int whichdispo = 0; whichdispo<ndispos; whichdispo++){	
						dispositions.put(Jsoup.parse(textSplitResult[whichdispo*2]).text(),
								Jsoup.parse(textSplitResult[whichdispo*2+1]).text());				
					}

				} else if(eventType=="sentence"){
				
					Sentence sentence = new Sentence(eventInfoHTML); 
					sentences.put(sentence.getCharge(), sentence); 
					
					// Make note if there is more than one sentence
					if(sentences.size() > 1){
						System.out.println("There are multiple sentences");
					}
					
				} else if(eventType=="hearings") {
					events.add(eventInfoHTML.text());
					
				}

			}
				
				
		}
	}
	
}
