package io.github.rosemoe.editor.widget;

/**
 * This class represents a 'row' in editor
 * Editor uses this to draw rows
 *
 * @author Rose
 */
class Row {

    /**
     * The index in lines
     * But not row index
     */
    public int lineIndex;

    /**
     * Whether this row is a start of a line
     * Editor will draw line number to left of this row to indicate this
      */
    public boolean isLeadingRow;
    
    /**
      * Start index in target line
      */
    public int startColumn;
    
    /**
      * End index in target line
      */
    public int endColumn;
    
}
