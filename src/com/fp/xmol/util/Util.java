package com.fp.xmol.util;

import org.apache.commons.lang.RandomStringUtils;
import org.zkoss.chart.Charts;

import java.text.DateFormatSymbols;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static Random random = new Random();
    public static String[] MONTHS = new DateFormatSymbols().getMonths();
    public static String[] ALPHABETS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static boolean emailValidator(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

    public static int nextInt(int low, int high){
        return random.nextInt(high-low) + low;
    }

    public static void setupColor(Charts chart){
        int n = chart.getSeriesSize();
        BsColor[] colors = BsColor.values();
        for (int i = 0 ; i < n ; i++){
            chart.getSeries(i).setColor(colors[i % colors.length].getHexCode());
        }
    }
    
    public static String keyGenerator() {
    	int length = 5;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);       
    }
    
    public static long countOccurrencesOf(final String str, final char character) {
        return str.codePoints().filter(ch -> ch == character).count();
    }
}
