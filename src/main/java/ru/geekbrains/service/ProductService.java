package ru.geekbrains.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.dto.Product;

import java.util.List;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    @PUT("products")
    Call<ResponseBody> updateProduct(@Body Product updateProductRequest);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @GET("products")
    Call<List<Product>> getProducts();
}

