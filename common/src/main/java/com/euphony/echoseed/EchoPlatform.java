package com.euphony.echoseed;

import java.nio.file.Path;

public final class EchoPlatform {
    public static Helper helper = Helper.MISSING;

    private EchoPlatform() {
    }

    public static boolean isModLoaded(String id) {
        return helper.isModLoaded(id);
    }

    public static Path configDir() {
        return helper.configDir();
    }

    public interface Helper {
        Helper MISSING = new Helper() {
            @Override
            public boolean isModLoaded(String id) {
                return false;
            }

            @Override
            public Path configDir() {
                return Path.of("config");
            }
        };

        boolean isModLoaded(String id);

        Path configDir();
    }
}
