package cc.shinbi.tsubuyaki.util;

public class StringUtil {
	private static String[][] REPLACE_ARRAY = { { "&", "&amp;" }, { "<", "&lt;" }, { ">", "&gt;" }, { "\"", "&quot;" },
			{ " ", "&nbsp;" }, { "\n", "<br>" } };

	static public String escapeHtml(String html) {
		String replaced = html;
		for (String[] element : REPLACE_ARRAY) {
			replaced = replaced.replace(element[0], element[1]);
		}
		return replaced;
	}

	static public String unescapeHtml(String html) {
		String replaced = html;
		for (String[] element : REPLACE_ARRAY) {
			replaced = replaced.replace(element[1], element[0]);
		}
		return replaced;
	}
}
