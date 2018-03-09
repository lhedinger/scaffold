package org.hedinger.scaffold.utils;

public class StringBounds {
    public static final int UNDEF = -1;

    public final int start;
    public final int end;

    public StringBounds(int start, int end) {
        this.start = (start < 0 ? 0 : start);
        this.end = (end < 0 ? UNDEF : end);
    }

    public String subString(String str) throws Exception {
        return str.substring(start, end);
    }

    public boolean subsetOf(StringBounds superset) {
        if (start < superset.start) {
            return false;
        }

        if (superset.end == UNDEF) {
            return true;
        }

        if (end > superset.end) {
            return false;
        }

        return true;
    }

    public boolean isFinite() {
        if (start == UNDEF || end == UNDEF) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String startStr = (start == UNDEF ? "?" : String.valueOf(start));
        String endStr = (end == UNDEF ? "?" : String.valueOf(end));
        return "[" + startStr + "," + endStr + "]";
    }
}
