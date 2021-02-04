package ru.geekbrains.create.product;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.db.utils.MyBatisUtils;
import ru.geekbrains.dto.ProductWithDoublePrice;
import ru.geekbrains.enums.Category;
import ru.geekbrains.dto.Product;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;
import ru.geekbrains.utils.StepUtils;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateProductPositiveTest {
    private static ProductService productService;
    static ProductsMapper productsMapper;
    private static Product product1;
    private static Product product2;
    private static Product product3;
    private static Product product4;
    private static Product product5;
    private static Product product6;
    private static Product product7;
    private static ProductWithDoublePrice product9;
    private static Faker faker = new Faker();
    private static Faker fakerRu = new Faker(new Locale("ru"));
    private int id;
    private int id2;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        productsMapper = MyBatisUtils.getProductsMapper("mybatis-config.xml");
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        product1 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product2 = new Product()
                .withTitle(fakerRu.name().fullName())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product3 = new Product()
                .withTitle(" ")
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product4 = new Product()
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product5 = new Product()
                .withTitle("$%@")
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        product6 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title);
        product7 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice(0);
        product9 = new ProductWithDoublePrice()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((Math.random() * 10000));
    }

    @Test
    @SneakyThrows
    void createProductInFoodCategoryTest() {
        Response<Product> response = productService.createProduct(product1)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo(product1.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product1.getCategoryTitle()));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(id)).getCategory_id(), is(1L));
    }

    @Test
    @SneakyThrows
    void createProductInFoodCategoryWithRusTitleTest() {
        Response<Product> response = productService.createProduct(product2)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo(product2.getTitle()));
    }

    @Test
    @SneakyThrows
    void createProductWithSpacesInTitleTest() {
        Response<Product> response = productService.createProduct(product3)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo(product3.getTitle()));
    }

    @Test
    @SneakyThrows
    void createProductWithEmptyNameTest() {
        Response<Product> response = productService.createProduct(product4)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.body().getTitle(), is(nullValue()));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(id)).getTitle(), is(nullValue()));
    }

    @Test
    @SneakyThrows
    void createProductWithSpecialSymbolsInTitleTest() {
        Response<Product> response = productService.createProduct(product5)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.body().getTitle(), equalTo(product5.getTitle()));
    }

    @Test
    @SneakyThrows
    void createProductWithEmptyPriceTest() {
        Response<Product> response = productService.createProduct(product6)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.body().getPrice(), equalTo(0));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(id)).getPrice(), is(0));
    }

    @Test
    @SneakyThrows
    void createProductWithNullPriceTest() {
        Response<Product> response = productService.createProduct(product7)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.body().getPrice(), equalTo(0));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(id)).getPrice(), is(0));
    }

    @Test
    @SneakyThrows
    void createProductWithFractionalPartInPriceTest() {
        Response<ProductWithDoublePrice> response = productService.createProductWithDoublePrice(product9)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat((int) response.body().getPrice(), equalTo((int) product9.getPrice()));
    }

    @Test
    @SneakyThrows
    void createProductsWithEqualParametersTest() {
        Response<Product> response = productService.createProduct(product1)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        Response<Product> response2 = productService.createProduct(product1)
                .execute();
        id2 = response2.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(productsMapper.selectByPrimaryKey((long) id).getPrice(),
                equalTo(productsMapper.selectByPrimaryKey((long) id2).getPrice()));
        assertThat(productsMapper.selectByPrimaryKey((long) id).getTitle(),
                equalTo(productsMapper.selectByPrimaryKey((long) id2).getTitle()));
    }


    @SneakyThrows
    @AfterEach
    void tearDown() {
        Integer sizeBefore = productsMapper.selectByExample(new ProductsExample()).size();
        assertThat(StepUtils.deleteImagesAfterTests(id).isSuccessful(), CoreMatchers.is(true));
        assertThat(productsMapper.selectByExample(new ProductsExample()).size(), equalTo(sizeBefore - 1));
        if (id2 != 0) {
            assertThat(StepUtils.deleteImagesAfterTests(id2).isSuccessful(), CoreMatchers.is(true));
        }
    }

}
