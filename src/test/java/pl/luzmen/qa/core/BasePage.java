package pl.luzmen.qa.core;

import pl.luzmen.qa.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(TestConfig.explicitWaitSeconds())
        );
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> visibleElements(By locator) {
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            return elements.isEmpty() ? null : elements;
        });
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    protected void selectByValue(By locator, String value) {
        new Select(visible(locator)).selectByValue(value);
    }

    protected void waitUntil(ExpectedCondition<?> condition) {
        wait.until(condition);
    }
}
