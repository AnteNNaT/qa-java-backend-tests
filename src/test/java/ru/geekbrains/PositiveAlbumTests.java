package ru.geekbrains;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveAlbumTests extends BaseTest {
    private static String imageHash;
    private static String imageHash2;
    private static String albumHash;
    private static String responseString;

    @Test
    @Order(1)
    void uploadImageFromUrlForAlbumTest() {
        imageHash=given()
                .headers(headers)
                .multiPart("image", imageNatureUrl)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(2)
    void albumCreationTest() {
        albumHash=given()
                .headers(headers)
                .contentType("multipart/form-data")
               .multiPart("ids[]", imageHash)
               .multiPart("title", albumTitle)
              .multiPart("description",albumDescription)
               .multiPart("cover", imageHash)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/album")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    @Order(3)
    void getAlbumTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.title", is(albumTitle))
                .body("data.cover", is(imageHash))
                .body("data.images[0].id", is(imageHash))
                .body("data.images_count", is (1))
                .body("data.account_url", is(accountUrl))
                .when()
                .get("/album/{albumHash}", albumHash)
                 .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void uploadImageToAlbumTest() {
        imageHash2=given()
                .headers(headers)
                .multiPart("image", imageNatureUrl)
                .multiPart("album",albumHash)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    @Order(5)
    void getImageInAlbumTest() {
        responseString= given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.images_count", is (2))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.images")
        ;
        assertThat(responseString,containsString(imageHash2));
        assertThat(responseString,containsString(imageHash));

    }

    @AfterAll
    static void tearDown() {
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .delete("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .delete("/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .delete("/image/{imageHash}", imageHash2)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
