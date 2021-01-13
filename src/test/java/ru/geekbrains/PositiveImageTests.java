package ru.geekbrains;

import io.restassured.RestAssured;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import ru.geekbrains.base.test.BaseTest;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

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
        // byte[] fileContent = FileUtils.readFileToByteArray(imageFileName);
        encodedImage = getFileContentInBase64String(imageFileName);
        encodedSmallImage = getFileContentInBase64String(smallImageFile);
        // byte[] fileContent = getFileContentInBase64(imageFileName);
        // encodedImage = Base64.getEncoder().encodeToString(fileContent);
        //byte[] emptyFileContent = getFileContentInBase64(emptyImageFileName);
        //encodedEmptyimage=Base64.getEncoder().encodeToString(emptyFileContent);
        //byte[] fileContent2 = FileUtils.readFileToByteArray(smallImageFile);
        //encodedSmallImage = Base64.getEncoder().encodeToString(fileContent2);

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
    void uploadJpegImageFromUrlTest() {
        imageHash2 = given()
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
    void uploadNotAnimatedGifImageFromUrlTest() {
        imageHash3 = given()
                .multiPart("image", imageGifUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
                .body("data.animated", is(false))
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
    @Order(4)
    void uploadAnimatedGifImageFromUrlTest() {
        imageHash4 = given()
                .multiPart("image", imageAnimatedGifUrl)
                .expect()
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
                .body("data.animated", is(true))
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
    @Order(5)
    void upload1x1PixelImageFromFileTest() {
        imageHash5 = given()
                .multiPart("image", encodedSmallImage)
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
    void getImageTest() {
        given()
                .expect()
                .when()
                .get("/image/{Id}", imageHash2)
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
                .post("/image/{imageHash}", imageHash)
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
                .get("/image/{Id}", imageHash)
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
                .post("/image/{imageHash2}/favorite", imageHash2)
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
                .get("/account/{username}/favorites", accountUrl)
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
                .delete("/image/{imageHash}", imageHash)
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
                .get("/image/{imageHash}", imageHash)
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
                .delete("/image/{imageHash}", imageHash2)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash3)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash4)
                .prettyPeek();
        given()
                .expect()
                .when()
                .delete("/image/{imageHash}", imageHash5)
                .prettyPeek();
    }


/*
    private static byte[] getFileContentInBase64(String imageFileName) {
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

 */

}
