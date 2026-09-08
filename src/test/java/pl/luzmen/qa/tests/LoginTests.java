package pl.luzmen.qa.tests;

import pl.luzmen.qa.core.BaseTest;
import pl.luzmen.qa.pages.InventoryPage;
import pl.luzmen.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public final class LoginTests extends BaseTest {

    @Test(description = "Valid user can authenticate and reach the inventory page")
    public void validUserCanLogin() {
        InventoryPage inventoryPage = loginPage()
                .open()
                .loginAs(STANDARD_USER, PASSWORD)
                .assertLoaded();

        Assert.assertEquals(inventoryPage.productCount(), 6, "Unexpected inventory size.");
    }

    @Test(description = "Locked user receives a clear access-denied message")
    public void lockedUserCannotLogin() {
        LoginPage page = loginPage()
                .open()
                .loginExpectingFailure(LOCKED_USER, PASSWORD);

        Assert.assertTrue(
                page.errorMessage().contains("locked out"),
                "Expected locked-out error message."
        );
    }

    @Test(description = "Invalid credentials are rejected")
    public void invalidCredentialsAreRejected() {
        LoginPage page = loginPage()
                .open()
                .loginExpectingFailure("invalid_user", "invalid_password");

        Assert.assertTrue(
                page.errorMessage().contains("Username and password do not match"),
                "Expected invalid-credentials error message."
        );
    }

    @Test(description = "Authenticated user can log out")
    public void userCanLogout() {
        InventoryPage inventoryPage = loginPage()
                .open()
                .loginAs(STANDARD_USER, PASSWORD)
                .assertLoaded();

        LoginPage loginPage = inventoryPage.logout();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login page should be visible after logout.");
    }
}
