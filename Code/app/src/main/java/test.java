package io.github.rosemoe.editor;

import io.github.rosemoe.editor.util.Parser;
import io.github.rosemoe.editor.struct.Item;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Field;
import org.apache.commons.lang3.StringUtils;

public class test {

public static String path;

    public static void set(String s) {path = s;}
	public static Object[] getClasses() {
		try {
			JavaClass clazz = new ClassParser(path).parse();
			List<Item> list = new ArrayList();
			list.add(new Item(clazz));
			return list.toArray();
		} catch (Throwable e) {
			return null;
		}
	}


	public static Object[] getMethods() {
		try {
			JavaClass clazz = new ClassParser(path).parse();
			List<Item> list = new ArrayList();
			final Method[] methodArr = clazz.getMethods();
			for (Method method : methodArr) {
				String methodName = Parser.removeAccessModifiers(method.toString());
				if (method.isStatic() || method.isPublic()) {
					list.add(new Item(method));
				}
			}
			return list.toArray();
		} catch (Throwable e) {
			return null;
		}
	}


	public static Object[] getFields() {
		try {
			JavaClass clazz = new ClassParser(path).parse();
			List<Item> list = new ArrayList();
			final Field[] fields = clazz.getFields();
			for (Field field : fields) {
				list.add(new Item(field));
			}
			return list.toArray();
		} catch (Throwable e) {
			return null;
		}
	}
}
