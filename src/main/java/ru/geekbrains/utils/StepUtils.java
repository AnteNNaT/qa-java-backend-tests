package ru.geekbrains.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.geekbrains.dto.ErrorResponse;
import ru.geekbrains.service.ProductService;

public class StepUtils
{
    private static ProductService productService;

    @SneakyThrows
    public static Response<ResponseBody> deleteImagesAfterTests (int id){
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        return productService.deleteProduct(id).execute();
    }

    @SneakyThrows
    public static ErrorResponse convertToErrorResponse(String errorResponseString){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(errorResponseString);
        return mapper.convertValue(actualObj, new TypeReference<ErrorResponse>() {
        });
    }
}
