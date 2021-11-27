package io.github.rosemoe.editor.struct;

import java.util.Comparator;

/**
 * The class used to save auto complete result items
 *
 * @author Rose
 */
@SuppressWarnings("CanBeFinal")
public class ResultItem {

    public final static Comparator<ResultItem> COMPARATOR_BY_NAME = new Comparator<ResultItem>() {

        @Override
        public int compare(ResultItem p1, ResultItem p2) {
            return p1.label.compareTo(p2.label);
        }

    };

    public static final int TYPE_KEYWORD = 0;
    public static final int TYPE_LOCAL_METHOD = 1;
    public static final int TYPE_METHOD = 2;
    public static final int TYPE_FIELD = 3;
	public static final int TYPE_CLASS = 4;

    public int type;

    public String commit;

    public String label;

    public String desc;

    public int mask = 0;

    public static final int MASK_SHIFT_LEFT_ONCE = 1;
    public static final int MASK_SHIFT_LEFT_TWICE = 1 << 1;

    public ResultItem(String str, String desc) {
        type = TYPE_KEYWORD;
        commit = label = str;
        this.desc = desc;
    }

    public ResultItem(String label, String desc, int type) {
        this.label = this.commit = label;
        this.desc = desc;
        this.type = type;
    }

    public ResultItem(String label, String commit, String desc, int type) {
        this.label = label;
        this.commit = commit;
        this.desc = desc;
        this.type = type;
    }

    public ResultItem mask(int m) {
        mask = m;
        return this;
    }

}

