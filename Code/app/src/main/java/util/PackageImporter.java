package io.github.rosemoe.editor.util;

import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.text.Content;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;

public class PackageImporter {
    
	public CodeEditor mEditor;
	ArrayList<String> imports = new ArrayList<String>();
	
	public PackageImporter(CodeEditor editor) {
		mEditor = editor;
	}
	
	public void importClass(String parent, String className, boolean isStatic) {
		String code = "";
		String content = isStatic ? "import static " + parent + "." + className : "import " + parent + "." + className;
		if (!imports.contains(content)) {
			imports.add(content);
		}
		mEditor.setText(importsToString());
	}
	
	public void organizeImports() {
		imports = getImportList();
		mEditor.setText(importsToString());
				
	}
	
	public ArrayList<String> getImportList() {
		imports = new ArrayList<>();
		Content content = mEditor.getText();
		boolean comment = false;
		int lineCount = content.getLineCount();
		int line = 0;
		if (lineCount == -1) return null;
		while (lineCount > line) {
			String text = content.getLineString(line);
		    if (text.contains("/*")) {
		        comment = true;
		    }
			if (text.startsWith("import ")) {
			    if (!comment) 
				    imports.add(text);
			}
			if (text.contains("*/")) {
			    comment = false;
			}
			if (text.contains("class") && !text.contains("\"")) break;
			++line;
		}
		Collections.sort(imports);
		return imports;
	}
	
	public String extractSource(String input) {
		String result = input.replaceFirst("import ", "");
		if (result.endsWith(";")) {
			result = result.substring(0, result.lastIndexOf(";"));
		}
		return result;
	}
	
	public String importsToString() {
		String code = "";
		int length = imports.size();
		int n = 0;
		while (length > n) {
			code += imports.get(n) + "\n";
			++n;
		}
		return code;
	}
}
