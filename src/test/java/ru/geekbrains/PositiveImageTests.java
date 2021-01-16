package ru.geekbrains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import ru.geekbrains.base.test.BaseTest;
import ru.geekbrains.service.Endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveImageTests extends BaseTest {

    private static String encodedImage;
    private static String encodedSmallImage;
    private static String imageHash;
    private static String imageHash2;
    private static String imageHash3;
    private static String imageHash4;
    private static String imageHash5;

    @BeforeAll
    static void setUp() {
        encodedImage = getFileContentInBase64String(imageFileName);
        encodedSmallImage = getFileContentInBase64String(smallImageFile);
        RestAssured.responseSpecification = responseSpec;
    }

    @Test
    @Order(1)
    void uploadPngImageFromFileTest() {
        imageHash = given()
                .multiPart("image", encodedImage)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/png"))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(2)
    void uploadJpegImageFromUrlTest() {
        imageHash2 = given()
                .multiPart("image", imageNatureUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(3)
    void uploadNotAnimatedGifImageFromUrlTest() {
        imageHash3 = given()
                .multiPart("image", imageGifUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
                .body("data.animated", is(false))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(4)
    void uploadAnimatedGifImageFromUrlTest() {
        imageHash4 = given()
                .multiPart("image", imageAnimatedGifUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
                .body("data.animated", is(true))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(5)
    void upload1x1PixelImageFromFileTest() {
        imageHash5 = given()
                .multiPart("image", encodedSmallImage)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(6)
    void getImageTest() {
        given()
                .expect()
                .when()
                .get(Endpoints.getDeleteAndUpdateImage, imageHash2)
                //.prettyPeek()
                .then()
                .log()
                .status();
    }

    @Test
    @Order(7)
    void updateImageInfoTest() {
        given()
                .multiPart("title", imageTitle)
                .multiPart("description", imageDescription)
                .expect()
                .body("data", is(true))
                .when()
                .post(Endpoints.getDeleteAndUpdateImage, imageHash)
                .then()
                .log()
                .status();
    }

    @Test
    @Order(8)
    void getImageUpdatedInfoTest() {
        given()
                .expect()
                .body("data.title", is(imageTitle))
                .body("data.description", is(imageDescription))
                .when()
                .get(Endpoints.getDeleteAndUpdateImage, imageHash)
                .then()
                .log()
                .status();
    }

    @Test
    @Order(9)
    void favoriteImageTest() {
        given()
                .expect()
                .body("data", is("favorited"))
                .when()
                .post(Endpoints.postFavoriteImage, imageHash2)
                .then()
                .log()
                .status();
    }

    @Test
    @Order(10)
    void getAccountFavoritesTest() {
        given()
                .expect()
                .body(containsString(imageHash2))
                .when()
                .get(Endpoints.getAccountFavorites, accountUrl)
                .then()
                .log()
                .status();
    }

    @Test
    @Order(11)
    void imageDeletionTest() {
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash)
                .prettyPeek()
                .then()
                .log()
                .status();
    }

    @Test
    @Order(12)
    void checkImageDeletionTest() {
        RestAssured.responseSpecification = negative404ResponseSpec;
        given()
                .when()
                .get(Endpoints.getDeleteAndUpdateImage, imageHash)
                .then()
                .log()
                .status();
    }

    @AfterAll
    static void tearDown() {
        RestAssured.responseSpecification = responseSpec;
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash2)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash3)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash4)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash5)
                .prettyPeek();
    }

}
