package cn.mageek.date;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 根据本地的国家，判断用什么样的日期
 */
public class CNLocalDateFormatter implements Formatter<LocalDate> {

    private static final String CN_PATTERN = "yyyy-MM-dd";

    private static final String NORMAL_PATTERN = "dd/MM/yyyy";


    public static String getPattern(Locale locale) {
        return isChina(locale) ? CN_PATTERN : NORMAL_PATTERN;
    }

    private static boolean isChina(Locale locale) {
//        return Locale.US.getCountry().equals(locale.getCountry());
//        return Locale.CHINA.getCountry().equals(locale.getCountry());
        System.out.println(Locale.CHINA.toString()+"::"+locale.toString());//无lang=cn zh_CN::zh_CN， 有的话就是 zh_CN::cn
        return Locale.CHINA.toString().toUpperCase().contains(locale.toString().toUpperCase());

    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(getPattern(locale)));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern(getPattern(locale)).format(object);
    }
}