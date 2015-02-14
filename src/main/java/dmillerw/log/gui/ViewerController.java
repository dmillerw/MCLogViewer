package dmillerw.log.gui;

import com.google.common.collect.Lists;
import com.sun.javafx.collections.ObservableListWrapper;
import dmillerw.log.parse.Category;
import dmillerw.log.parse.Entry;
import dmillerw.log.parse.LogParser;
import dmillerw.log.util.CollectionHelper;
import javafx.event.Event;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.util.Collections;
import java.util.List;

public class ViewerController {

    private static String entriesToString(List<Entry> entryList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry e : entryList) {
            stringBuilder.append("[").append(LogParser.FORMAT.format(e.timestamp)).append("] ");
            stringBuilder.append("[").append(e.level).append("]: ");
            stringBuilder.append(e.entry);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private String[] selectedCategories;
    private String[] selectedLevels;

    public TextArea log;

    public ListView categoryList;
    public ListView levelList;

    public void initialize() {
        categoryList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        categoryList.setItems(new ObservableListWrapper(Viewer.log.getCategories()));
        categoryList.getSelectionModel().clearSelection();

        levelList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        levelList.setItems(new ObservableListWrapper(CollectionHelper.setToList(Viewer.log.levelSet)));
        levelList.getSelectionModel().clearSelection();

        reloadLog();
    }

    public void onCategoryListClicked(Event event) {
        List<String> temp = Lists.newArrayList();
        categoryList.getSelectionModel().getSelectedItems().forEach((o) -> temp.add((String) o));
        selectedCategories = temp.toArray(new String[temp.size()]);

        reloadLog();
    }

    public void onLevelListClicked(Event event) {
        selectedLevels = (String[]) levelList.getSelectionModel().getSelectedItems().toArray(new String[0]);

        reloadLog();
    }

    private void reloadLog() {
        if (selectedCategories == null || selectedCategories.length == 0) {
            if (selectedLevels == null || selectedLevels.length == 0) {
                log.setText(entriesToString(Viewer.log.getAllEntries()));
            } else {
                log.setText(entriesToString(Viewer.log.getAllEntries(selectedLevels)));
            }
        } else {
            List<Category> categories = Lists.newArrayList();
            for (String str : selectedCategories) {
                categories.add(Viewer.log.getCategory(str));
            }
            List<Entry> entries = Lists.newArrayList();
            categories.forEach((c) -> entries.addAll(c.getEntries(selectedLevels)));
            Collections.sort(entries, (d1, d2) -> d1.timestamp.compareTo(d2.timestamp));
            log.setText(entriesToString(entries));
        }
    }

    public void reload(Event event) {
        Viewer.load(true);
        initialize();
        reset(null);
        reloadLog();
    }

    public void load(Event event) {
        Viewer.load(false);
        initialize();
        reset(null);
        reloadLog();
    }

    public void reset(Event event) {
        selectedCategories = null;
        selectedLevels = null;

        categoryList.getSelectionModel().clearSelection();
        levelList.getSelectionModel().clearSelection();

        reloadLog();
    }
}
