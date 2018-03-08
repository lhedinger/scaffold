package org.hedinger.scaffold.utils;

public class StringBounds
{
    public static final int UNDEF = -1;

    public final int start;
    public final int end;

    public StringBounds(int start, int end)
    {
        this.start = (start < 0 ? UNDEF : start);
        this.end = (end < 0 ? UNDEF : end);
    }

    public String subString(String str) throws Exception
    {
        return str.substring(start, end);
    }

}
