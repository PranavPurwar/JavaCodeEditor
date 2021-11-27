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
		final String asStr = Parser.removeAccessModifiers(m.toString());
		this.label = StringUtils.substringAfter(asStr, " ");
		if (label.contains(" throws")) {
			label = StringUtils.substringBefore(label, " throws");
		}
		if (label.contains(" [")) {
			label = StringUtils.substringBefore(label, " [");
		}
		final String returnType = m.getReturnType().toString();
		this.desc = returnType.startsWith("java.lang.") ? StringUtils.substringAfter(returnType, "java.lang.") : returnType;
		this.desc = desc.replace(" ", "");
		this.commit = m.getName();
		this.commit += "()";
	}
	
	public Item(Field f) {
		final String asStr = Parser.removeAccessModifiers(f.toString());
		this.label = StringUtils.substringAfter(asStr, " ");
		if (label.contains(" ")) {
			this.label = StringUtils.substringBefore(label, " ");
		}
		this.desc = StringUtils.substringBefore(asStr, " ");
	}
	
	public Item(JavaClass clazz) {
		this.label = StringUtils.substringAfterLast(clazz.getClassName(), ".");
		this.desc = clazz.getPackageName();
	}
}
