import java.util.ArrayList;

import org.jsoup.nodes.Element;
public class Sentence {
	String charge			= ""; 
	String sentencedToText 	= "";
	String minSentenceText	= ""; 
	String maxSentenceText	= "";
	String sentenceTermText	= "";
	String suspendedText	= ""; 
	String sentenceAllText	= ""; 
	int incarcerated		= 0; 
	int suspended			= 0; 
//	int ndoc				= 0;
	
	
	public String getCharge(){
		return charge;
	}
	
	public String getSentencedToText(){
		return sentencedToText.replaceAll("[,]", "-");
	}

	public String getMinSentenceText(){
		return minSentenceText.replaceAll("[,]", "-");
	}
	
	public String getMaxSentenceText(){
		return maxSentenceText.replaceAll("[,]", "-");
	}
	
	public String getSentenceTermText(){
		return sentenceTermText.replaceAll("[,]", "-");
	}
	
	public String getSuspendedText(){
		return suspendedText.replaceAll("[,]", "-");
	}
	
	public String getSentenceAllText(){
		return sentenceAllText.replaceAll("[,]", "-");
	}
	
	public int isIncarcerated(){
		return incarcerated;
	}
	
	public int isSuspended(){
		return suspended;
	}

	
	public Sentence(){

	}

	public Sentence(Element aTable){
//		System.out.println("suspended: " + suspended);
		sentenceAllText = aTable.select("div > div > div > div").text();
		
		// This gets the innermost element that contains the phrase indicated
		// "Sentenced to" is usually followed by the institution. This is an indication of incarceration
		if(null!=aTable.select(":contains(Sentenced to):not(:has(:contains(Sentenced to)))").first()){
//			System.out.println("Sentenced to");
			if(suspended==0){
				incarcerated = 1;
			}
			sentencedToText = aTable.select(":contains(Sentenced to):not(:has(:contains(Sentenced to)))").first().text();
//			System.out.println("Incarcerated: " + aTable.select(":contains(Sentenced to):not(:has(:contains(Sentenced to)))").first().text());
		}
		
		// Contains the minimum length of sentence (may contain other text)
		if(null!=aTable.select(":contains(Minimum):not(:has(:contains(Minimum)))").first()){
//			System.out.println("Min");
			if(suspended==0){
				incarcerated = 1;	
			}
			
			minSentenceText = aTable.select(":contains(Minimum):not(:has(:contains(Minimum)))").first().text();
//			System.out.println("Sentence Length Minimum: " + aTable.select(":contains(Minimum):not(:has(:contains(Minimum)))").first().text());
		}
		
		// Contains maximum length of sentence (may contain other text)
		if(null!=aTable.select(":contains(Maximum):not(:has(:contains(Maximum)))").first()){
//			System.out.println("Max");
			
			if(suspended==0){
				incarcerated = 1;	
			}
			maxSentenceText = aTable.select(":contains(Maximum):not(:has(:contains(Maximum)))").first().text();

//			System.out.println("Sentence Length Maximum: " + aTable.select(":contains(Maximum):not(:has(:contains(Maximum)))").first().text());
		}
		
		// "Term" may contain the sentence length
		if(null!=aTable.select(":contains(Term):not(:has(:contains(Term)))").first()){
//			System.out.println("Term");
			if(suspended==0){
				incarcerated = 1;	
			}
			sentenceTermText = aTable.select(":contains(Term):not(:has(:contains(Term)))").first().text();
			 
//			System.out.println("Sentence Length: " + aTable.select(":contains(Term):not(:has(:contains(Term)))").first().text());
		}
		
		// Indication of suspended sentence
		if(null!=aTable.select(":contains(Suspended):not(:has(:contains(Suspended)))").first()){
//			System.out.println("SHOULD BE SUSPENDED");
			suspendedText = aTable.select(":contains(Suspended):not(:has(:contains(Suspended)))").first().text();
			suspended = 1; 
			incarcerated = 0; 
//			System.out.println("Suspended: " + aTable.select(":contains(Suspended):not(:has(:contains(Suspended)))").first().text());
		}
//		System.out.println("\n\n\n");

		
		
	}

}
