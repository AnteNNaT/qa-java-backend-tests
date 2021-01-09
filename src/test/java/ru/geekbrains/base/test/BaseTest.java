package ru.geekbrains.base.test;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.is;

public abstract class BaseTest {
    private static Properties prop = new Properties();
    private static Path filePath = Paths.get("src").resolve("test").resolve("resources");
    private static File propertiesFile = new File(filePath + "/application.properties");
    protected static String imageFileName = "error.png";
    protected static Map<String, String> headers = new HashMap<>();
    protected static String token;
    protected static String imageNatureUrl;
    protected static String albumTitle;
    protected static String albumDescription;
    protected static String accountUrl;
    protected static String imageTitle;
    protected static String imageDescription;
    protected static String imageGifUrl;
    protected static String updatedAlbumTitle;
    protected static String updatedAlbumDescription;
    protected static String largeImageUrl;
    protected static String textFileUrl;
    protected static ResponseSpecification responseSpec;
    protected static ResponseSpecification negativeResponseSpec;


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
        updatedAlbumTitle = prop.getProperty("updatedAlbumTitle");
        updatedAlbumDescription = prop.getProperty("updatedAlbumDescription");
        largeImageUrl = prop.getProperty("largeImageUrl");
        textFileUrl = prop.getProperty("textFileUrl");
        headers.put("Authorization", token);
        RestAssured.baseURI = prop.getProperty("base.url");

         responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("success", is(true))
                 .expectBody("status", is(200))
                .build();

        negativeResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectBody("success", is(false))
                .expectBody("status", is(400))
                .build();

        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private static void loadProperties() {
        try {
            prop.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
