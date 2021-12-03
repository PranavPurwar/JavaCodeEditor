package io.github.rosemoe.editor.text;

import android.graphics.Paint;
import java.util.Arrays;

/**
  * Cache to measure text quickly
  * This is very useful when text is long
  * Use this to make editor 20x faster than before
  * It is not thread-safe
  *
  * @author Rose
  */
public class FontCache {
    
    private final float[] cache;

    private final char[] buffer;
    
    public FontCache() {
        cache = new float[65536];
        buffer = new char[2];
    }
    
    /**
      * Clear caches of font
      */
    public void clearCache() {
        Arrays.fill(cache, 0);
    }
    
    /**
      * Measure a single character
      */
    public float measureChar(char ch, Paint p) {
        float width = cache[(int) ch];
        if (width == 0) {
            buffer[0] = ch;
            width = p.measureText(buffer, 0, 1);
            cache[(int) ch] = width;
        }
        return width;
    }
    
    /*
     * Measure text
     */
    public float measureText(char[] chars, int start, int end, Paint p) {
        float width = 0f;
        for (int i = start;i < end;i++) {
            char ch = chars[i];
            if (isEmoji(ch) && i + 1 < end) {
                buffer[0] = ch;
                buffer[1] = chars[++i];
                width += p.measureText(buffer, 0, 2);
            } else {
                width += measureChar(ch, p);
            }
        }
        return width;
    }
    
    /**
      * Measure text
      */
    public float measureText(CharSequence str, int start, int end, Paint p) {
        float width = 0f;
        for (int i = start;i < end;i++) {
            char ch = str.charAt(i);
            if (isEmoji(ch) && i + 1 < end) {
                buffer[0] = ch;
                buffer[1] = str.charAt(++i);
                width += p.measureText(buffer, 0, 2);
            } else {
                width += measureChar(ch, p);
            }
        }
        return width;
    }
    
    private static boolean isEmoji(char ch) {
        return ch == 0xd83c || ch == 0xd83d;
    }
    
}
