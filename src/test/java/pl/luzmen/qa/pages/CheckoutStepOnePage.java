package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        if (!"Checkout: Your Information".equals(text(TITLE))) {
            throw new IllegalStateException("Checkout step one did not load correctly.");
        }
        return this;
    }

    public CheckoutStepTwoPage continueWith(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE);
        return new CheckoutStepTwoPage(driver);
    }
}
