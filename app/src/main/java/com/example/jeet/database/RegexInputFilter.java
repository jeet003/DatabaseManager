package com.example.jeet.database;

import android.text.InputFilter;
import android.text.Spanned;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexInputFilter implements InputFilter {

    private Pattern mPattern;
    private static final String CLASS_NAME = RegexInputFilter.class.getSimpleName();

    /**
     * Convenience constructor, builds Pattern object from a String
     *
     * @param pattern Regex string to build pattern from.
     */
    public RegexInputFilter(String pattern) {
        this(Pattern.compile(pattern));
    }

    public RegexInputFilter(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException(CLASS_NAME + " requires a regex.");
        }

        mPattern = pattern;
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int sourceStart, int sourceEnd,
                               Spanned destination, int destinationStart,
                               int destinationEnd) {
        String textToCheck = destination.subSequence(0, destinationStart).
                toString() + source.subSequence(sourceStart, sourceEnd) +
                destination.subSequence(
                        destinationEnd, destination.length()).toString();
        Matcher matcher = mPattern.matcher(textToCheck);
        if (!matcher.matches()) {

            // It does not match partially too
            if (!matcher.hitEnd()) {
                return "";
            }
        }
            return null;

    }
}