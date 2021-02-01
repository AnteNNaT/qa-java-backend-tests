package ru.geekbrains.utils;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Response;
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
}
