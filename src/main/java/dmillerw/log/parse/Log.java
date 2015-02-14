package dmillerw.log.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Log {

    private Map<String, Category> categoryMap = Maps.newHashMap();
    public final Set<String> levelSet;

    public Log(Map<String, Category> categoryMap, Set<String> levelSet) {
        this.categoryMap = categoryMap;
        this.levelSet = levelSet;
    }

    private String[] levelsToArray() {
        return levelSet.toArray(new String[levelSet.size()]);
    }

    public List<String> getCategories() {
        List<String> list = Lists.newArrayList();
        list.addAll(categoryMap.keySet());
        Collections.sort(list);
        return list;
    }

    public Category getCategory(String string) {
        return categoryMap.get(string);
    }

    public List<Entry> getAllEntries() {
        List<Entry> list = Lists.newLinkedList();
        categoryMap.values().forEach((c) -> list.addAll(c.getEntries(levelsToArray())));
        Collections.sort(list, (d1, d2) -> d1.timestamp.compareTo(d2.timestamp));
        return list;
    }

    public List<Entry> getAllEntries(String ... levels) {
        List<Entry> list = Lists.newLinkedList();
        categoryMap.values().forEach((c) -> list.addAll(c.getEntries(levels)));
        Collections.sort(list, (d1, d2) -> d1.timestamp.compareTo(d2.timestamp));
        return list;
    }
}
