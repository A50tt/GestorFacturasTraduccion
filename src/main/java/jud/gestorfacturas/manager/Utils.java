
package jud.gestorfacturas.manager;

import java.math.RoundingMode;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Utils {

    public Utils() {

    }

    public static String formatDecimalNumberToStringIfNecessary(Double dou, int decimalDigits) {
        try {
            return Utils.formatDecimalNumber(dou, decimalDigits, "#");
        } catch (CustomException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Double checkIfDecimalAndReturnDotDouble(String number) {
        if (number.contains(",") || number.contains(".")) {
            String newNumber = "";
            for (char ch : number.toCharArray()) {
                try {
                    if (ch == ',' || ch == '.') {
                        newNumber += ".";
                    } else if (Character.isDigit(ch)) {
                        newNumber += ch;
                    } else {
                        return null;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return Double.valueOf(newNumber);
        } else {
            return Double.valueOf(number);
        }
    }
    
    public static boolean isParseableToDouble (String num) {
        try {
            Double.parseDouble(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static Double formatDecimalNumberToDoubleIfNecessary(Double dou, int decimalDigits) {
        try {
            return Double.parseDouble(Utils.formatDecimalNumber(dou, decimalDigits, "#").replace(",","."));
        } catch (CustomException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String formatDecimalNumberToStringAlways(Double dou, int decimalDigits) {
        try {
            return Utils.formatDecimalNumber(dou, decimalDigits, "0");
        } catch (CustomException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static String formatDecimalNumber(Double dou, int decimalDigits, String optionalDecimal) throws CustomException {
        DecimalFormat df;
        if (decimalDigits > 0) {
            String digits = "";
            for (int i = 0; i < decimalDigits; i++) {
                digits += optionalDecimal;
            }
            //We force to do the rounding only with the last decimal that we indicate in optionalDecimal
            //If this is missing, the rounding will be done with the entire number, each decimal.
            DecimalFormat dfAux = new DecimalFormat("0." + digits + optionalDecimal);
            dou = Double.parseDouble(dfAux.format(dou).replace(",", "."));
            df = new DecimalFormat("0." + digits);
            df.setRoundingMode(RoundingMode.HALF_UP);
        } else if (decimalDigits == 0) {
            df = new DecimalFormat("#");
        } else {
            throw new CustomException("Decimals indicated to format < 0");
        }
        return df.format(dou);
    }
    
    public java.sql.Date convertStringToDate(String str) {
        DateTimeFormatter formatter;
        try {
            if (Pattern.matches("\\d{2}-\\d{2}-\\d{4}", str)) { // 01-12-2024
                formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            } else if (Pattern.matches("\\d{2}/\\d{2}/\\d{4}", str)) { // 01/12/2024
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            } else if (Pattern.matches("\\d{2}-\\d{2}-\\d{2}", str)) { // 01-12-24
                formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
            } else if (Pattern.matches("\\d{2}/\\d{2}/\\d{2}", str)) { // 01/12/24
                formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            } else if (Pattern.matches("\\d{8}", str)) { // 01122024
                formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            } else if (Pattern.matches("\\d{6}", str)) { // 011224
                formatter = DateTimeFormatter.ofPattern("ddMMyy");
            } else {
                return null;
            }
            LocalDate dateTime = LocalDate.parse(str, formatter);
            //return new Date(dateTime.atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
            return Date.valueOf(dateTime);
        } catch (DateTimeException e) {
            return null;
        }
    }
    
    public static String convertDateToString(Date date, String format) {
        String pattern = format;
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
