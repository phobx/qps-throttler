package org.example.cache;

import java.util.Arrays;

public class StringArrayWrapper {

    private final String[] stringArray;

    public StringArrayWrapper(String[] stringArray) {
        this.stringArray = stringArray;
    }

    public String[] getStringArray() {
        return stringArray;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringArrayWrapper that = (StringArrayWrapper) o;
        return Arrays.equals(stringArray, that.stringArray);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(stringArray);
    }
}
