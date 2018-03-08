package org.hedinger.scaffold.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartBuffer
{
    private final String str;
    private int offset = 0;

    public SmartBuffer(String uri)
    {
        String s = null;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(uri));

            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                stringBuffer.append(line).append("\n");
            }

            s = stringBuffer.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        str = s;
    }

    public int indexOf(String value) throws Exception
    {
        return str.indexOf(value, offset);
    }

    public StringBounds firstMatch(String regex) throws Exception
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (!matcher.find(offset)) return null;
       
        return new StringBounds(matcher.start(), matcher.end());
    }

    public void skip(int offset) throws Exception
    {
        this.offset = offset;
    }

    public String subString(int start, int end) throws Exception
    {
        return str.substring(start, end);
    }

    public int length()
    {
        return str.length();
    }
}
