package gpacalculator;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public final class FormParser {
    private FormParser() {
    }

    public static Map<String, String> parse(String encoded) {
        Map<String, String> values = new LinkedHashMap<>();
        if (encoded == null || encoded.isBlank()) {
            return values;
        }

        for (String pair : encoded.split("&")) {
            int equals = pair.indexOf('=');
            String key = equals >= 0 ? pair.substring(0, equals) : pair;
            String value = equals >= 0 ? pair.substring(equals + 1) : "";
            values.put(decode(key), decode(value));
        }
        return values;
    }

    public static int intValue(Map<String, String> values, String key, int fallback, int min, int max) {
        try {
            int parsed = Integer.parseInt(values.getOrDefault(key, String.valueOf(fallback)).trim());
            return Math.max(min, Math.min(max, parsed));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    public static double doubleValue(Map<String, String> values, String key, double fallback) {
        try {
            String value = values.getOrDefault(key, "").trim();
            return value.isEmpty() ? fallback : Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
