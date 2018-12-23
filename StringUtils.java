package com.yana.YFrame.StringUtils;/*
 * Ali Ghalambaz<aghalambaz@gmail.com> on 8/1/14.
 * Written in yframe
 * com.yana.YFrame For Android Beta
 */

import java.util.zip.CRC32;

public class StringUtils {

    public static String convertUnicodeEscape(String s) {
        char[] out = new char[s.length()];

        ParseState state = ParseState.NORMAL;
        int j = 0, k = 0, unicode = 0;
        char c = ' ';
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (state == ParseState.ESCAPE) {
                if (c == 'u') {
                    state = ParseState.UNICODE_ESCAPE;
                    unicode = 0;
                } else { // we don't care about other escapes
                    out[j++] = '\\';
                    out[j++] = c;
                    state = ParseState.NORMAL;
                }
            } else if (state == ParseState.UNICODE_ESCAPE) {
                if ((c >= '0') && (c <= '9')) {
                    unicode = (unicode << 4) + c - '0';
                } else if ((c >= 'a') && (c <= 'f')) {
                    unicode = (unicode << 4) + 10 + c - 'a';
                } else if ((c >= 'A') && (c <= 'F')) {
                    unicode = (unicode << 4) + 10 + c - 'A';
                } else {
                    throw new IllegalArgumentException("Malformed unicode escape");
                }
                k++;

                if (k == 4) {
                    out[j++] = (char) unicode;
                    k = 0;
                    state = ParseState.NORMAL;
                }
            } else if (c == '\\') {
                state = ParseState.ESCAPE;
            } else {
                out[j++] = c;
            }
        }

        if (state == ParseState.ESCAPE) {
            out[j++] = c;
        }

        return new String(out, 0, j);
    }

    public static String convertToCRC32(String content)
    {
        CRC32 crc = new CRC32();
        crc.update(content.getBytes());
        return Long.toHexString(crc.getValue());
    }
    public static String lastCharacter(String text)
    {
        return text.substring(text.length()-1);
    }
    static enum ParseState {
        NORMAL,
        ESCAPE,
        UNICODE_ESCAPE
    }
}
