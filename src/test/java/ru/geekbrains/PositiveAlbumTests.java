package ru.geekbrains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import ru.geekbrains.base.test.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveAlbumTests extends BaseTest {
    private static String imageHash;
    private static String imageHash2;
    private static String imageHash3;
    private static String albumHash;

    @BeforeAll
    static void setUp()  {
        RestAssured.responseSpecification=responseSpec;
    }

    @Test
    @Order(1)
    void uploadImageFromUrlForAlbumTest() {
        imageHash = given()
                .multiPart("image", imageNatureUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(2)
    void uploadImage3FromUrlForAlbumTest() {
        imageHash3 = given()
                .multiPart("image", imageNatureUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }


    @Test
    @Order(3)
    void albumCreationTest() {
        albumHash = given()
                .contentType("multipart/form-data")
                .multiPart("ids[]", imageHash)
                .multiPart("title", albumTitle)
                .multiPart("description", albumDescription)
                .multiPart("cover", imageHash)
                .expect()
                .body("data.id", is(notNullValue()))
                .when()
                .post("/album")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    @Order(4)
    void getAlbumTest() {
        given()
                .expect()
                .body("data.title", is(albumTitle))
                .body("data.cover", is(imageHash))
                .body("data.images[0].id", is(imageHash))
                .body("data.images_count", is(1))
                .body("data.account_url", is(accountUrl))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(5)
    void uploadImageToAlbumTest() {
        imageHash2 = given()
                .multiPart("image", imageNatureUrl)
                .multiPart("album", albumHash)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    @Order(6)
    void getImageInAlbumTest() {
        given()
                .expect()
                .body("data.images_count", is(2))
                .body(containsString(imageHash2))
                .body(containsString(imageHash))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek();

    }

    @Test
    @Order(7)
    void favoriteAlbumTest() {
        given()
                .expect()
                .body("data", is("favorited"))
                .when()
                .post("/album/{albumHash}/favorite", albumHash)
                .prettyPeek();

    }

    @Test
    @Order(8)
    void getAccountFavoritesTest() {
        given()
                .expect()
                .body(containsString(albumHash))
                .when()
                .get("/account/{username}/favorites", accountUrl)
                .prettyPeek();
    }

    @Test
    @Order(9)
    void updateAlbumUsingPutTest() {
        given()
                .multiPart("ids[]", imageHash)
                .multiPart("ids[]", imageHash3)
                .multiPart("title", updatedAlbumTitle)
                .multiPart("description", updatedAlbumDescription)
                .multiPart("cover", imageHash3)
                .expect()
                .body("data", is(true))
                .when()
                .put("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(10)
    void checkUpdateAlbumUsingPutTest() {
        given()
                .expect()
                .body("data.title", is(updatedAlbumTitle))
                .body("data.description", is(updatedAlbumDescription))
                .body("data.cover", is(imageHash3))
                .body(containsString(imageHash3))
                .body(containsString(imageHash))
                .body(not(containsString(imageHash2)))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(11)
    void updateAlbumUsingPostTest() {
        given()
                .multiPart("ids[]", imageHash2)
                .multiPart("ids[]", imageHash3)
                .multiPart("title", updatedAlbumTitle + "1")
                .multiPart("description", updatedAlbumDescription + "1")
                .multiPart("cover", imageHash2)
                .expect()
                .body("data", is(true))
                .when()
                .post("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(12)
    void checkUpdateAlbumUsingPostTest() {
        given()
                .expect()
                .body("data.title", is(updatedAlbumTitle + "1"))
                .body("data.description", is(updatedAlbumDescription + "1"))
                .body("data.cover", is(imageHash2))
                .body(containsString(imageHash3))
                .body(containsString(imageHash2))
                .body(not(containsString(imageHash)))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(13)
    void addImageToAlbumTest() {
        given()
                .multiPart("ids[]", imageHash)
                .expect()
                .body("data", is(true))
                .when()
                .put("/album/{albumHash}/add", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(14)
    void checkAddingImageToAlbumTest() {
        given()
                .expect()
                .body(containsString(imageHash))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(15)
    void removeImageFromAlbumTest() {
        given()
                .multiPart("ids[]", imageHash3)
                .expect()
                .body("data", is(true))
                .when()
                .delete("/album/{albumHash}/remove_images", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(16)
    void checkRemovingImageFromAlbumTest() {
        given()
                .expect()
                .body(not(containsString(imageHash3)))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(17)
    void albumDeletionTest() {
        given()
                .expect()
                .when()
                .delete("/album/{albumHash}", albumHash)
                .prettyPeek();
    }

    @Test
    @Order(18)
    void checkAlbumDeletionTest() {
        RestAssured.responseSpecification=negative404ResponseSpec;
        given()
                .when()
                .get("album/{albumHash}", albumHash)
                .then()
                .log()
                .status();
    }



    @AfterAll
    static void tearDown() {
        RestAssured.responseSpecification=responseSpec;
        given()
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash2)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash3)
                .prettyPeek();
    }
}
