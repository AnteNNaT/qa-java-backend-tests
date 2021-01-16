package ru.geekbrains.base.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.is;

public abstract class BaseTest {
    private static Properties prop = new Properties();
    private static Path filePath = Paths.get("src").resolve("test").resolve("resources");
    private static File propertiesFile = new File(filePath + "/application.properties");
    protected static File imageFileName = new File(filePath + "/justImage.png");
    protected static File smallImageFile = new File(filePath + "/smallImage.jpg");
    protected static File reallyNotImage = new File(filePath + "/reallyNotImage.png");
    protected static Map<String, String> headers = new HashMap<>();
    protected static String token;
    protected static String imageNatureUrl;
    protected static String albumTitle;
    protected static String albumDescription;
    protected static String accountUrl;
    protected static String imageTitle;
    protected static String imageDescription;
    protected static String imageGifUrl;
    protected static String imageAnimatedGifUrl;
    protected static String updatedAlbumTitle;
    protected static String updatedAlbumDescription;
    protected static String largeImageUrl;
    protected static String textFileUrl;
    protected static ResponseSpecification responseSpec;
    protected static ResponseSpecification negativeResponseSpec;
    protected static ResponseSpecification negative404ResponseSpec;
    protected static RequestSpecification requestSpec;


    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = prop.getProperty("token");
        albumTitle = prop.getProperty("albumTitle");
        albumDescription = prop.getProperty("albumDescription");
        accountUrl = prop.getProperty("accountUrl");
        imageTitle = prop.getProperty("imageTitle");
        imageDescription = prop.getProperty("imageDescription");
        imageNatureUrl = prop.getProperty("imageNatureUrl");
        imageGifUrl = prop.getProperty("imageGifUrl");
        imageAnimatedGifUrl = prop.getProperty("imageAnimatedGifUrl");
        updatedAlbumTitle = prop.getProperty("updatedAlbumTitle");
        updatedAlbumDescription = prop.getProperty("updatedAlbumDescription");
        largeImageUrl = prop.getProperty("largeImageUrl");
        textFileUrl = prop.getProperty("textFileUrl");
        headers.put("Authorization", token);
        RestAssured.baseURI = prop.getProperty("base.url");

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("success", is(true))
                .expectBody("status", is(200))
                .build();

        negativeResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType(ContentType.JSON)
                .expectBody("success", is(false))
                .expectBody("status", is(400))
                .build();

        negative404ResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectContentType(ContentType.HTML)
                .build();

        requestSpec = new RequestSpecBuilder()
                .addHeaders(headers)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.requestSpecification = requestSpec;
    }

    protected static String getFileContentInBase64String(File fileName) {
        byte[] fileContent = new byte[0];
        try {
            fileContent = FileUtils.readFileToByteArray(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private static void loadProperties() {
        try {
            prop.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
