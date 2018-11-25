package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorsUtils {
    public static boolean checkMoneyFormat(String testString){
        Pattern p = Pattern.compile("^([1-9])+([0-9])*(\\.{0,1}[0-9]{1,2}){0,1}");
        Matcher m = p.matcher(testString);
        return m.matches();
    }
}
