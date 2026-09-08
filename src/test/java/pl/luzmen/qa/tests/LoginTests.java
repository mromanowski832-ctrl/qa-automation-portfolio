package pl.luzmen.qa.tests;

import pl.luzmen.qa.core.BaseTest;
import pl.luzmen.qa.pages.InventoryPage;
import pl.luzmen.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class LoginTests extends BaseTest {

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"invalid_user", "invalid_password"},
                {STANDARD_USER, "wrong_password"}
        };
    }

    @Test(
            groups = {"smoke", "regression"},
            description = "Valid user can authenticate and reach the inventory page"
    )
    public void validUserCanLogin() {
        InventoryPage inventoryPage = loginPage()
                .open()
                .loginAs(STANDARD_USER, PASSWORD)
                .assertLoaded();

        Assert.assertEquals(inventoryPage.productCount(), 6, "Unexpected inventory size.");
    }

    @Test(
            groups = {"regression"},
            description = "Locked user receives a clear access-denied message"
    )
    public void lockedUserCannotLogin() {
        LoginPage page = loginPage()
                .open()
                .loginExpectingFailure(LOCKED_USER, PASSWORD);

        Assert.assertTrue(
                page.errorMessage().contains("locked out"),
                "Expected locked-out error message."
        );
    }

    @Test(
            dataProvider = "invalidCredentials",
            groups = {"regression"},
            description = "Invalid credentials are rejected"
    )
    public void invalidCredentialsAreRejected(String username, String password) {
        LoginPage page = loginPage()
                .open()
                .loginExpectingFailure(username, password);

        Assert.assertTrue(
                page.errorMessage().contains("Username and password do not match"),
                "Expected invalid-credentials error message."
        );
    }

    @Test(
            groups = {"regression"},
            description = "Authenticated user can log out"
    )
    public void userCanLogout() {
        InventoryPage inventoryPage = loginPage()
                .open()
                .loginAs(STANDARD_USER, PASSWORD)
                .assertLoaded();

        LoginPage loginPage = inventoryPage.logout();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login page should be visible after logout.");
    }
}
