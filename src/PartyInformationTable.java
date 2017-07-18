import java.util.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PartyInformationTable {

		String defendant 	= ""; 
		String defenseatt 	= "";
		String plaintiff 	= ""; 
		String prosecatt	= "";

		
		public String getDefendantName(){
			return defendant.replaceAll("[,]", ";");
		}
		public String getPlaintiffName(){
			return plaintiff.replaceAll("[,]", ";");
		}
		public String getDefenseAttorney(){
			return defenseatt.replaceAll("[,]", ";");
		}
		public String getProsecutingAttorney(){
			return prosecatt.replaceAll("[,]", ";");
		}

		
		public PartyInformationTable(){
			defendant	= "NULL";
			defenseatt	= "NULL";
			plaintiff	= "NULL";
			prosecatt	= "NULL";
			
		}
		
		public PartyInformationTable(Element aTable){
			// Check that the table is the right one
			String caption= aTable.select("caption").text();
			assert caption == "Party Information";
			
			Elements rows = aTable.select("tr");
			
			for(Element tr : rows){

				for (int i = 0; i < tr.childNodeSize(); i++) {
					if (tr.child(i).text().equals("Defendant")) {
						defendant = tr.child(i + 1).text();
						defenseatt = tr.child(tr.childNodeSize() - 1).text();
					}
					else if (tr.child(i).text().equals("Plaintiff")) {
						plaintiff = tr.child(i + 1).text();
						prosecatt = tr.child(tr.childNodeSize() - 1).text();
					}
				}
			}
		}


}