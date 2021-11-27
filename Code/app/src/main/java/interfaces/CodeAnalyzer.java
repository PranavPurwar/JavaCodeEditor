package io.github.rosemoe.editor.interfaces;

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;

/**
 * Interface for analyzing highlight
 *
 * @author Rose
 */
public interface CodeAnalyzer {

    /**
     * Analyze spans for the given input
     *
     * @param content  The input text
     * @param colors   Result dest
     * @param delegate Delegate between thread and analyzer
     * @see TextAnalyzer#analyze(Content)
     * @see TextAnalyzer.AnalyzeThread.Delegate#shouldAnalyze()
     */
    void analyze(CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate);

}
