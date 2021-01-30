package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;

public class CreateProductNegativeTest {
    private static ProductService productService;
    private static Product product1;
    private static Product product2;
    private static Product product3;
    private static Product product4;
    private static Product product5;
    private static Product product6;
    private static Product product7;
    private static int id;
    private static Faker faker = new Faker();


    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        product1=new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.NULL_CATEGORY.title)
                .withPrice((int) (Math.random() * 10000));
        product2=new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(String.valueOf(Category.FOOD.id))
                .withPrice((int) (Math.random() * 10000));
        product3=new Product()
                .withId(1000)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product5=new Product()
                .withTitle(faker.food().ingredient())
                .withPrice((int) (Math.random() * 10000));
        product6=new Product()
                .withTitle(faker.lorem().fixedString(500))
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product7 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) -(Math.random() * 10000));
    }

    @Test
    @SneakyThrows
    void createProductInNonExistCategoryTest() {
        Response<Product> response = productService.createProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
    @Test
    @SneakyThrows
    void createProductWithIdCategoryTest() {
        Response<Product> response = productService.createProduct(product2)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
    @Test
    @SneakyThrows
    void createProductWithIdProductTest() {
        Response<Product> response = productService.createProduct(product3)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void createProductWithEmptyCategoryTest() {
        Response<Product> response = productService.createProduct(product5)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
    @Test
    @SneakyThrows
    void createProductWithTooLongTitleTest() {
        Response<Product> response = productService.createProduct(product6)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
    @Test
    @SneakyThrows
        //move to negativeTest?
    void createProductWithNegativePriceTest() {
        Response<Product> response = productService.createProduct(product7)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @SneakyThrows
    @AfterAll
    static void afterAll() {
            Response<ResponseBody> response = productService.deleteProduct(id).execute();
            assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }
}