package ru.geekbrains;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageUploadTests extends BaseTest {

     private static String encodedImage;
    private static String imageHash;
    private static String imageHash2;



    @BeforeAll
      static void setUp() {
        byte[] fileContent = getFileContentInBase64();
        encodedImage=Base64.getEncoder().encodeToString(fileContent);
    }


    @Test
    void uploadImageFromFileTest() {
         imageHash=given()
                .headers(headers)
                .multiPart("image", encodedImage)
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
    void uploadImageFromUrlTest() {
        imageHash2=given()
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
    void getImageInfo() {
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


    @AfterAll
    static void tearDown() {
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


    private static byte[] getFileContentInBase64() {
        ClassLoader classLoader= ImageUploadTests.class.getClassLoader();
        File inputFile=new File(Objects.requireNonNull(classLoader.getResource("IMG_20200527_170705.jpg").getFile()));
        byte[] fileContent =new byte[0];
        try {
            fileContent= FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

}
