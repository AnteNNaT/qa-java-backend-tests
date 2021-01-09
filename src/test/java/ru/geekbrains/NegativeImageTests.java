package ru.geekbrains;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class NegativeImageTests extends BaseTest{

    @Test
    void uploadTooLargeImageTest() {
            given()
                    .headers(headers)
                    .multiPart("image", largeImageUrl)
                    .expect()
                    .body("success", is(false))
                    .body("status", is(400))
                    .body("data.error", is("File is over the size limit"))
                    .when()
                    .post("/image")
                    .prettyPeek()
                    .then()
                    .statusCode(400);
    }

    @Test
    void uploadTextFileInsteadOfImageTest() {
        given()
                .headers(headers)
                .multiPart("image", textFileUrl)
                .expect()
                .body("success", is(false))
                .body("status", is(400))
                .body("data.error.type", is("ImgurException"))
                .body("data.error.code", is(1003))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @Test
    void uploadImageWithInvalidUrlTest() {
        given()
                .headers(headers)
                .multiPart("image", "?"+imageNatureUrl)
                .expect()
                .body("success", is(false))
                .body("status", is(400))
                .body("data.error", is("Invalid URL ("+"?"+imageNatureUrl+")"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(400);
    }
}
