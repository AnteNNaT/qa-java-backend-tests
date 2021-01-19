package ru.geekbrains.utils;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.geekbrains.service.Endpoints;

import static io.restassured.RestAssured.given;

public class StepUtils {


    public static void deleteImagesAfterTests (String imageName){
        given()
                .when()
                .delete(Endpoints.getDeleteAndUpdateImage, imageName);
    }
}
