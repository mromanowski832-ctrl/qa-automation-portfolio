package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public final class CartPage extends BasePage {

    private static final By TITLE = By.cssSelector("span.title");
    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By ITEM_NAMES = By.cssSelector(".inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage assertLoaded() {
        waitUntil(ExpectedConditions.urlContains("cart"));
        waitUntil(ExpectedConditions.textToBe(TITLE, "Your Cart"));
        return this;
    }

    public int itemCount() {
        return visibleElements(CART_ITEMS).size();
    }

    public List<String> itemNames() {
        return visibleElements(ITEM_NAMES).stream()
                .map(element -> element.getText().trim())
                .toList();
    }

    public CheckoutStepOnePage checkout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutStepOnePage(driver);
    }
}
