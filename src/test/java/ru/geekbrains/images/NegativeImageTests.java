package ru.geekbrains.images;

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
        encodedReallyNotImage = getFileContentInBase64String(reallyNotImage);
        RestAssured.responseSpecification = negativeResponseSpec;

        //requestWithoutAuthSpec=new RequestSpecBuilder()
        //        .setAccept(ContentType.JSON)
        //        .log(LogDetail.ALL)
        //         .build();
    }

    @Test
    void uploadTooLargeImageTest() {
        given()
                .spec(createRequestSpec(largeImageUrl))
                .expect()
                .body("data.error", is("File is over the size limit"))
                .when()
                .post(Endpoints.postCreateImage);
    }

    @Test
    void uploadTextFileInsteadOfImageTest() {
        given()
                .spec(createRequestSpec(textFileUrl))
                .expect()
                .body("data.error.type", is("ImgurException"))
                .body("data.error.code", is(1003))
                .when()
                .post(Endpoints.postCreateImage);
    }

    @Test
    void uploadImageWithInvalidUrlTest() {
        given()
                .spec(createRequestSpec(invalidUrl))
                .expect()
                .body("data.error", is("Invalid URL (" + invalidUrl + ")"))
                .when()
                .post(Endpoints.postCreateImage);
    }

    @Test
    void uploadReallyNotImageFromFileTest() {
        given()
                .spec(createRequestSpec(encodedReallyNotImage))
                .when()
                .post(Endpoints.postCreateImage);
    }

    @Test
    void uploadEmptyImageTest() {
        given()
                .spec(createRequestSpec(""))
                .when()
                .post(Endpoints.postCreateImage);
    }
    /*
    @Test
    void uploadImageWithoutAuthTest() {
        given()
                .spec(requestWithoutAuthSpec)
                .multiPart("image",imageNatureUrl)
                .expect()
                .when()
                .post("/image");
    }

     */


}
