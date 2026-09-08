package pl.luzmen.qa.core;

import pl.luzmen.qa.driver.DriverFactory;
import pl.luzmen.qa.driver.DriverManager;
import pl.luzmen.qa.listeners.TestListener;
import pl.luzmen.qa.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected static final String STANDARD_USER = "standard_user";
    protected static final String LOCKED_USER = "locked_out_user";
    protected static final String PASSWORD = "secret_sauce";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.setDriver(DriverFactory.createDriver());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected LoginPage loginPage() {
        return new LoginPage(DriverManager.getDriver());
    }

    protected void loginAsStandardUser() {
        loginPage()
                .open()
                .loginAs(STANDARD_USER, PASSWORD)
                .assertLoaded();
    }
}
