package io.github.rosemoe.editor.util;

import org.apache.commons.lang3.StringUtils;

public class Parser {
    
	public static String removeAccessModifiers(String code) {
		String result = StringUtils.replace(code, "private ", "");
		result = StringUtils.replace(result, "public ", "");
		result = StringUtils.replace(result, "native ", "");
		result = StringUtils.replace(result, "protected ", "");
		result = StringUtils.replace(result, "static ", "");
		result = StringUtils.replace(result, "volatile ", "");
		result = StringUtils.replace(result, "transient ", "");
		result = StringUtils.replace(result, "abstract ", "");
		result = StringUtils.replace(result, "synchronised ", "");
		result = StringUtils.replace(result, "final ", "");
		return result;
	}
}
