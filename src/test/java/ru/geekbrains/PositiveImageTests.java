package ru.geekbrains;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import ru.geekbrains.base.test.BaseTest;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveImageTests extends BaseTest {

    private static String encodedImage;
    private static String imageHash;
    private static String imageHash2;
    private static String imageHash3;

    @BeforeAll
    static void setUp() {
        byte[] fileContent = getFileContentInBase64();
        encodedImage = Base64.getEncoder().encodeToString(fileContent);
    }

    @Test
    @Order(1)
    void uploadPngImageFromFileTest() {
        imageHash = given()
                .headers(headers)
                .multiPart("image", encodedImage)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/png"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(responseSpec)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(2)
    void uploadJpegImageFromUrlTest() {
        imageHash2 = given()
                .headers(headers)
                .multiPart("image", imageNatureUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/jpeg"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(responseSpec)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(3)
    void uploadGifImageFromUrlTest() {
        imageHash3 = given()
                .headers(headers)
                .multiPart("image", imageGifUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .spec(responseSpec)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

    }

    @Test
    @Order(4)
    void getImageTest() {
        given()
                .headers(headers)
                .expect()
                .when()
                .get("/image/{Id}", imageHash2)
                //.prettyPeek()
                .then()
                .spec(responseSpec)
                .log()
                .status();
    }

    @Test
    @Order(5)
    void updateImageInfoTest() {
        given()
                .headers(headers)
                .multiPart("title", imageTitle)
                .multiPart("description", imageDescription)
                .expect()
                .body("data", is(true))
                .when()
                .post("/image/{imageHash}", imageHash)
                .then()
                .spec(responseSpec)
                .log()
                .status();
    }

    @Test
    @Order(6)
    void getImageUpdatedInfoTest() {
        given()
                .headers(headers)
                .expect()
                .body("data.title", is(imageTitle))
                .body("data.description", is(imageDescription))
                .when()
                .get("/image/{Id}", imageHash)
                .then()
                .spec(responseSpec)
                .log()
                .status();
    }

    @Test
    @Order(7)
    void favoriteImageTest() {
        given()
                .headers(headers)
                .expect()
                .body("data", is("favorited"))
                .when()
                .post("/image/{imageHash2}/favorite", imageHash2)
                .then()
                .spec(responseSpec)
                .log()
                .status();
    }

    @Test
    @Order(8)
    void getAccountFavoritesTest() {
        given()
                .headers(headers)
                .expect()
                .body(containsString(imageHash2))
                .when()
                .get("/account/{username}/favorites", accountUrl)
                .then()
                .spec(responseSpec)
                .log()
                .status();
    }

    @Test
    @Order(9)
    void imageDeletionTest() {
        given()
                .headers("Authorization", token)
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash)
                .prettyPeek()
                .then()
                .spec(responseSpec)
                .log()
                .status();
    }

    @Test
    @Order(10)
    void checkImageDeletionTest() {
        given()
                .headers(headers)
                .when()
                .get("/image/{Id}", imageHash)
                .then()
                .statusCode(404)
                .log()
                .status();
    }

    @AfterAll
    static void tearDown() {
        given()
                .headers("Authorization", token)
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash2)
                .prettyPeek()
                .then()
                .spec(responseSpec);
        given()
                .headers("Authorization", token)
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash3)
                .prettyPeek()
                .then()
                .spec(responseSpec);
    }


    private static byte[] getFileContentInBase64() {
        ClassLoader classLoader = PositiveImageTests.class.getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource(imageFileName).getFile()));
        byte[] fileContent = new byte[0];
        try {
            fileContent = FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

}
