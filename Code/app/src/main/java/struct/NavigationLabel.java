package io.github.rosemoe.editor.struct;

/**
 * Model for code navigation
 *
 * @author Rose
 */
@SuppressWarnings("CanBeFinal")
public class NavigationLabel {

    /**
     * The line position
     */
    public int line;

    /**
     * The description
     */
    public String label;

    /**
     * Create a new navigation
     *
     * @param line  The line position
     * @param label The description
     */
    public NavigationLabel(int line, String label) {
        this.line = line;
        this.label = label;
    }

}
