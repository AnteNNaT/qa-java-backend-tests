package ru.geekbrains;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.dto.ErrorResponse;
import ru.geekbrains.dto.GetCategoryResponse;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static ru.geekbrains.enums.Category.FOOD;
import static ru.geekbrains.enums.Category.NULL_CATEGORY;

public class GetCategoryTest {
    static CategoryService categoryService;
    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
    }

    @SneakyThrows
    @Test
    void getCategoryByIdPositiveTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(FOOD.id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @Test
    void getCategoryWithResponseAssertionsPositiveTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(FOOD.id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo(FOOD.title));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(FOOD.title)));
    }

    @SneakyThrows
    @Test
    /*
    разобраться с NullPointerException
     */
    void getCategoryByNonExistentIdNegativeTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(NULL_CATEGORY.id).execute();
        assertThat(response.code(), equalTo(404));
       // System.out.println(response.errorBody().string());
       //String s=response.errorBody().string();
       // ObjectMapper mapper = new ObjectMapper();
       // JsonNode actualObj = mapper.readTree(s);
       // ErrorResponse errorResponse=mapper.convertValue(actualObj,new TypeReference<ErrorResponse>(){
       // });
       // System.out.println(errorResponse.toString());
      //  assertThat(errorResponse.getStatus(),equalTo(404));
        //System.out.println(response.toString());
   //     assertThat(response.body().status, is(404));
    }


}
