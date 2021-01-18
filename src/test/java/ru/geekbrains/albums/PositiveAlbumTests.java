package ru.geekbrains.albums;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import ru.geekbrains.base.test.BaseTest;
import ru.geekbrains.dto.response.way2.AlbumInfoData;
import ru.geekbrains.dto.response.way1.AlbumResponse;
import ru.geekbrains.dto.response.way2.CommonResponse;
import ru.geekbrains.service.Endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveAlbumTests extends BaseTest {
    private static String imageHash;
    private static String imageHash2;
    private static String imageHash3;
    private static String albumHash;

    @BeforeAll
    static void setUp() {
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
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
                .post(Endpoints.postCreateImage)
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
                .post(Endpoints.postCreateImage)
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
                .formParam("title", albumTitle)
                .formParam("description", albumDescription)
                .formParam("cover", imageHash)
                .expect()
                .body("data.id", is(notNullValue()))
                .when()
                .post(Endpoints.postCreateAlbum)
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
                .get(Endpoints.getDeleteAndUpdateAlbum, albumHash);
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
                .post(Endpoints.postCreateImage)
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    @Order(6)
    void getImageInAlbumTest() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode imageResponse = given()
                .when()
                .get(Endpoints.getDeleteAndUpdateAlbum, albumHash)
                .then()
                .extract()
                .response()
                .as(JsonNode.class);
        CommonResponse<AlbumInfoData> albumInfoData =
                mapper.convertValue(imageResponse, new TypeReference<CommonResponse<AlbumInfoData>>() {
                });
        assertThat(albumInfoData.getData().getImageInfoResponse(), containsInAnyOrder
                (hasProperty("id", is(imageHash2)),
                        hasProperty("id", is(imageHash))));
        assertThat(albumInfoData.getData().getImagesCount(), is(2));
    }

    @Test
    @Order(7)
    void favoriteAlbumTest() {
        given()
                .expect()
                .body("data", is("favorited"))
                .when()
                .post(Endpoints.postFavoriteAlbum, albumHash);

    }

    @Test
    @Order(8)
    void getAccountFavoritesTest() {
        given()
                .expect()
                .body(containsString(albumHash))
                .when()
                .get(Endpoints.getAccountFavorites, accountUrl);
    }

    @Test
    @Order(9)
    void updateAlbumUsingPutTest() {
        given()
                .multiPart("ids[]", imageHash)
                .multiPart("ids[]", imageHash3)
                .formParam("title", updatedAlbumTitle)
                .formParam("description", updatedAlbumDescription)
                .formParam("cover", imageHash3)
                .expect()
                .body("data", is(true))
                .when()
                .put(Endpoints.getDeleteAndUpdateAlbum, albumHash);
    }

    @Test
    @Order(10)
    void checkUpdateAlbumUsingPutTest() {
        //AlbumResponseData album=
        given()
                .expect()
                .body("data.title", is(updatedAlbumTitle))
                .body("data.description", is(updatedAlbumDescription))
                .body("data.cover", is(imageHash3))
                .body(containsString(imageHash3))
                .body(containsString(imageHash))
                .body(not(containsString(imageHash2)))
                .when()
                .get(Endpoints.getDeleteAndUpdateAlbum, albumHash);
        //   .then()
        //   .extract()
        //  .response()
        // .as(AlbumResponseData.class);
        //assertThat(album.getData().getTitle(), is(updatedAlbumTitle));
        // System.out.println(album.getData().getId());
    }

    @Test
    @Order(11)
    void updateAlbumUsingPostTest() {
        updatedAlbumTitle = faker.hobbit().location();
        updatedAlbumDescription = faker.hobbit().thorinsCompany();
        given()
                .multiPart("ids[]", imageHash2)
                .multiPart("ids[]", imageHash3)
                .formParam("title", updatedAlbumTitle)
                .formParam("description", updatedAlbumDescription)
                .formParam("cover", imageHash2)
                .expect()
                .body("data", is(true))
                .when()
                .post(Endpoints.getDeleteAndUpdateAlbum, albumHash);
    }

    @Test
    @Order(12)
    void checkUpdateAlbumUsingPostTest() {
        AlbumResponse album =
                given()
                        // .expect()
                        .when()
                        .get(Endpoints.getDeleteAndUpdateAlbum, albumHash)
                        .then()
                        .extract()
                        .response()
                        .as(AlbumResponse.class);
        assertThat(album.getData().getCover(), is(imageHash2));
        assertThat(album.getData().getTitle(), is(updatedAlbumTitle));
        assertThat(album.getData().getDescription(), is(updatedAlbumDescription));
        assertThat(album.getData().getImages(), containsInAnyOrder(
                hasProperty("id", is(imageHash2)),
                hasProperty("id", is(imageHash3))
        ));
        assertThat(album.getData().getImages(), not(contains(hasProperty("id", is(imageHash2)))));
    }

    @Test
    @Order(13)
    void addImageToAlbumTest() {
        given()
                .multiPart("ids[]", imageHash)
                .expect()
                .body("data", is(true))
                .when()
                .put(Endpoints.putAddImageToAlbum, albumHash);
    }

    @Test
    @Order(14)
    void checkAddingImageToAlbumTest() {
        given()
                .expect()
                .body(containsString(imageHash))
                .when()
                .get(Endpoints.getDeleteAndUpdateAlbum, albumHash);
    }

    @Test
    @Order(15)
    void removeImageFromAlbumTest() {
        given()
                .multiPart("ids[]", imageHash3)
                .expect()
                .body("data", is(true))
                .when()
                .delete(Endpoints.deleteRemoveImageToAlbum, albumHash);
    }

    @Test
    @Order(16)
    void checkRemovingImageFromAlbumTest() {
        given()
                .expect()
                .body(not(containsString(imageHash3)))
                .when()
                .get(Endpoints.getDeleteAndUpdateAlbum, albumHash);
    }

    @Test
    @Order(17)
    void albumDeletionTest() {
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateAlbum, albumHash);
    }

    @Test
    @Order(18)
    void checkAlbumDeletionTest() {
        RestAssured.responseSpecification = negative404ResponseSpec;
        given()
                .when()
                .get(Endpoints.getDeleteAndUpdateAlbum, albumHash);
    }


    @AfterAll
    static void tearDown() {
        RestAssured.responseSpecification = responseSpec;
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash);
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash2);
        given()
                .expect()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageHash3);
    }
}
