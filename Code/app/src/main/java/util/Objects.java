package io.github.rosemoe.editor.util;

/**
 * Compatible implementation of Objects
 *
 * @author Rose
 */
public class Objects {

    /**
     * If the value is null, throw a {@link NullPointerException}
     * Otherwise, return the value
     */
    public static <T> T requireNonNull(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }

}
