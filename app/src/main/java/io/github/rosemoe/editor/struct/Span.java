package io.github.rosemoe.editor.struct;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.github.rosemoe.editor.widget.EditorColorScheme;

/**
 * The span model
 *
 * @author Rose
 */
public class Span {

    /**
     * Flag for {@link Span#problemFlags}.
     *
     * Indicates this span is in ERROR region
     */
    public static final int FLAG_ERROR = 1 << 3;
    /**
     * Flag for {@link Span#problemFlags}.
     *
     * Indicates this span is in WARNING region
     */
    public static final int FLAG_WARNING = 1 << 2;
    /**
     * Flag for {@link Span#problemFlags}.
     *
     * Indicates this span is in TYPO region
     */
    public static final int FLAG_TYPO = 1 << 1;
    /**
     * Flag for {@link Span#problemFlags}.
     *
     * Indicates this span is in DEPRECATED region
     */
    public static final int FLAG_DEPRECATED = 1;

    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALICS = 1 << 1;

    public int column;

    public int colorId;

    public int underlineColor = 0;

    /**
     * Create a new span
     *
     * @param column  Start column of span
     * @param colorId Type of span
     * @see Span#obtain(int, int)
     */
    private Span(int column, int colorId) {
        this.column = column;
        this.colorId = colorId;
    }

    /**
     * Set a underline for this region
     * Zero for no underline
     *
     * @param color Color for this underline (not color id of {@link EditorColorScheme})
     * @return Self
     */
    public Span setUnderlineColor(int color) {
        underlineColor = color;
        return this;
    }

    /**
     * Get span start column
     *
     * @return Start column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Set column of this span
     */
    public Span setColumn(int column) {
        this.column = column;
        return this;
    }

    /**
     * Make a copy of this span
     */
    public Span copy() {
        Span copy = obtain(column, colorId);
        copy.setUnderlineColor(underlineColor);
        return copy;
    }

    public boolean recycle() {
        colorId = column = underlineColor = 0;
        return cacheQueue.offer(this);
    }

    public static Span obtain(int column, int colorId) {
        Span span = cacheQueue.poll();
        if (span == null) {
            return new Span(column, colorId);
        } else {
            span.column = column;
            span.colorId = colorId;
            return span;
        }
    }

    public static void recycleAll(Collection<Span> spans) {
        for (Span span : spans) {
            if (!span.recycle()) {
                return;
            }
        }
    }

    private static final BlockingQueue<Span> cacheQueue = new ArrayBlockingQueue<>(8192 * 2);

}
