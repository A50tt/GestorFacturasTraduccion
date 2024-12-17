
package jud.gestorfacturas.manager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    
    public Utils() {
        
    }
    
    public static String formatDecimalNumberIfNecessary(Double dou, int decimalDigits) {
        return Utils.formatDecimalNumber(dou, decimalDigits, "#");
    }

    public static String formatDecimalNumberAlways(Double dou, int decimalDigits) {
        return Utils.formatDecimalNumber(dou, decimalDigits, "0");
    }
    
    public static String formatDecimalNumber(Double dou, int decimalDigits, String optionalDecimal) {
        String digits = "";
        float num = Math.round(dou * 100) / 100;
        if (decimalDigits < 0) {
            return null;
        } else if (decimalDigits == 0) {
            String str = String.valueOf(num);
            return str.substring(0, str.indexOf("."));
        }
        for (int i = 1; i <= decimalDigits; i++) {
            digits += optionalDecimal;
        }
        return new DecimalFormat("0." + digits).format(num);
    }
}
