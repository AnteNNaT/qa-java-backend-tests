package ru.geekbrains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseTest {
    private static Properties prop=new Properties();
    private static Path filePath = Paths.get("src").resolve("test").resolve("resources");
    private static File testFile =new File(filePath +"/application.properties");
    protected static Map<String, String> headers=new HashMap<>();
    protected static String token;
    protected static String imageNatureUrl;
    protected static String albumTitle;
    protected static String albumDescription;
    protected static String accountUrl;

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        token=prop.getProperty("token");
        albumTitle=prop.getProperty("albumTitle");
        albumDescription=prop.getProperty("albumDescription");
        accountUrl=prop.getProperty("accountUrl");
        imageNatureUrl=prop.getProperty("imageNatureUrl");
        headers.put("Authorization", token);
        RestAssured.baseURI=prop.getProperty("base.url");

        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private static void loadProperties() {
        try {
            prop.load(new FileInputStream(testFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
