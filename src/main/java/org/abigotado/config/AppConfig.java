package org.abigotado.config;

public class AppConfig {
    public static final int DEFAULT_CLICKS = 10;

    public static final int DEFAULT_EXPIRATION_DAYS = 7;

    public static final int DEFAULT_LINK_CLEANUP_INTERVAL_HOURS = 24;

    public static final String SHORT_LINK_PREFIX = "clk.ru/";

    public static final String FILE_PATH = "links.json";

    public static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private AppConfig() {}
}
