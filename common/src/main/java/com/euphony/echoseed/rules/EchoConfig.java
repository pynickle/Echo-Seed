package com.euphony.echoseed.rules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Server-owned configuration values. No Minecraft or YACL types.
 */
public record EchoConfig(
        double growthSpeed,
        double markDurationSeconds,
        double cooldownSeconds,
        int presenceRange,
        boolean showMarkDuration
) {
    public static final EchoConfig DEFAULTS = new EchoConfig(1.0, 45.0, 8.0, 4, false);
    public static final String FILE_NAME = "echo_seed.json";
    public static final String GROWTH_SPEED_KEY = "growth_speed";
    public static final String MARK_DURATION_SECONDS_KEY = "mark_duration_seconds";
    public static final String COOLDOWN_SECONDS_KEY = "cooldown_seconds";
    public static final String PRESENCE_RANGE_KEY = "presence_range";
    public static final String SHOW_MARK_DURATION_KEY = "show_mark_duration";

    public static EchoConfig sanitize(
            double growthSpeed,
            double markDurationSeconds,
            double cooldownSeconds,
            int presenceRange,
            boolean showMarkDuration
    ) {
        return new EchoConfig(
                growthSpeed > 0.0 ? growthSpeed : DEFAULTS.growthSpeed,
                markDurationSeconds > 0.0 ? markDurationSeconds : DEFAULTS.markDurationSeconds,
                cooldownSeconds >= 0.0 ? cooldownSeconds : DEFAULTS.cooldownSeconds,
                presenceRange >= 0 ? presenceRange : DEFAULTS.presenceRange,
                showMarkDuration
        );
    }

    public static EchoConfig parse(String json) {
        if (json == null || json.isBlank()) {
            return DEFAULTS;
        }
        try {
            JsonElement element = JsonParser.parseString(json);
            if (!element.isJsonObject()) {
                return DEFAULTS;
            }
            JsonObject object = element.getAsJsonObject();
            return sanitize(
                    number(object, GROWTH_SPEED_KEY, DEFAULTS.growthSpeed),
                    number(object, MARK_DURATION_SECONDS_KEY, DEFAULTS.markDurationSeconds),
                    number(object, COOLDOWN_SECONDS_KEY, DEFAULTS.cooldownSeconds),
                    (int) number(object, PRESENCE_RANGE_KEY, DEFAULTS.presenceRange),
                    booleanValue(object, SHOW_MARK_DURATION_KEY, DEFAULTS.showMarkDuration)
            );
        } catch (RuntimeException ignored) {
            return DEFAULTS;
        }
    }

    public static EchoConfig read(Path path) {
        if (path == null || !Files.isRegularFile(path)) {
            return DEFAULTS;
        }
        try {
            return parse(Files.readString(path));
        } catch (IOException ignored) {
            return DEFAULTS;
        }
    }

    public static void write(Path path, EchoConfig config) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(path, config.toJson());
    }

    public String toJson() {
        return "{\n"
                + "  \"" + GROWTH_SPEED_KEY + "\": " + number(growthSpeed) + ",\n"
                + "  \"" + MARK_DURATION_SECONDS_KEY + "\": " + number(markDurationSeconds) + ",\n"
                + "  \"" + COOLDOWN_SECONDS_KEY + "\": " + number(cooldownSeconds) + ",\n"
                + "  \"" + PRESENCE_RANGE_KEY + "\": " + presenceRange + ",\n"
                + "  \"" + SHOW_MARK_DURATION_KEY + "\": " + showMarkDuration + "\n"
                + "}\n";
    }

    public long stageDurationMillis() {
        return Math.max(1L, Math.round(EchoRules.DEFAULT_STAGE_DURATION_MILLIS / growthSpeed));
    }

    public long markDurationMillis() {
        return Math.max(1L, Math.round(markDurationSeconds * 1000.0));
    }

    public long cooldownMillis() {
        return Math.max(0L, Math.round(cooldownSeconds * 1000.0));
    }

    private static double number(JsonObject object, String key, double fallback) {
        if (!object.has(key) || !object.get(key).isJsonPrimitive() || !object.get(key).getAsJsonPrimitive().isNumber()) {
            return fallback;
        }
        return object.get(key).getAsDouble();
    }

    private static boolean booleanValue(JsonObject object, String key, boolean fallback) {
        if (!object.has(key) || !object.get(key).isJsonPrimitive() || !object.get(key).getAsJsonPrimitive().isBoolean()) {
            return fallback;
        }
        return object.get(key).getAsBoolean();
    }

    private static String number(double value) {
        if (value == Math.rint(value) && !Double.isInfinite(value)) {
            return Long.toString(Math.round(value));
        }
        return String.format(Locale.ROOT, "%s", value);
    }
}
