package pl.luzmen.qa.driver;

import pl.luzmen.qa.config.TestConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public final class DriverFactory {

    private static final String BROWSERSTACK_HUB = "https://hub.browserstack.com/wd/hub";

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        WebDriver driver = switch (TestConfig.runMode()) {
            case "local" -> createLocalDriver();
            case "browserstack" -> createBrowserStackDriver();
            default -> throw new IllegalArgumentException(
                    "Unsupported run.mode: " + TestConfig.runMode() + ". Use local or browserstack."
            );
        };

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.pageLoadTimeoutSeconds()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(TestConfig.scriptTimeoutSeconds()));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        try {
            driver.manage().window().maximize();
        } catch (RuntimeException ignored) {
            // Headless and some remote environments may not expose a maximizable native window.
        }

        return driver;
    }

    private static WebDriver createLocalDriver() {
        return switch (TestConfig.browser()) {
            case "chrome" -> new org.openqa.selenium.chrome.ChromeDriver(chromeOptions());
            case "edge" -> new org.openqa.selenium.edge.EdgeDriver(edgeOptions());
            case "firefox" -> new org.openqa.selenium.firefox.FirefoxDriver(firefoxOptions());
            default -> throw unsupportedBrowser();
        };
    }

    private static WebDriver createBrowserStackDriver() {
        TestConfig.requireBrowserStackCredentials();

        MutableCapabilities capabilities = browserCapabilities();
        Map<String, Object> browserStackOptions = new HashMap<>();

        browserStackOptions.put("userName", TestConfig.browserStackUsername());
        browserStackOptions.put("accessKey", TestConfig.browserStackAccessKey());
        browserStackOptions.put("os", "Windows");
        browserStackOptions.put("osVersion", "11");
        browserStackOptions.put("projectName", "LuzMen QA Automation Portfolio");
        browserStackOptions.put("buildName", TestConfig.browserStackBuildName());
        browserStackOptions.put("sessionName", "SauceDemo UI Regression");

        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("bstack:options", browserStackOptions);

        try {
            return new RemoteWebDriver(URI.create(BROWSERSTACK_HUB).toURL(), capabilities);
        } catch (MalformedURLException exception) {
            throw new IllegalStateException("Invalid BrowserStack hub URL.", exception);
        }
    }

    private static MutableCapabilities browserCapabilities() {
        return switch (TestConfig.browser()) {
            case "chrome" -> chromeOptions();
            case "edge" -> edgeOptions();
            case "firefox" -> firefoxOptions();
            default -> throw unsupportedBrowser();
        };
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-notifications");

        if (TestConfig.headless()) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }

        return options;
    }

    private static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-notifications");

        if (TestConfig.headless()) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }

        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        if (TestConfig.headless()) {
            options.addArguments("-headless");
        }

        return options;
    }

    private static IllegalArgumentException unsupportedBrowser() {
        return new IllegalArgumentException(
                "Unsupported browser: " + TestConfig.browser()
                        + ". Supported values: chrome, edge, firefox."
        );
    }
}
