package ru.geekbrains.base.test;

import com.github.javafaker.Faker;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
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
    protected static String largeImageUrl;
    protected static String textFileUrl;
    protected static String imageGifUrl;
    protected static String imageAnimatedGifUrl;
    protected static String invalidUrl;
    protected static String albumTitle;
    protected static String albumDescription;
    protected static String accountUrl;
    protected static String imageTitle;
    protected static String imageDescription;
    protected static String updatedAlbumTitle;
    protected static String updatedAlbumDescription;
    protected static ResponseSpecification responseSpec;
    protected static ResponseSpecification negativeResponseSpec;
    protected static ResponseSpecification negative404ResponseSpec;
    protected static RequestSpecification requestSpec;
    protected static Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token = prop.getProperty("token");
        accountUrl = prop.getProperty("accountUrl");
        imageTitle = faker.dune().planet();
        imageDescription = faker.dune().quote();
        imageNatureUrl = prop.getProperty("imageNatureUrl");
        imageGifUrl = prop.getProperty("imageGifUrl");
        largeImageUrl = prop.getProperty("largeImageUrl");
        textFileUrl = prop.getProperty("textFileUrl");
        imageAnimatedGifUrl = prop.getProperty("imageAnimatedGifUrl");
        invalidUrl = "?" + imageNatureUrl;
        albumTitle = faker.book().title();
        albumDescription = faker.elderScrolls().quote();
        updatedAlbumTitle = faker.hobbit().character();
        updatedAlbumDescription = faker.hobbit().quote();
        headers.put("Authorization", token);
        RestAssured.baseURI = prop.getProperty("base.url");
        RestAssured.filters(new ResponseLoggingFilter(), new RequestLoggingFilter(), new AllureRestAssured());

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
                .build();

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

    protected static RequestSpecification createRequestSpec(String value) {
        RequestSpecification requestSpecWithMultiPart;
        MultiPartSpecification multiPartSpec;
        multiPartSpec = new MultiPartSpecBuilder(value)
                .controlName("image")
                .build();
        requestSpecWithMultiPart = requestSpec
                .multiPart(multiPartSpec);
        return requestSpecWithMultiPart;
    }

    private static void loadProperties() {
        try {
            prop.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
