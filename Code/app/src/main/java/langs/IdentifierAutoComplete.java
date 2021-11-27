package io.github.rosemoe.editor.langs;

import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.struct.ResultItem;
import io.github.rosemoe.editor.struct.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
* Identifier auto-completion
* You can use it to provide identifiers
* <strong>Note:</strong> To use this, you must use {@link Identifiers} as {@link TextAnalyzeResult#mExtra}
*/
public class IdentifierAutoComplete implements AutoCompleteProvider {
	
	private String[] mKeywords;
	
	public void setKeywords(String[] keywords) {
		mKeywords = keywords;
	}
	
	public String[] getKeywords() {
		return mKeywords;
	}
	
	public static class Identifiers {
		
		private final List<String> identifiers = new ArrayList<>();
		private HashMap<String, Object> cache;
		private final static Object SIGN = new Object();
		
		public void addIdentifier(String identifier) {
			if (cache == null) {
				throw new IllegalStateException("begin() has not been called");
			}
			if (cache.put(identifier, SIGN) == SIGN) {
				return;
			}
			identifiers.add(identifier);
		}
		
		public void begin() {
			cache = new HashMap<>();
		}
		
		public void finish() {
			cache.clear();
			cache = null;
		}
		
		public List<String> getIdentifiers() {
			return identifiers;
		}
		
	}
	
	@Override
	public List<ResultItem> getAutoCompleteItems(String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int line) {
		List<ResultItem> keywords = new ArrayList<>();
		final String[] keywordArray = mKeywords;
		if (keywordArray != null) {
			for (String kw : keywordArray) {
				if (kw.startsWith(prefix)) {
					keywords.add(new ResultItem(kw, "Keyword", ResultItem.TYPE_KEYWORD));
				}
			}
		}
		Collections.sort(keywords, ResultItem.COMPARATOR_BY_NAME);
		Object extra = colors.mExtra;
		Identifiers userIdentifiers = (extra instanceof Identifiers) ? (Identifiers) extra : null;
		if (userIdentifiers != null) {
			List<ResultItem> words = new ArrayList<>();
			for (String word : userIdentifiers.getIdentifiers()) {
				if (word.startsWith(prefix) && !(word.equals(prefix))) {
					words.add(new ResultItem(word, "Local", ResultItem.TYPE_LOCAL_METHOD));
				}
			}
			Collections.sort(words, ResultItem.COMPARATOR_BY_NAME);
			keywords.addAll(words);
		}
		List<ResultItem> methods = new ArrayList<>();
		final Object[] methodArr = io.github.rosemoe.editor.test.getMethods();
		if (methodArr != null) {
			for (Object obj : methodArr) {
				Item method = (Item) obj;
				if (method.label.startsWith(prefix)) {
					methods.add(new ResultItem(method.label, method.commit, method.desc, ResultItem.TYPE_METHOD));
				}
			}
			Collections.sort(methods, ResultItem.COMPARATOR_BY_NAME);
			keywords.addAll(methods);
		}
		List<ResultItem> fields = new ArrayList();
		final Object[] fieldArr = io.github.rosemoe.editor.test.getFields();
		if (fieldArr != null) {
			for (Object obj : fieldArr) {
				Item field = (Item) obj;
				if (field.label.startsWith(prefix)) {
					fields.add(new ResultItem(field.label, field.desc, ResultItem.TYPE_FIELD));
				}
			}
			Collections.sort(fields, ResultItem.COMPARATOR_BY_NAME);
			keywords.addAll(fields);
		}
		List<ResultItem> classes = new ArrayList();
		final Object[] classArr = io.github.rosemoe.editor.test.getClasses();
		if (classArr != null) {
			for (Object obj : classArr) {
				Item clazz = (Item) obj;
				if (clazz.label.startsWith(prefix)) {
					classes.add(new ResultItem(clazz.label, clazz.desc, ResultItem.TYPE_CLASS));
				}
			}
			Collections.sort(classes, ResultItem.COMPARATOR_BY_NAME);
			keywords.addAll(classes);
		}
		return keywords;
	}
	
	
}
