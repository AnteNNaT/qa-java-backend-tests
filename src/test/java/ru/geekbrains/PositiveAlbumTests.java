package ru.geekbrains;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PositiveAlbumTests extends BaseTest {
    private static String imageHash;
    private static String imageHash2;
    private static String imageHash3;
    private static String albumHash;
    private static String responseString;


    @Test
    @Order(1)
    void uploadImageFromUrlForAlbumTest() {
        imageHash=given()
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
    @Order(2)
    void uploadImage3FromUrlForAlbumTest() {
        imageHash3=given()
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
    void albumCreationTest() {
        albumHash=given()
                .headers(headers)
                .contentType("multipart/form-data")
               .multiPart("ids[]", imageHash)
               .multiPart("title", albumTitle)
              .multiPart("description",albumDescription)
               .multiPart("cover", imageHash)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("/album")
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
    void getAlbumTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.title", is(albumTitle))
                .body("data.cover", is(imageHash))
                .body("data.images[0].id", is(imageHash))
                .body("data.images_count", is (1))
                .body("data.account_url", is(accountUrl))
                .when()
                .get("/album/{albumHash}", albumHash)
                 .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(5)
    void uploadImageToAlbumTest() {
        imageHash2=given()
                .headers(headers)
                .multiPart("image", imageNatureUrl)
                .multiPart("album",albumHash)
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
    @Order(6)
    void getImageInAlbumTest() {
        responseString= given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.images_count", is (2))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.images")
        ;
        assertThat(responseString,containsString(imageHash2));
        assertThat(responseString,containsString(imageHash));

    }

    @Test
    @Order(7)
    void favoriteAlbumTest() {
        given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("data", is("favorited"))
                .body("status", is(200))
                .when()
                .post("/album/{albumHash}/favorite", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @Test
    @Order(8)
    void getAccountFavoritesTest() {
        responseString = given()
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
        assertThat(responseString, containsString(albumHash));
    }

    @Test
    @Order(9)
    void updateAlbumUsingPutTest() {
        given()
                .headers(headers)
                .multiPart("ids[]", imageHash)
                .multiPart("ids[]", imageHash3)
                .multiPart("title",updatedAlbumTitle)
                .multiPart("description",updatedAlbumDescription)
                .multiPart("cover",imageHash3)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is (true))
                .when()
                .put("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(10)
    void checkUpdateAlbumUsingPutTest() {
        responseString= given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.title", is (updatedAlbumTitle))
                .body("data.description", is (updatedAlbumDescription))
                .body("data.cover", is (imageHash3))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data")
        ;
        assertThat(responseString,containsString(imageHash3));
        assertThat(responseString,containsString(imageHash));
        assertThat(responseString, not(containsString(imageHash2)));
    }

    @Test
    @Order(11)
    void updateAlbumUsingPostTest() {
        given()
                .headers(headers)
                .multiPart("ids[]", imageHash2)
                .multiPart("ids[]", imageHash3)
                .multiPart("title",updatedAlbumTitle+"1")
                .multiPart("description",updatedAlbumDescription+"1")
                .multiPart("cover",imageHash2)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is (true))
                .when()
                .post("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(12)
    void checkUpdateAlbumUsingPostTest() {
        responseString= given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data.title", is (updatedAlbumTitle+"1"))
                .body("data.description", is (updatedAlbumDescription+"1"))
                .body("data.cover", is (imageHash2))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data")
        ;
        assertThat(responseString,containsString(imageHash3));
        assertThat(responseString,containsString(imageHash2));
        assertThat(responseString, not(containsString(imageHash)));
    }

    @Test
    @Order(13)
    void addImageToAlbumTest() {
        given()
                .headers(headers)
                .multiPart("ids[]", imageHash)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is (true))
                .when()
                .put("/album/{albumHash}/add", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(14)
    void checkAddingImageToAlbumTest() {
        responseString= given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data")
        ;
        assertThat(responseString,containsString(imageHash));
    }

    @Test
    @Order(15)
    void removeImageFromAlbumTest() {
        given()
                .headers("Authorization", token)
                .multiPart("ids[]",imageHash3)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .body("data", is (true))
                .when()
                .delete("/album/{albumHash}/remove_images", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    @Order(16)
    void checkRemovingImageFromAlbumTest() {
        responseString= given()
                .headers(headers)
                .expect()
                .body("success", is(true))
                .body("status", is(200))
                .when()
                .get("/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data")
        ;
        assertThat(responseString,not(containsString(imageHash3)));
    }

    @Test
    @Order(17)
    void albumDeletionTest() {
        given()
            .headers("Authorization", token)
            .expect()
            .body("success", is(true))
            .body("status", is(200))
            .when()
            .delete("/album/{albumHash}", albumHash)
            .prettyPeek()
            .then()
            .statusCode(200);
    }

    @Test
    @Order(18)
    void checkAlbumDeletionTest() {
        given()
                .headers(headers)
                .when()
                .get("album/{albumHash}", albumHash)
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
}
