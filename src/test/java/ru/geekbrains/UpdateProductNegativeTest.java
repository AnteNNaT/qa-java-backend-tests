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
import ru.geekbrains.utils.StepUtils;

import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateProductNegativeTest {
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

    @SneakyThrows
    @Test
    void updateCategoryTitleToNonExistentInProductTest() {
        product1.setId(id);
        product1.setCategoryTitle(Category.NULL_CATEGORY.title);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @Test
    void updateCategoryTitleToNullInProductTest() {
        product1.setId(id);
        product1.setCategoryTitle(null);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @Test
    void updateCategoryWithoutIdProductTest() {
        product1.setId(null);
        product1.setCategoryTitle(Category.FOOD.title);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @Test
    void updateCategoryWithNonExistentIdProductTest() {
        product1.setId((int) (Math.random() * 1000000));
        product1.setCategoryTitle(Category.FOOD.title);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @Test
    void updateCategoryWithTooLongTitleProductTest() {
        product1.setId(id);
        product1.setCategoryTitle(Category.FOOD.title);
        product1.setTitle(faker.lorem().fixedString(500));
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }

    @SneakyThrows
    @Test
    void updateCategoryWithNegativePriceProductTest() {
        product1.setId(id);
        product1.setCategoryTitle(Category.FOOD.title);
        product1.setPrice(-(int) (Math.random() * 10000));
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
