package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;
import ru.geekbrains.utils.StepUtils;

import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductNegativeTest {
    private static ProductService productService;
    private static Product product1;
    private static Faker faker = new Faker();
    private static int id;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        product1 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        Response<Product> response = productService.createProduct(product1).execute();
        id = response.body().getId();
    }

    @Test
    @SneakyThrows
    void deleteProductWithOutIdTest() {
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void deleteProductWithNonExistentIdTest() {
        product1.setId((int) (Math.random() * 1000000));
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @Test
    @SneakyThrows
    void deleteProductWithNullIdTest() {
        product1.setId(0);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @AfterAll
    static void afterAll() {
        assertThat(StepUtils.deleteImagesAfterTests(id).isSuccessful(), CoreMatchers.is(true));
    }

}
