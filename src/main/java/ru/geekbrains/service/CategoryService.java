package ru.geekbrains.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.dto.ErrorResponse;
import ru.geekbrains.dto.GetCategoryResponse;

public interface CategoryService {

    @GET("categories/{id}")
    Call<GetCategoryResponse> getCategory(@Path("id") int id);

    @GET("categories/{id}")
    Call<ErrorResponse> getErrorNotFound(@Path("id") int id);
}