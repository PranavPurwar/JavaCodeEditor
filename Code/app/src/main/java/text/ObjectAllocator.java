package io.github.rosemoe.editor.text;

import java.util.List;

import io.github.rosemoe.editor.struct.BlockLine;

/**
 * A object provider for speed improvement
 * Now meaningless because it is not as well as it expected
 *
 * @author Rose
 */
public class ObjectAllocator {

    private static List<BlockLine> blockLines;
    private static final int RECYCLE_LIMIT = 1024 * 8;

    public static void recycleBlockLine(List<BlockLine> src) {
        if (src == null) {
            return;
        }
        if (blockLines == null) {
            blockLines = src;
            return;
        }
        int size = blockLines.size();
        int sizeAnother = src.size();
        while (sizeAnother > 0 && size < RECYCLE_LIMIT) {
            size++;
            sizeAnother--;
            blockLines.add(src.get(sizeAnother));
        }
    }

    public static BlockLine obtainBlockLine() {
        return (blockLines == null || blockLines.isEmpty()) ? new BlockLine() : blockLines.remove(blockLines.size() - 1);
    }

}
