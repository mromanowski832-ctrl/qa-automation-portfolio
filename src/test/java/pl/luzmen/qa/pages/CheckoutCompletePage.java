package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class CheckoutCompletePage extends BasePage {

    private static final By TITLE = By.cssSelector("span.title");
    private static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage assertLoaded() {
        if (!"Checkout: Complete!".equals(text(TITLE))) {
            throw new IllegalStateException("Checkout completion page did not load correctly.");
        }
        return this;
    }

    public String confirmationMessage() {
        return text(COMPLETE_HEADER);
    }
}
