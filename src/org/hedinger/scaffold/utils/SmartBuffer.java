package org.hedinger.scaffold.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartBuffer {
    private final String body;

    public SmartBuffer(FileReader file) {
        String s = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(file);

            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line).append("\n");
            }

            s = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }

        body = s;
    }

    public SmartBuffer(String body) {
        this.body = body;
    }

    public int indexOf(String value, int start) throws Exception {
        return body.indexOf(value, start);
    }

    public StringBounds firstMatch(String regex, int start) throws Exception {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(body);

        if (!matcher.find(start))
            return null;

        return new StringBounds(matcher.start(), matcher.end());
    }

    public String substring(StringBounds range) throws Exception {
        if (range == null) {
            return null;
        }
        return substring(range.start, range.end);
    }

    public String substring(int start, int end) throws Exception {
        return body.substring(start, end);
    }

    public int length() {
        return body.length();
    }
}
