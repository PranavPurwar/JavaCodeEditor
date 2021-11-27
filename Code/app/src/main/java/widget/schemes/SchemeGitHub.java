package io.github.rosemoe.editor.widget.schemes;

import io.github.rosemoe.editor.widget.EditorColorScheme;

/**
  * ColorScheme for editor
  * picked from GitHub site
 * Thanks to liyujiang-gzu (GitHub @liyujiang-gzu)
  */
public final class SchemeGitHub extends EditorColorScheme {

    @Override
    public void applyDefault() {
        super.applyDefault();
        setColor(ANNOTATION, 0xff6f42c1);
        setColor(FUNCTION_NAME, 0xff24292e);
        setColor(IDENTIFIER_NAME, 0xff24292e);
        setColor(IDENTIFIER_VAR, 0xff24292e);
        setColor(LITERAL, 0xff032f62);
        setColor(OPERATOR, 0xff005cc5);
        setColor(COMMENT, 0xff6a737d);
        setColor(KEYWORD, 0xffde3a49);
        setColor(WHOLE_BACKGROUND, 0xffffffff);
        setColor(TEXT_NORMAL, 0xff24292e);
        setColor(LINE_NUMBER_BACKGROUND, 0xffffffff);
        setColor(LINE_NUMBER, 0xffbec0c1);
        setColor(SELECTION_INSERT, 0xffc7edcc);
        setColor(SELECTION_HANDLE, 0xffc7edcc);
    }
    
}
