package dmillerw.log.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public class CollectionHelper {

    public static <T> List<T> setToList(Set<T> set) {
        List<T> list = Lists.newArrayList();
        for (T t : set) {
            list.add(t);
        }
        return list;
    }

    public static <T> boolean arrayContains(T search, T[] array, boolean def) {
        if (array == null) {
            return def;
        }

        for (T t : array) {
            if (search.equals(t)) {
                return true;
            }
        }
        return false;
    }
}
