package dmillerw.log.parse;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    public static final DateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static Pattern PATTERN = Pattern.compile("\\[(.*?)\\]");

    private static String getNthMatch(String string, int index) {
        Matcher matcher = PATTERN.matcher(string);
        for (int i=0; i<=index; i++) {
            matcher.find();
        }
        return matcher.group();
    }

    private static Date getDate(String line) {
        String date = getNthMatch(line, 0);
        date = date.replace("[", "").replace("]", "");
        try {
            return FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

    private static String getLevel(String line) {
        String level = getNthMatch(line, 1);
        level = level.replace("[", "").replace("]", "");
        level = level.substring(level.lastIndexOf("/") + 1);
        return level;
    }

    private static String getCategory(String line) {
        String category = getNthMatch(line, 2);
        category = category.replace("[", "").replace("]", "");
        category = category.substring(0, category.lastIndexOf("/"));
        return category;
    }

    private static String getEntry(String line) {
        return line.substring(line.indexOf(":", 9) + 2);
    }

    public static Log parseLog(File file) {
        final Map<String, Category> tempMap = Maps.newHashMap();
        final Set<String> levels = Sets.newHashSet();

        try {
            for (String line : Files.readAllLines(file.toPath())) {
                if (line.isEmpty())
                    continue;

                if (!line.startsWith("["))
                    continue;

                String mod = getCategory(line);
                if (mod.equalsIgnoreCase("STDOUT"))
                    continue;

                Date timestamp = getDate(line);
                String level = getLevel(line);
                String entry = getEntry(line);

                levels.add(level);

                if (mod.equalsIgnoreCase("Forge Mod Loader")) {
                    mod = "FML";
                }

                if (mod.equalsIgnoreCase("MinecraftForge")) {
                    mod = "Minecraft Forge";
                }

                if (entry.startsWith("Sent event") || entry.startsWith("Sending event")) {
                    mod = "FML";
                }

                if (entry.startsWith("Parsing dependency info") || entry.startsWith("Parsed dependency info")) {
                    mod = "FML";
                }

                if (entry.startsWith("Enabling mod")) {
                    mod = "FML";
                }

                Category category = tempMap.get(mod);
                if (category == null) {
                    category = new Category(mod);
                }
                category.addEntry(timestamp, level, entry);

                tempMap.put(mod, category);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Log(tempMap, levels);
    }
}
