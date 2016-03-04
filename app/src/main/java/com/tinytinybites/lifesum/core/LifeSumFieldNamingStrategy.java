package com.tinytinybites.lifesum.core;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.regex.Pattern;
import com.google.gson.FieldNamingStrategy;

/**
 * Created by bundee on 3/2/16.
 */
public class LifeSumFieldNamingStrategy implements FieldNamingStrategy {
    //Tag
    private static final String TAG = LifeSumFieldNamingStrategy.class.getCanonicalName();

    private static final String JSON_WORD_DELIMITER = "_";
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("(?=\\p{Lu})"); //http://stackoverflow.com/a/20661766/377844

    public LifeSumFieldNamingStrategy() {}

    @Override
    public String translateName(Field f) {
        if (f.getName().startsWith("m")) {
            return handleWords(f.getName().substring(1));
        } else {
            return handleWords(f.getName());
        }
    }

    private String handleWords(String fieldName) {
        String[] words = UPPERCASE_PATTERN.split(fieldName);
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (sb.length() > 0) {
                sb.append(JSON_WORD_DELIMITER);
            }
            sb.append(word.toLowerCase(Locale.US));
        }
        return sb.toString();
    }
}
