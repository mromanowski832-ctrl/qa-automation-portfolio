package pl.luzmen.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public final class TestConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private TestConfig() {
    }

    public static String baseUrl() {
        return get("base.url", "https://www.saucedemo.com/");
    }

    public static String browser() {
        return get("browser", "edge").toLowerCase(Locale.ROOT);
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "false"));
    }

    public static String runMode() {
        return get("run.mode", "local").toLowerCase(Locale.ROOT);
    }

    public static int explicitWaitSeconds() {
        return getPositiveInt("explicit.wait.seconds", 10);
    }

    public static int pageLoadTimeoutSeconds() {
        return getPositiveInt("page.load.timeout.seconds", 30);
    }

    public static int scriptTimeoutSeconds() {
        return getPositiveInt("script.timeout.seconds", 20);
    }

    public static String browserStackUsername() {
        return firstNonBlank(
                System.getProperty("browserstack.username"),
                System.getenv("BROWSERSTACK_USERNAME")
        );
    }

    public static String browserStackAccessKey() {
        return firstNonBlank(
                System.getProperty("browserstack.accessKey"),
                System.getenv("BROWSERSTACK_ACCESS_KEY")
        );
    }

    public static String browserStackBuildName() {
        String explicit = firstNonBlank(
                System.getProperty("browserstack.build"),
                System.getenv("BROWSERSTACK_BUILD")
        );

        if (explicit != null) {
            return explicit;
        }

        String githubRunNumber = System.getenv("GITHUB_RUN_NUMBER");
        if (githubRunNumber != null && !githubRunNumber.isBlank()) {
            return "GitHub Actions #" + githubRunNumber;
        }

        return "Local portfolio run";
    }

    public static void requireBrowserStackCredentials() {
        if (browserStackUsername() == null || browserStackAccessKey() == null) {
            throw new IllegalStateException(
                    "BrowserStack mode requires BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY."
            );
        }
    }

    private static String get(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String envKey = key.toUpperCase(Locale.ROOT).replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }

        String propertyValue = PROPERTIES.getProperty(key);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue.trim();
        }

        return defaultValue;
    }

    private static int getPositiveInt(String key, int defaultValue) {
        String raw = get(key, Integer.toString(defaultValue));

        try {
            int parsed = Integer.parseInt(raw);
            if (parsed <= 0) {
                throw new IllegalArgumentException(key + " must be greater than zero.");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(key + " must be an integer. Current value: " + raw, exception);
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Missing test configuration file: " + CONFIG_FILE);
            }
            properties.load(input);
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load " + CONFIG_FILE, exception);
        }
    }
}
