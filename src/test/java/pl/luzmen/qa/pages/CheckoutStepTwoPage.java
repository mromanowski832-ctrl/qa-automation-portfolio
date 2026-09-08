package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class CheckoutStepTwoPage extends BasePage {

    private static final By TITLE = By.cssSelector("span.title");
    private static final By TOTAL = By.cssSelector(".summary_total_label");
    private static final By FINISH = By.id("finish");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepTwoPage assertLoaded() {
        if (!"Checkout: Overview".equals(text(TITLE))) {
            throw new IllegalStateException("Checkout overview did not load correctly.");
        }
        return this;
    }

    public String total() {
        return text(TOTAL);
    }

    public CheckoutCompletePage finish() {
        click(FINISH);
        return new CheckoutCompletePage(driver);
    }
}
