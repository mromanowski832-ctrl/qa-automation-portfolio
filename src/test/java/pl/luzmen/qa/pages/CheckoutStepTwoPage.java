package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class CheckoutStepTwoPage extends BasePage {

    private static final By TITLE = By.cssSelector("span.title");
    private static final By TOTAL = By.cssSelector(".summary_total_label");
    private static final By FINISH = By.id("finish");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepTwoPage assertLoaded() {
        waitUntil(ExpectedConditions.urlContains("checkout-step-two"));
        waitUntil(ExpectedConditions.textToBe(TITLE, "Checkout: Overview"));
        return this;
    }

    public String total() {
        return text(TOTAL);
    }

    public CheckoutCompletePage finish() {
        jsClick(FINISH);
        waitUntil(ExpectedConditions.urlContains("checkout-complete"));
        return new CheckoutCompletePage(driver);
    }
}
