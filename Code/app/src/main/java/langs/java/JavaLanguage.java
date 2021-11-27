package io.github.rosemoe.editor.langs.java;

import io.github.rosemoe.editor.langs.internal.MyCharacter;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.langs.IdentifierAutoComplete;

/**
 * Java language is much complex.
 * This is a basic support
 *
 * @author Rose
 */
public class JavaLanguage implements EditorLanguage {
	
	public boolean colon = false;
	
    @Override
    public CodeAnalyzer getAnalyzer() {
        return new JavaCodeAnalyzer();
    }

    @Override
    public AutoCompleteProvider getAutoCompleteProvider() {
        IdentifierAutoComplete autoComplete = new IdentifierAutoComplete();
        autoComplete.setKeywords(JavaTextTokenizer.sKeywords);
        return autoComplete;
    }

    @Override
    public boolean isAutoCompleteChar(char ch) {
        return MyCharacter.isJavaIdentifierPart(ch);
    }

    @Override
    public int getIndentAdvance(String content) {
        JavaTextTokenizer t = new JavaTextTokenizer(content);
        Tokens token;
        int advance = 0;
        while ((token = t.directNextToken()) != Tokens.EOF) {
            switch (token) {
                case COLON:
					advance++;
					colon = true;
                    break;
                case LBRACE:
				    if (!colon) advance++;
					colon = false;
                    break;
                case RBRACE:
				    if (advance != 0) advance--;
                    break;
            }
        }
        advance = Math.max(0, advance);
        return advance * 4;
    }

    @Override
    public boolean useTab() {
        return true;
    }

    @Override
    public CharSequence format(CharSequence content) {
        return content;
    }
}
