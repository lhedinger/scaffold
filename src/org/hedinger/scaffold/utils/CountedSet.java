package org.hedinger.scaffold.utils;

import java.util.HashMap;
import java.util.Map;

public class CountedSet<E> {

    private int sum = 0;
    private Map<E, Integer> set = new HashMap<>();

    public void put(E value) {
        Integer i = set.get(value);
        if(i == null) {
            i = 1;
        } else {
            i++;
        }
        set.put(value,i);
        sum++;
    }

    public int getCount(E value) {
        if(set.containsKey(value)) {
            return set.get(value);
        }
        return 0;
    }

    @Override
    public String toString() {
        return sum + ":" + set.toString();
    }
}
