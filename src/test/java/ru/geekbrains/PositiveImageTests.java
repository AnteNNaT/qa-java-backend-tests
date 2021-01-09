package ru.geekbrains;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveImageTests extends BaseTest {

    private static String encodedImage;
    private static String imageHash;
    private static String imageHash2;
    private static String imageHash3;
    private static String responseFavoritesString;

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
                .body("success", is(true))
                .body("status", is(200))
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/png"))
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
    void uploadJpegImageFromUrlTest() {
        imageHash2 = given()
                .headers(headers)
                .multiPart("image", imageNatureUrl)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
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
    @Order(3)
    void uploadGifImageFromUrlTest() {
        imageHash3 = given()
                .headers(headers)
                .multiPart("image", imageGifUrl)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
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
    @Order(4)
    void getImageTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .get("/image/{Id}", imageHash2)
                //.prettyPeek()
                .then()
                .statusCode(200)
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
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is(true))
                .when()
                .post("/image/{imageHash}", imageHash)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .log()
                .status();
    }

    @Test
    @Order(6)
    void getImageUpdatedInfoTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.title", is(imageTitle))
                .body("data.description", is(imageDescription))
                .when()
                .get("/image/{Id}", imageHash)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .log()
                .status();
    }

    @Test
    @Order(7)
    void favoriteImageTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is("favorited"))
                .when()
                .post("/image/{imageHash2}/favorite", imageHash2)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .log()
                .status();
    }

    @Test
    @Order(8)
    void getAccountFavoritesTest() {
        responseFavoritesString = given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .get("/account/{username}/favorites", accountUrl)
                //.prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data");
        assertThat(responseFavoritesString, containsString(imageHash2));
    }

    @Test
    @Order(9)
    void imageDeletionTest() {
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
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .delete("/image/{imageHash}", imageHash2)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .delete("/image/{imageHash}", imageHash3)
                .prettyPeek()
                .then()
                .statusCode(200);
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
