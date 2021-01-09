package ru.geekbrains;

import org.junit.jupiter.api.Test;
import ru.geekbrains.base.test.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class NegativeImageTests extends BaseTest {

    @Test
    void uploadTooLargeImageTest() {
        given()
                .headers(headers)
                .multiPart("image", largeImageUrl)
                .expect()
                .body("data.error", is("File is over the size limit"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(negativeResponseSpec);
    }

    @Test
    void uploadTextFileInsteadOfImageTest() {
        given()
                .headers(headers)
                .multiPart("image", textFileUrl)
                .expect()
                .body("data.error.type", is("ImgurException"))
                .body("data.error.code", is(1003))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(negativeResponseSpec);
    }

    @Test
    void uploadImageWithInvalidUrlTest() {
        given()
                .headers(headers)
                .multiPart("image", "?" + imageNatureUrl)
                .expect()
                .body("data.error", is("Invalid URL (" + "?" + imageNatureUrl + ")"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(negativeResponseSpec);
    }
}
