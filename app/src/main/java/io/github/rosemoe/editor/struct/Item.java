package io.github.rosemoe.editor.struct;

import io.github.rosemoe.editor.util.Parser;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.commons.lang3.StringUtils;

public class Item {
	
	public String label;
	public String commit;
	public String desc;
	
	public Item(Method m) {
		String params = m.toString();
		params = StringUtils.substring(params, params.indexOf("("), params.indexOf(")") + 1);
		this.label = m.getName() + params;
		final String returnType = m.getReturnType().toString();
		this.desc = returnType.startsWith("java.lang.") ? StringUtils.substringAfter(returnType, "java.lang.") : returnType;
		this.desc = desc.replace(" ", "");
		this.commit = m.getName();
		this.commit += "()";
	}
	
	public Item(Field f) {
		this.label = f.getName();
		this.desc = f.getType().toString();
	}
	
	public Item(JavaClass clazz) {
		this.label = StringUtils.substringAfterLast(clazz.getClassName(), ".");
		this.desc = clazz.getPackageName();
	}
}
