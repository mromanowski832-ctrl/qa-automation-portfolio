package pl.luzmen.qa.tests;

import pl.luzmen.qa.core.BaseTest;
import pl.luzmen.qa.driver.DriverManager;
import pl.luzmen.qa.pages.CartPage;
import pl.luzmen.qa.pages.CheckoutCompletePage;
import pl.luzmen.qa.pages.CheckoutStepTwoPage;
import pl.luzmen.qa.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public final class PurchaseFlowTests extends BaseTest {

    @Test(
            groups = {"ui", "ui-smoke", "ui-regression"},
            description = "Standard user can complete an end-to-end purchase"
    )
    public void userCanCompletePurchase() {
        loginAsStandardUser();

        InventoryPage inventoryPage = new InventoryPage(DriverManager.getDriver())
                .addBackpack();

        Assert.assertEquals(inventoryPage.cartBadgeCount(), 1, "Cart badge should show one item.");

        CartPage cartPage = inventoryPage
                .openCart()
                .assertLoaded();

        Assert.assertEquals(cartPage.itemCount(), 1, "Cart should contain exactly one item.");
        Assert.assertTrue(
                cartPage.itemNames().contains("Sauce Labs Backpack"),
                "Expected product is missing from the cart."
        );

        CheckoutStepTwoPage overview = cartPage
                .checkout()
                .assertLoaded()
                .continueWith("Michał", "Romanowski", "66-600")
                .assertLoaded();

        Assert.assertTrue(overview.total().startsWith("Total: $"), "Checkout total is not displayed.");

        CheckoutCompletePage completePage = overview
                .finish()
                .assertLoaded();

        Assert.assertEquals(
                completePage.confirmationMessage(),
                "Thank you for your order!",
                "Unexpected checkout confirmation."
        );
    }
}
