package Ez.SodiumClient.SRC.utils;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.NavigableMap;

public class NumberUtils {
    private static final NavigableMap<Long, String> SUFFIXES = Maps.newTreeMap();

    public NumberUtils() {
    }

    public static String formatCompact(long value) {
        if (value == Long.MIN_VALUE) {
            return formatCompact(-9223372036854775807L);
        } else if (value < 0L) {
            return "-" + formatCompact(-value);
        } else if (value < 1000L) {
            return Long.toString(value);
        } else {
            Map.Entry<Long, String> entry = SUFFIXES.floorEntry(value);
            Long divideBy = (Long)entry.getKey();
            String suffix = (String)entry.getValue();
            long truncated = value / (divideBy / 10L);
            boolean hasDecimal = truncated < 100L && (double)truncated / 10.0 != (double)(truncated / 10L);
            return hasDecimal ? (double)truncated / 10.0 + suffix : truncated / 10L + suffix;
        }
    }

    static {
        SUFFIXES.put(1000L, "K");
        SUFFIXES.put(1000000L, "M");
        SUFFIXES.put(1000000000L, "G");
        SUFFIXES.put(1000000000000L, "T");
        SUFFIXES.put(1000000000000000L, "P");
        SUFFIXES.put(1000000000000000000L, "E");
    }
}
