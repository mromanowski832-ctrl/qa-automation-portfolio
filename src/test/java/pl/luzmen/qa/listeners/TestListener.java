package pl.luzmen.qa.listeners;

import io.qameta.allure.Allure;
import pl.luzmen.qa.config.TestConfig;
import pl.luzmen.qa.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TestListener implements ITestListener {

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Override
    public void onTestSuccess(ITestResult result) {
        markBrowserStackSession("passed", "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        captureFailureEvidence(result);

        String reason = result.getThrowable() == null
                ? "Test failed"
                : result.getThrowable().getClass().getSimpleName() + ": " + result.getThrowable().getMessage();

        markBrowserStackSession("failed", reason);
    }

    private void captureFailureEvidence(ITestResult result) {
        if (!DriverManager.hasDriver()) {
            return;
        }

        WebDriver driver = DriverManager.getDriver();
        if (!(driver instanceof TakesScreenshot screenshotDriver)) {
            return;
        }

        String safeName = result.getMethod().getMethodName().replaceAll("[^a-zA-Z0-9._-]", "_");
        String fileName = safeName + "-" + TIMESTAMP.format(LocalDateTime.now()) + ".png";
        Path destination = Path.of("screenshots", fileName);

        try {
            byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            Files.createDirectories(destination.getParent());
            Files.write(destination, screenshot);
            attachToAllure(safeName, screenshot);
        } catch (IOException | RuntimeException exception) {
            System.err.println("Unable to save failure screenshot: " + exception.getMessage());
        }
    }

    private void attachToAllure(String testName, byte[] screenshot) {
        try {
            Allure.addAttachment(
                    "Failure screenshot - " + testName,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        } catch (RuntimeException exception) {
            System.err.println("Unable to attach screenshot to Allure: " + exception.getMessage());
        }
    }

    private void markBrowserStackSession(String status, String reason) {
        if (!"browserstack".equals(TestConfig.runMode()) || !DriverManager.hasDriver()) {
            return;
        }

        WebDriver driver = DriverManager.getDriver();
        if (!(driver instanceof JavascriptExecutor executor)) {
            return;
        }

        String safeReason = sanitize(reason);
        String script = "browserstack_executor: {"
                + "\"action\":\"setSessionStatus\","
                + "\"arguments\":{"
                + "\"status\":\"" + status + "\","
                + "\"reason\":\"" + safeReason + "\""
                + "}}";

        try {
            executor.executeScript(script);
        } catch (RuntimeException exception) {
            System.err.println("Unable to update BrowserStack session status: " + exception.getMessage());
        }
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", " ")
                .replace("\n", " ");
    }
}
