package ru.geekbrains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.geekbrains.base.test.BaseTest;
import ru.geekbrains.service.Endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class NegativeImageTests extends BaseTest {


    private static String encodedReallyNotImage;
    //private static RequestSpecification requestWithoutAuthSpec;

    @BeforeAll
    static void setUp() {
        encodedReallyNotImage=getFileContentInBase64String(reallyNotImage);
        RestAssured.responseSpecification=negativeResponseSpec;

        //requestWithoutAuthSpec=new RequestSpecBuilder()
        //        .setAccept(ContentType.JSON)
        //        .log(LogDetail.ALL)
       //         .build();
    }

    @Test
    void uploadTooLargeImageTest() {
        given()
                .multiPart("image", largeImageUrl)
                .expect()
                .body("data.error", is("File is over the size limit"))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek();
    }

    @Test
    void uploadTextFileInsteadOfImageTest() {
        given()
                .multiPart("image", textFileUrl)
                .expect()
                .body("data.error.type", is("ImgurException"))
                .body("data.error.code", is(1003))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek();
    }

    @Test
    void uploadImageWithInvalidUrlTest() {
        given()
                .multiPart("image", "?" + imageNatureUrl)
                .expect()
                .body("data.error", is("Invalid URL (" + "?" + imageNatureUrl + ")"))
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek();
    }

    @Test
    void uploadReallyNotImageFromFileTest() {
        given()
                .multiPart("image", encodedReallyNotImage)
                .when()
                .post("/image")
                .prettyPeek();
    }
    @Test
    void uploadEmptyImageTest() {
        given()
                .multiPart("image", "")
                .expect()
                .when()
                .post(Endpoints.postCreateImage)
                .prettyPeek();
    }
    /*
    @Test
    void uploadImageWithoutAuthTest() {
        given()
                .spec(requestWithoutAuthSpec)
                .multiPart("image",imageNatureUrl)
                .expect()
                .when()
                .post("/image")
                .prettyPeek();
    }

     */
}
