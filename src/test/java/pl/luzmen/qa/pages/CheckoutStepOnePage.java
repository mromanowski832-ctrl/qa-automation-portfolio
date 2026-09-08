package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class CheckoutStepOnePage extends BasePage {

    private static final By TITLE = By.cssSelector("span.title");
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepOnePage assertLoaded() {
        waitUntil(ExpectedConditions.urlContains("checkout-step-one"));
        waitUntil(ExpectedConditions.textToBe(TITLE, "Checkout: Your Information"));
        return this;
    }

    public CheckoutStepTwoPage continueWith(String firstName, String lastName, String postalCode) {
        setReactInputValue(FIRST_NAME, firstName);
        setReactInputValue(LAST_NAME, lastName);
        setReactInputValue(POSTAL_CODE, postalCode);

        jsClick(CONTINUE);
        waitUntil(ExpectedConditions.urlContains("checkout-step-two"));

        return new CheckoutStepTwoPage(driver);
    }

    private void setReactInputValue(By locator, String value) {
        WebElement element = visible(locator);

        ((JavascriptExecutor) driver).executeScript(
                "const input = arguments[0];"
                        + "const value = arguments[1];"
                        + "const setter = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value').set;"
                        + "setter.call(input, value);"
                        + "input.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "input.dispatchEvent(new Event('change', { bubbles: true }));",
                element,
                value
        );

        waitUntil(ExpectedConditions.attributeToBe(locator, "value", value));
    }
}
