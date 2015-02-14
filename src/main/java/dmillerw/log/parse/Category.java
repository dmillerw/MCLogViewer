package dmillerw.log.parse;

import com.google.common.collect.Lists;
import dmillerw.log.util.CollectionHelper;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Category {

    public final String name;
    private LinkedList<Entry> entries = Lists.newLinkedList();

    public Category(String name) {
        this.name = name;
    }

    public void addEntry(Date timestamp, String level, String entry) {
        entries.add(new Entry(timestamp, level, entry));
    }

    public List<Entry> getEntries(final String ... levels) {
        List<Entry> list = Lists.newLinkedList();
        entries.forEach((entry) -> {
            if (CollectionHelper.arrayContains(entry.level, levels, true))
                list.add(entry);
        });
        Collections.sort(list, (d1, d2) -> d1.timestamp.compareTo(d2.timestamp));
        return list;
    }

    @Override
    public String toString() {
        return name + ": " + entries;
    }
}
