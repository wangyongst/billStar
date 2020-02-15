package com.tuofan.core.utils;


import java.util.Date;
import java.util.regex.Pattern;

/**
 * 
 * 身份证工具类.
 * 
 * 
 */
public class IDCardUtils {

	// 加权因字数
	private static final int[] WI = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

	// 代码
	private static final char[] CODE = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

	/**
	 * 检查身份证是否合法
	 * 
	 * @param card
	 *            身份证号码
	 * @return 是/否
	 */
	public static boolean verifi(String card) {
		if (card.length() == 15 && Pattern.matches("^\\d{15}$", card)) {
			card = card15To18(card);
		}
		if ((card != null ? card.length() : 0) == 18 && isDate(card)) {
			card = card.toUpperCase();
			if (Pattern.matches("^\\d{17}[xX]|\\d{18}$", card)) {
				char[] chars = card.toCharArray();
				int si = 0;
				for (int i = 0; i < 17; i++) {
					si += (chars[i] - '0') * WI[i];
				}
				return chars[17] == CODE[si % 11];
			}
			return false;
		}
		return false;
	}

	private static boolean isDate(String card) {
		String y = card.substring(6, 10);
		String m = card.substring(10, 12);
		String d = card.substring(12, 14);
		String date = y + "-" + m + "-" + d;
		Date formatDate = DateTimeUtils.getFormatDate(date);
		if (null == formatDate) {
			return false;
		}
		return true;
	}

	/**
	 * 身份证15位转18位
	 * 
	 * @param card15
	 *            15位身份证号码
	 * @return 18位身份证号码
	 */
	public static String card15To18(String card15) {
		try {
			if (card15.length() == 15) {
				int si = 0;
				StringBuilder card18 = new StringBuilder();
				card18.append(card15.substring(0, 6));
				card18.append("19");
				card18.append(card15.substring(6, 15));
				for (int i = 0; i < 17; i++) {
					si += (card18.charAt(i) - '0') * WI[i];
				}
				card18.append(CODE[si % 11]);
				return card18.toString();
			}
		} catch (Exception ex) {
			return null;
		}
		return card15;
	}
}
