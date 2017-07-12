import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaseNumberRefiner {
//	static CaseInput testInput = new CaseInput();
//	static String[] caseNumbers = new String[100];

	public CaseNumberRefiner() {
		// TODO Auto-generated constructor stub
	}
	
	public static String Refiner(String input) {
		if (input.contains("C") && !input.endsWith("C")) {
			Pattern p = Pattern.compile("(\\d{6})");
			Matcher m = p.matcher(input);
			if (m.find()) {
				input = "C" + m.group(0);
				return input;
			}
			else {
				return "";
			}
		}
		else {
			input = input.substring(0, input.length() - 1);
			return input;
		}
	}

//	public static void main(String[] args) {
//		testInput.open();
//		testInput.read();
//		testInput.initializeArray(caseNumbers);
//		testInput.close();
//		for (int i = 0; i < 100; i++) {
//			System.out.println(caseNumbers[i] +", NEW: " + Refiner(caseNumbers[i]));
//		}
	
////		Refiner("C-15-155264-1");
////		Refiner("07C231117");
////		Refiner("C240118B");
////		Refiner("C-10-269880-1");
////		Refiner("13FN0053X");
////		Refiner("09F42581X");
//	}

}