package io.github.rosemoe.editor.widget;

import java.util.NoSuchElementException;

/**
 * Row iterator.
 * This iterator is able to return a series of Row objects linearly
 * Editor uses this to get information of rows and paint them accordingly
 *
 * @author Rose
 */
interface RowIterator {

    /**
     * Return next Row object
     *
     * The result should not be stored, because implementing classes will always return the same
     * object due to performance
     *
     * @return Row object contains the information about a row
     * @throws NoSuchElementException If no more row available
     */
    Row next();

    /**
     * Whether there is more Row object
     *
     * @return Whether more row available
     */
    boolean hasNext();

}
