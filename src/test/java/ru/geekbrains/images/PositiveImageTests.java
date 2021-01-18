package ru.geekbrains.images;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import ru.geekbrains.base.test.BaseTest;
import ru.geekbrains.dto.response.way2.AnimatedImageInfoData;
import ru.geekbrains.dto.response.way2.CommonResponse;
import ru.geekbrains.dto.response.way2.ImageInfoData;
import ru.geekbrains.dto.response.way1.ImageResponse;
import ru.geekbrains.service.Endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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
        //RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

    @Test
    @Order(1)
    void uploadPngImageFromFileTest() {
        ImageResponse imageResponse = given()
                .spec(createRequestSpec(encodedImage))
                // .multiPart("image", encodedImage)
                .when()
                .post(Endpoints.postCreateImage)
                .then()
                .extract()
                .response()
                .as(ImageResponse.class);
        imageHash = imageResponse.getData().getId();
        assertThat(imageResponse.getData().getId(), is(notNullValue()));
        assertThat(imageResponse.getData().getType(), is("image/png"));

    }

    @Test
    @Order(2)
    void uploadJpegImageFromUrlTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode imageResponse = given()
                .spec(createRequestSpec(imageNatureUrl))
                //.multiPart("image", imageNatureUrl)
                .when()
                .post(Endpoints.postCreateImage)
                .then()
                .extract()
                .response()
                .as(JsonNode.class);
        CommonResponse<ImageInfoData> imageInfoData =
                mapper.convertValue(imageResponse, new TypeReference<CommonResponse<ImageInfoData>>() {
                });
        imageHash2 = imageInfoData.getData().getId();
        assertThat(imageInfoData.getData().getId(), is(notNullValue()));
        assertThat(imageInfoData.getData().getType(), is("image/jpeg"));
    }

    @Test
    @Order(3)
    void uploadNotAnimatedGifImageFromUrlTest() {
        ImageResponse imageResponse = given()
                .spec(createRequestSpec(imageGifUrl))
                //.multiPart("image", imageGifUrl)
                .when()
                .post(Endpoints.postCreateImage)
                .then()
                .extract()
                .response()
                .as(ImageResponse.class);
        imageHash3 = imageResponse.getData().getId();
        assertThat(imageResponse.getData().getId(), is(notNullValue()));
        assertThat(imageResponse.getData().getType(), is("image/gif"));
        assertThat(imageResponse.getData().getAnimated(), is(false));
    }

    @Test
    @Order(4)
    void uploadAnimatedGifImageFromUrlTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode imageResponse = given()
                .spec(createRequestSpec(imageAnimatedGifUrl))
                //.multiPart("image", imageAnimatedGifUrl)
                .when()
                .post(Endpoints.postCreateImage)
                .then()
                .extract()
                .response()
                .as(JsonNode.class);
        CommonResponse<AnimatedImageInfoData> imageInfoData =
                mapper.convertValue(imageResponse, new TypeReference<CommonResponse<AnimatedImageInfoData>>() {
                });
        imageHash4 = imageInfoData.getData().getId();
        assertThat(imageInfoData.getData().getId(), is(notNullValue()));
        assertThat(imageInfoData.getData().getType(), is("image/gif"));
        assertThat(imageInfoData.getData().getAnimated(), is(true));

    }

    @Test
    @Order(5)
    void upload1x1PixelImageFromFileTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode imageResponse = given()
                .spec(createRequestSpec(encodedSmallImage))
                //.multiPart("image", encodedSmallImage)
                .when()
                .post(Endpoints.postCreateImage)
                .then()
                .extract()
                .response()
                .as(JsonNode.class);
        CommonResponse<ImageInfoData> imageInfoData =
                mapper.convertValue(imageResponse, new TypeReference<CommonResponse<ImageInfoData>>() {
                });
        imageHash5 = imageInfoData.getData().getId();
        assertThat(imageHash5, is(notNullValue()));
        assertThat(imageInfoData.getData().getType(), is("image/jpeg"));
    }

    @Test
    @Order(6)
    void getImageTest() {
        given()
                .spec(requestSpec)
                .when()
                .get(Endpoints.getDeleteAndUpdateImage, imageHash2);
    }

    @Test
    @Order(7)
    void updateImageInfoTest() {
        given()
                .spec(requestSpec)
                .multiPart("title", imageTitle)
                .multiPart("description", imageDescription)
                .expect()
                .body("data", is(true))
                .when()
                .post(Endpoints.getDeleteAndUpdateImage, imageHash);
    }

    @Test
    @Order(8)
    void getImageUpdatedInfoTest() {
        given()
                .spec(requestSpec)
                .expect()
                .body("data.title", is(imageTitle))
                .body("data.description", is(imageDescription))
                .when()
                .get(Endpoints.getDeleteAndUpdateImage, imageHash);
    }

    @Test
    @Order(9)
    void favoriteImageTest() {
        given()
                .spec(requestSpec)
                .expect()
                .body("data", is("favorited"))
                .when()
                .post(Endpoints.postFavoriteImage, imageHash2);
    }

    @Test
    @Order(10)
    void getAccountFavoritesTest() {
        given()
                .spec(requestSpec)
                .expect()
                .body(containsString(imageHash2))
                .when()
                .get(Endpoints.getAccountFavorites, accountUrl);
    }

    @Test
    @Order(11)
    void imageDeletionTest() {
        given()
                .spec(requestSpec)
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash);
    }

    @Test
    @Order(12)
    void checkImageDeletionTest() {
        RestAssured.responseSpecification = negative404ResponseSpec;
        given()
                .spec(requestSpec)
                .when()
                .get(Endpoints.getDeleteAndUpdateImage, imageHash);
    }

    @AfterAll
    static void tearDown() {
        RestAssured.responseSpecification = responseSpec;
        given()
                .spec(requestSpec)
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash2);
        given()
                .when()
                .spec(requestSpec)
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash3);
        given()
                .when()
                .spec(requestSpec)
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash4);
        given()
                .when()
                .spec(requestSpec)
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash5);
    }

}
