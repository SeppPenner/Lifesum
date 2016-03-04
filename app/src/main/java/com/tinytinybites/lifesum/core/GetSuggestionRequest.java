package com.tinytinybites.lifesum.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bundee on 3/1/16.
 */
public class GetSuggestionRequest implements Api.Request{
    List<String> mErrors = new ArrayList<>();

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public List<String> getErrors() {
        return mErrors;
    }

}
