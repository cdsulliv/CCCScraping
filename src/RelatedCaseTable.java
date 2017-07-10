import java.util.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class RelatedCaseTable {
	ArrayList<String> relatedCases = new ArrayList<String>();
	
	public ArrayList<String> getRelatedCases(){
		return relatedCases;
	}
	
	public RelatedCaseTable(){
		relatedCases = new ArrayList<String>(Arrays.asList("NULL")); 
	}
	
	public RelatedCaseTable(Element aTable){
		// Check that the table is the right one
		String caption= aTable.select("caption").text();
		assert caption == "Related Case Information";
		
		Elements rows = aTable.select("tr");
		
		for(Element tr : rows){
			if(!tr.text().equals("Related Cases")){
				relatedCases.add(tr.text());
			}
		}
	}
	
	public String getRelatedCasesString(){
		String listString = "";

		for (String s : relatedCases)
		{
		    listString += s + "; ";
		}
		return listString; 

	}

}
