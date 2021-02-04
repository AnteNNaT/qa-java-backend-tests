package ru.geekbrains.get.category;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.checkerframework.checker.units.qual.C;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.db.utils.MyBatisUtils;
import ru.geekbrains.dto.ErrorResponse;
import ru.geekbrains.dto.GetCategoryResponse;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.Messages;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;
import ru.geekbrains.utils.StepUtils;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.geekbrains.enums.Category.FOOD;
import static ru.geekbrains.enums.Category.NULL_CATEGORY;

public class GetCategoryTest {
    static CategoryService categoryService;
    private static ProductService productService;
    private static Product product1;
    private static Product product2;
    static ProductsMapper productsMapper;
    private static Faker faker = new Faker();
    private static int id;
    private static int id2;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit().create(CategoryService.class);
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        productsMapper = MyBatisUtils.getProductsMapper("mybatis-config.xml");

        product1 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product2 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
    }


    @SneakyThrows
    @Test
    void getCategoryWithResponseAssertionsPositiveTest() {
        Response<GetCategoryResponse> response = categoryService.getCategory(FOOD.id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(FOOD.id));
        assertThat(response.body().getTitle(), equalTo(FOOD.title));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(FOOD.title)));
        ProductsExample example = new ProductsExample();
        example.createCriteria().andCategory_idEqualTo((long) FOOD.id);
        assertThat((long) response.body().getProducts().size(), equalTo(productsMapper.countByExample(example)));
    }

    @SneakyThrows
    @Test
    void getCategoryByNonExistentIdNegativeTest() {
        Response<ResponseBody> response = categoryService.getErrorNotFound(NULL_CATEGORY.id).execute();
        assertThat(response.code(), equalTo(404));
        ErrorResponse errorResponse = StepUtils.convertToErrorResponse(response.errorBody().string());
        assertThat(errorResponse.getStatus(), equalTo(404));
        assertThat(errorResponse.getMessage(), equalTo(Messages.unableToFindCategory + NULL_CATEGORY.id));
    }

    @Test
    @SneakyThrows
    void getCategoryWithNewProductsTest() {
        Response<Product> response1 = productService.createProduct(product1)
                .execute();
        id = response1.body().getId();
        Response<Product> response2 = productService.createProduct(product2)
                .execute();
        id2 = response2.body().getId();
        Response<GetCategoryResponse> response = categoryService.getCategory(FOOD.id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));
        assertThat(response.body().getTitle(), equalTo(FOOD.title));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(FOOD.title)));
        assertThat(response.body().getProducts(), hasItems(
                hasProperty("id", is(id)),
                hasProperty("id", is(id2))
        ));
        ProductsExample example = new ProductsExample();
        example.createCriteria().andCategory_idEqualTo((long) FOOD.id);
        assertThat((long) response.body().getProducts().size(), equalTo(productsMapper.countByExample(example)));
    }

    @SneakyThrows
    @AfterAll
    static void afterAll() {
        Integer sizeBefore = productsMapper.selectByExample(new ProductsExample()).size();
        if (id != 0) {
            assertThat(StepUtils.deleteImagesAfterTests(id).isSuccessful(), CoreMatchers.is(true));
            assertThat(productsMapper.selectByExample(new ProductsExample()).size(), equalTo(sizeBefore - 1));
            sizeBefore--;
        }
        if (id2 != 0) {
            assertThat(StepUtils.deleteImagesAfterTests(id2).isSuccessful(), CoreMatchers.is(true));
            assertThat(productsMapper.selectByExample(new ProductsExample()).size(), equalTo(sizeBefore - 1));
        }
    }


}
