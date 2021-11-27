package io.github.rosemoe.editor.text;

/**
 * Indexer without cache
 *
 * @author Rose
 */
public final class NoCacheIndexer extends CachedIndexer implements Indexer {

    /**
     * Create a indexer without cache
     *
     * @param content Target content
     */
    public NoCacheIndexer(Content content) {
        super(content);
        //Disable dynamic indexing
        if (super.getMaxCacheSize() != 0) {
            super.setMaxCacheSize(0);
        }
        if (super.isHandleEvent()) {
            super.setHandleEvent(false);
        }
    }

}

