package io.github.rosemoe.editor.widget.schemes;

import io.github.rosemoe.editor.widget.EditorColorScheme;

/**
  * ColorScheme for editor
  * picked from Notepad++ v7.8.1
 * Thanks to liyujiang-gzu (GitHub @liyujiang-gzu)
  */
public final class SchemeNotepadXX extends EditorColorScheme {

    @Override
    public void applyDefault() {
        super.applyDefault();
        setColor(ANNOTATION, 0xff0000ff);
        setColor(FUNCTION_NAME, 0xff000000);
        setColor(IDENTIFIER_NAME, 0xff000000);
        setColor(IDENTIFIER_VAR, 0xff000000);
        setColor(LITERAL, 0xff808080);
        setColor(OPERATOR, 0xff0000ff);
        setColor(COMMENT, 0xff008000);
        setColor(KEYWORD, 0xff8000ff);
        setColor(WHOLE_BACKGROUND, 0xffffffff);
        setColor(TEXT_NORMAL, 0xff000000);
        setColor(LINE_NUMBER_BACKGROUND, 0xffe4e4e4);
        setColor(LINE_NUMBER, 0xff808080);
        setColor(SELECTED_TEXT_BACKGROUND, 0xff75d975);
        setColor(MATCHED_TEXT_BACKGROUND, 0xffc0c0c0);
        setColor(CURRENT_LINE, 0xffe8e8ff);
        setColor(SELECTION_INSERT, 0xff8000ff);
        setColor(SELECTION_HANDLE, 0xff8000ff);
        setColor(BLOCK_LINE, 0xffc0c0c0);
        setColor(BLOCK_LINE_CURRENT, 0);
    }
    
}
