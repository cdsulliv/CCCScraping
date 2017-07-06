	import java.util.*;
	import org.jsoup.nodes.Element;
	import org.jsoup.select.Elements;

public class PartyInformationTable {

		String defendant 	= ""; 
		String plaintiff 	= ""; 
		String defenseatt 	= "";
		String prosecatt	= "";

		
		public String getDefendantName(){
			return defendant;
		}
		public String getPlaintiffName(){
			return plaintiff;
		}
		public String getDefenseAttorney(){
			return defenseatt;
		}
		public String getProsecutingAttorney(){
			return prosecatt;
		}

		
		public PartyInformationTable(Element aTable){
			// Check that the table is the right one
			String caption= aTable.select("caption").text();
			assert caption == "Party Information";
			
			Elements rows = aTable.select("tr");
			
			for(Element tr : rows){
				System.out.println(tr.text());
			}
		}


}
