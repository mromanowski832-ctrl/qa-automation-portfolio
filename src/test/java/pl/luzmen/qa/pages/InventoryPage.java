package pl.luzmen.qa.pages;

import pl.luzmen.qa.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public final class InventoryPage extends BasePage {

    private static final By TITLE = By.cssSelector("span.title");
    private static final By INVENTORY_ITEMS = By.cssSelector(".inventory_item");
    private static final By INVENTORY_PRICES = By.cssSelector(".inventory_item_price");
    private static final By SORT = By.cssSelector("[data-test='product-sort-container']");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    private static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack");
    private static final By REMOVE_BACKPACK = By.id("remove-sauce-labs-backpack");
    private static final By ADD_BIKE_LIGHT = By.id("add-to-cart-sauce-labs-bike-light");
    private static final By REMOVE_BIKE_LIGHT = By.id("remove-sauce-labs-bike-light");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage assertLoaded() {
        waitUntil(ExpectedConditions.urlContains("inventory"));
        waitUntil(ExpectedConditions.textToBe(TITLE, "Products"));
        return this;
    }

    public int productCount() {
        return visibleElements(INVENTORY_ITEMS).size();
    }

    public InventoryPage sortLowToHigh() {
        selectByValue(SORT, "lohi");
        waitUntil(ExpectedConditions.attributeToBe(SORT, "value", "lohi"));
        return this;
    }

    public List<Double> prices() {
        return visibleElements(INVENTORY_PRICES).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .map(price -> price.replace("$", ""))
                .map(Double::parseDouble)
                .toList();
    }

    public InventoryPage addBackpack() {
        click(ADD_BACKPACK);
        visible(REMOVE_BACKPACK);
        return this;
    }

    public InventoryPage addBikeLight() {
        click(ADD_BIKE_LIGHT);
        visible(REMOVE_BIKE_LIGHT);
        return this;
    }

    public InventoryPage removeBackpack() {
        click(REMOVE_BACKPACK);
        visible(ADD_BACKPACK);
        return this;
    }

    public int cartBadgeCount() {
        return Integer.parseInt(text(CART_BADGE));
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver).assertLoaded();
    }
}
