package ru.geekbrains.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.geekbrains.dto.Product;
import ru.geekbrains.dto.ProductWithDoublePrice;

import java.util.List;

public interface ProductService {
    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @POST("products")
    Call<ResponseBody> createProductWithError(@Body Product createProductRequest);

    @POST("products")
    Call<ProductWithDoublePrice> createProductWithDoublePrice(@Body ProductWithDoublePrice createProductRequest);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

    @PUT("products")
    Call<Product> updateProduct(@Body Product updateProductRequest);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @GET("products")
    Call<List<Product>> getProducts();
}

