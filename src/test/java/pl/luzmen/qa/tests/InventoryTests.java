package pl.luzmen.qa.tests;

import pl.luzmen.qa.core.BaseTest;
import pl.luzmen.qa.pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class InventoryTests extends BaseTest {

    @Test(
            groups = {"regression"},
            description = "Product prices are sorted from low to high"
    )
    public void productsCanBeSortedLowToHigh() {
        loginAsStandardUser();

        InventoryPage inventoryPage = new InventoryPage(
                pl.luzmen.qa.driver.DriverManager.getDriver()
        ).sortLowToHigh();

        List<Double> actual = inventoryPage.prices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Comparator.naturalOrder());

        Assert.assertEquals(actual, expected, "Products are not sorted by ascending price.");
    }

    @Test(
            groups = {"smoke", "regression"},
            description = "Cart badge accurately reflects add and remove operations"
    )
    public void cartBadgeTracksProductChanges() {
        loginAsStandardUser();

        InventoryPage inventoryPage = new InventoryPage(
                pl.luzmen.qa.driver.DriverManager.getDriver()
        )
                .addBackpack()
                .addBikeLight();

        Assert.assertEquals(inventoryPage.cartBadgeCount(), 2, "Cart badge should show two items.");

        inventoryPage.removeBackpack();

        Assert.assertEquals(inventoryPage.cartBadgeCount(), 1, "Cart badge should show one item.");
    }
}
