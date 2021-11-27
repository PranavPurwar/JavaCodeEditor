package io.github.rosemoe.editor.widget;

/**
 * This class is used to control cursor visibility
 *
 * @author Rose
 */
final class CursorBlink implements Runnable {

    long lastSelectionModificationTime = 0;
    int period;
    boolean visibility;
    boolean valid;
    final CodeEditor editor;

    CursorBlink(CodeEditor editor, int period) {
        visibility = true;
        this.editor = editor;
        this.period = period;
    }

    void setPeriod(int period) {
        this.period = period;
        if (period <= 0) {
            visibility = true;
            valid = false;
        } else {
            valid = true;
        }
    }

    void onSelectionChanged() {
        lastSelectionModificationTime = System.currentTimeMillis();
        visibility = true;
    }

    @Override
    public void run() {
        if (valid && period > 0) {
            if (System.currentTimeMillis() - lastSelectionModificationTime >= period * 2) {
                visibility = !visibility;
                editor.invalidate();
            }
            editor.postDelayed(this, period);
        } else {
            visibility = true;
        }
    }

}
