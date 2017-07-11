import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

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
	
	private int min = -1;
	private int max = -1;
	
	private String minUnit;
	private String maxUnit;
	
	// method to split string sentence and extract min, max, isMinUnit, isMaxUnit
	public static void parseSentence(String sentence) {
		// empty for now
	}
	
	// testing method to print things
//	public static void printMinMax() {
//		System.out.println("min = " + min);
//		System.out.println("minUnit: " + minUnit);
//		System.out.println("max = " + max);
//		System.out.println("maxUnit: " + maxUnit);
//		System.out.println("");
//	}
	
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
	public String getMinUnit() {
		return minUnit;
	}
	
	public String getMaxUnit() {
		return maxUnit;
	}
	
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

	public Sentence(Element aTable) {
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
			// PARSEHERE
//			parseSentence(minSentenceText);
//			System.out.println("Min should be set: " + min);
//			System.out.println("Sentence Length Minimum: " + aTable.select(":contains(Minimum):not(:has(:contains(Minimum)))").first().text());
		}
		
		// Contains maximum length of sentence (may contain other text)
		if(null!=aTable.select(":contains(Maximum):not(:has(:contains(Maximum)))").first()){
//			System.out.println("Max");
			
			if(suspended==0){
				incarcerated = 1;	
			}
			maxSentenceText = aTable.select(":contains(Maximum):not(:has(:contains(Maximum)))").first().text();
			// PARSEHERE
//			parseSentence(maxSentenceText);
//			System.out.println("Max should be set: " + max);
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
		
		
		
		

		// isMinUnit and isMaxUnit check whether minimum and maximum have valid units (days/months/year)
		boolean isMinUnit, isMaxUnit;
		String[] splitSentence;
		String sentence = "";
		if (minSentenceText == "") {
			sentence = sentenceAllText;
		}
		else {
			sentence = minSentenceText;
		}
		splitSentence = sentence.split("\\W+");
		for (int i = 0; i < splitSentence.length; i++) {
			System.out.println(i + ". " + splitSentence[i]);
			if (splitSentence[i].compareTo("Minimum") == 0) {
				if (StringUtils.isNumeric(splitSentence[i+1])) {
					min = Integer.parseInt(splitSentence[i+1]);
					isMinUnit = splitSentence[i+2].compareTo("days") == 0 || splitSentence[i+2].compareTo("Days") == 0 || 
							splitSentence[i+2].compareTo("months") == 0 || splitSentence[i+2].compareTo("Months") == 0 || 
							splitSentence[i+2].compareTo("years") == 0 || splitSentence[i+2].compareTo("Years") == 0;
					if (isMinUnit) {
						minUnit = splitSentence[i+2];
					}
					
				}
			}
			else if (splitSentence[i].compareTo("Maximum") == 0) {
				if (StringUtils.isNumeric(splitSentence[i+1])) {
					max = Integer.parseInt(splitSentence[i+1]);
					isMaxUnit = splitSentence[i+2].compareTo("days") == 0 || splitSentence[i+2].compareTo("Days") == 0 || 
							splitSentence[i+2].compareTo("months") == 0 || splitSentence[i+2].compareTo("Months") == 0 || 
							splitSentence[i+2].compareTo("years") == 0 || splitSentence[i+2].compareTo("Years") == 0;
					if (isMaxUnit) {
						maxUnit = splitSentence[i+2];
					}	
				}
			}
			else {
				if (splitSentence[i].compareTo("Term") == 0) {
					if (StringUtils.isNumeric(splitSentence[i+1])) {
						min = Integer.parseInt(splitSentence[i+1]);
						isMinUnit = splitSentence[i+2].compareTo("days") == 0 || splitSentence[i+2].compareTo("Days") == 0 || 
								splitSentence[i+2].compareTo("months") == 0 || splitSentence[i+2].compareTo("Months") == 0 || 
								splitSentence[i+2].compareTo("years") == 0 || splitSentence[i+2].compareTo("Years") == 0;
						if (isMinUnit) {
							minUnit = splitSentence[i+2];
						}	
					}
				}	
			}
			
		}
	}
	
	
	
	public static void main(String[] args) {
//		parseSentence("Term: Minimum:19 Months- Maximum:60 Months");
//		printMinMax();
//		parseSentence("Sentence# 0001: Minimum 12 Months to Maximum 48 Months Placement: NSP");
//		printMinMax();
//		parseSentence(": Sentenced to Nevada Dept. of Corrections Term: Minimum:12 Months- Maximum:36 Months Credit for Time Served: 330 Days Comment (04/30/15 - PROBATION REVOKED); ");
//		printMinMax();
	}
	

}
