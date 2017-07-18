import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaseNumberRefiner {

	public CaseNumberRefiner() {
		
	}
	
	public static String Refiner(String input) {
		if ((input.contains("C") && !input.endsWith("C")) || (input.contains("c") && !input.endsWith("c"))) {
			Pattern p = Pattern.compile("(\\d{6})");
			Matcher m = p.matcher(input);
			if (m.find()) {
				input = "C" + m.group(0);
				return input;
			}
			else {
				return "NoCrossRef";
			}
		}
		else {
			input = input.substring(0, input.length() - 1);
			return input;
		}
	}
}