package ma.glasnost.orika.impl;

import com.google.common.collect.ImmutableMap;
import ma.glasnost.orika.MapEntry;

import java.util.*;

public class DefaultConcreteTypeMap {

    private static Map<Class, Class> map = ImmutableMap.<Class, Class>builder()
            .put(Collection.class, ArrayList.class)
            .put(List.class, ArrayList.class)
            .put(Set.class, LinkedHashSet.class)
            .put(Map.class, LinkedHashMap.class)
            .put(Map.Entry.class, MapEntry.class)
            .put(SortedMap.class, TreeMap.class)
            .put(SortedSet.class, TreeSet.class)
            .build();

    public static Set<Map.Entry<Class, Class>> getAll() {
        return map.entrySet();
    }

    public static Class get(Class<?> type) {
        return map.get(type);
    }
}
