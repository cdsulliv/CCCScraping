import java.util.*;
import java.io.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
	
public class ChargeInformationTable {

	ArrayList<String> charges 	= new ArrayList<String>();
	ArrayList<String> statutes 	= new ArrayList<String>(); 
	ArrayList<String> levels 	= new ArrayList<String>();
	ArrayList<String> dates		= new ArrayList<String>();

	
	public ArrayList<String> getCharges() {
		return charges;
	}
	public ArrayList<String> getStatutes() {
		return statutes;
	}
	public ArrayList<String> getLevels() {
		return levels;
	}
	public ArrayList<String> getDates() {
		return dates;
	}
	
	public String getChargesString() {
		String listString = ""; 
		for (String s : charges)
		{
		    listString += s + "; ";
		}
		return listString.replaceAll("[,]", "-"); 	
	}
	public String getStatutesString() {
		String listString = ""; 
		for (String s : statutes)
		{
		    listString += s + "; ";
		}
		return listString; 	
	}
	public String getLevelsString() {
		String listString = ""; 
		for (String s : levels)
		{
		    listString += s + "; ";
		}
		return listString.replaceAll("[,]", "-"); 	
	}
	public String getDatesString() {
		String listString = ""; 
		for (String s : dates)
		{
		    listString += s + "; ";
		}
		return listString.replaceAll("[,]", "-"); 	

	}
	
	public ChargeInformationTable() {
		ArrayList<String> charges 	= new ArrayList<String>(Arrays.asList("NULL"));
		ArrayList<String> statutes 	= new ArrayList<String>(Arrays.asList("NULL")); 
		ArrayList<String> levels 	= new ArrayList<String>(Arrays.asList("NULL"));
		ArrayList<String> dates		= new ArrayList<String>(Arrays.asList("NULL"));
	}
	
	public ChargeInformationTable(Element aTable) {
		// Check that the table is the right one
		String caption= aTable.select("caption").text();
		assert caption == "Charge Information";
		
		// chargeInfo will hold the information from each line of charge data
		TreeMap<String, String> chargeInfo = null;

		// Information to extract from the table
		List<String> headers = Arrays.asList("chargenumber", "chargename", "blank", "statute", "level", "date");

		// Loop through the rows of the table and create a new charge for each line
		Elements rows = aTable.select("tr");
		for(Element row : rows) {
			chargeInfo = new TreeMap<String, String>();
			
			Elements cells = row.select("td");

			for (String col : headers) {
				if(cells.size() == headers.size()) {
					chargeInfo.put(col, cells.get(headers.indexOf(col)).text());
				}
			}
			
			// Add charge information to the appropriate ArrayLists 
			charges.add(chargeInfo.get("chargename"));
			statutes.add(chargeInfo.get("statute"));
			levels.add(chargeInfo.get("level"));
			dates.add(chargeInfo.get("date"));
		}
		
		// Remove the nulls from the ArrayLists
		charges.removeAll(Collections.singleton(null));
		statutes.removeAll(Collections.singleton(null));
		levels.removeAll(Collections.singleton(null));
		dates.removeAll(Collections.singleton(null));
			
	}
}