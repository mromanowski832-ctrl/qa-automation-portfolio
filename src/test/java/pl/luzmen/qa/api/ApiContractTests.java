package pl.luzmen.qa.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public final class ApiContractTests {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test(
            groups = {"api", "smoke", "regression"},
            description = "Single post endpoint returns the expected HTTP and JSON contract"
    )
    public void singlePostContractIsStable() {
        given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", notNullValue())
                .body("body", notNullValue());
    }

    @Test(
            groups = {"api", "regression"},
            description = "Post filtering returns a non-empty collection for a known user"
    )
    public void postsCanBeFilteredByUser() {
        given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .queryParam("userId", 1)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("[0].userId", equalTo(1));
    }
}
