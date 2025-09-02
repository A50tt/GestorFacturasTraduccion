
package utils;

import java.math.RoundingMode;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormatUtils {

    public FormatUtils() {}
    
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
    
    public static String formatDecimalNumberToDoubleIfNecessary(Double dou, int decimalDigits) {
        try {
            if (dou % 1 != 0 || Boolean.parseBoolean(ConfigUtils.loadProperty("factura.forzar_decimales_cantidad"))) { // Si tiene decimales o si en la configuración global siempre se quieren decimales 
                return convertToLocaleThousandSeparatorAndDecimal(dou, 2);
            }
            return convertToLocaleThousandSeparatorAndDecimal(dou, 0);
        } catch (Exception ex) {
            FrameUtils.showErrorMessage("Error", ex.getMessage(), ex);
        }
        return null;
    }
    
    public static java.sql.Date convertStringToDate(String str) {
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
    
    public static String convertToLocaleThousandSeparatorAndDecimal(Double number, int decimalPositions) {
        Locale l = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getNumberInstance(l);
        formatter.setMinimumFractionDigits(decimalPositions);
        formatter.setMaximumFractionDigits(decimalPositions);
        return formatter.format(number);
    }

    public static String convertToEngDecimalSystemString(Double number) {
        String numberStr = String.valueOf(number);
        return FormatUtils.convertToEngDecimalSystemString(numberStr);
    }
    
    public static String convertToEngDecimalSystemString(String number) {
        String numberStr = number;
        String numberWithoutCommas = numberStr.replace(",", "#");
        String numberWithoutDotsAndCommas = numberWithoutCommas.replace(".", ",");
        String resultNumber = numberWithoutDotsAndCommas.replace("#", ".");
        return resultNumber;
    }
}
