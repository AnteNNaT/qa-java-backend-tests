package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import static org.hamcrest.Matchers.*;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductTest {
    static ProductService productService;
    private static Product product1;
    private static int id;
    private static Faker faker = new Faker();

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        product1 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int)(Math.random() * 10000));
        Response<Product> response =productService.createProduct(product1).execute();
        id=response.body().getId();
    }

    @Test
    void testsChain(){
        getProductByIdPositiveTest();
        getProductAfterDeletionNegativeTest();
    }
    @SneakyThrows
    void getProductByIdPositiveTest() {
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getPrice(), equalTo(product1.getPrice()));
        assertThat(response.body().getTitle(), equalTo(product1.getTitle()));
        assertThat(response.body().getCategoryTitle(), equalTo(product1.getCategoryTitle()));
    }

    @SneakyThrows
    void getProductAfterDeletionNegativeTest() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        Response<Product> productResponse = productService.getProduct(id).execute();
        assertThat(productResponse.isSuccessful(), CoreMatchers.is(false));
    }
    @SneakyThrows
    @Test
    void getProductByNonExistentIdNegativeTest() {
        id=(int)(Math.random() * 1000000);
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
    @SneakyThrows
    @Test
    void getProductByNullIdNegativeTest() {
        id=0;
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
    @SneakyThrows
    @Test
    void getProductByNegativeIdNegativeTest() {
        id=-(int)(Math.random() * 1000000);
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

}
