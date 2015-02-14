package dmillerw.log.parse;

import java.util.Date;

public class Entry {

    public final Date timestamp;
    public final String level;
    public final String entry;

    public Entry(Date timestamp, String level, String entry) {
        this.timestamp = timestamp;
        this.level = level;
        this.entry = entry;
    }
}
