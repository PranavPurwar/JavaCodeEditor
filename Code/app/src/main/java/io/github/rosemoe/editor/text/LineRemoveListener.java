package io.github.rosemoe.editor.text;

/**
  * A listener to know when a ContentLine object is removed from Content object
  *
  * @author Rose
  */
public interface LineRemoveListener {
    
    /**
      * When a ContentLine is removed from Content, this method is called
      *
      * @param content Caller Content
      * @param line ContentLine object removed
      */
    void onRemove(Content content, ContentLine line);
    
}
