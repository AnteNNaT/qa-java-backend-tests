package ru.geekbrains.update.product;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.db.utils.MyBatisUtils;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;
import ru.geekbrains.utils.StepUtils;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UpdateProductPositiveTest {
    private static ProductService productService;
    private static ProductsMapper productsMapper;
    private static Product product1;
    private static Product product2;
    private static Product product3;
    private static Faker faker = new Faker();
    private static Faker fakerRu = new Faker(new Locale("ru"));
    private static int id;
    private static String newTitle;
    private static int price;
    private static int id2;
    private static int id3;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        productsMapper = MyBatisUtils.getProductsMapper("mybatis-config.xml");

        product1 = new Product()
                .withTitle(faker.app().name())
                .withCategoryTitle(Category.ELECTRONIC.title)
                .withPrice((int) (Math.random() * 10000));
        Response<Product> response = productService.createProduct(product1).execute();
        id = response.body().getId();
        newTitle = faker.app().name();
        product1.setId(response.body().getId());
        product1.setTitle(newTitle);
        price = (int) (Math.random() * 1000);

        product2 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        Response<Product> response2 = productService.createProduct(product2).execute();
        id2 = response2.body().getId();
        product2.setId(id2);

        product3 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int) (Math.random() * 10000));
        Response<Product> response3 = productService.createProduct(product3).execute();
        id3 = response3.body().getId();
        product3.setId(id3);

    }


    @Test
    void UpdateOneFieldInProductChainTest() {
        updateTitleInProductTest();
        updatePriceInProductTest();
        updateCategoryInProductTest();
        updateTitleInRusInProductTest();
    }

    @Test
    void UpdateOneFieldToNullInProductChainTest() {
        updateTitleToNullInProductTest();
        updatePriceToNullInProductTest();
    }

    @SneakyThrows
    void updateTitleInProductTest() {
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(newTitle));
        assertThat(response.body().getPrice(), equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product1.getCategoryTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) id).getTitle(), equalTo(product1.getTitle()));
    }

    @SneakyThrows
    void updatePriceInProductTest() {
        product1.setPrice(price);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(newTitle));
        assertThat(response.body().getPrice(), equalTo(price));
        assertThat(response.body().getCategoryTitle(), equalTo(product1.getCategoryTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) id).getPrice(), equalTo(product1.getPrice()));
    }

    @SneakyThrows
    void updateCategoryInProductTest() {
        product1.setCategoryTitle(Category.FOOD.title);
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assert response.body() != null;
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(newTitle));
        assertThat(response.body().getPrice(), equalTo(price));
        assertThat(response.body().getCategoryTitle(), equalTo(Category.FOOD.title));
        assertThat(productsMapper.selectByPrimaryKey((long) id).getCategory_id(), equalTo((long) Category.FOOD.id));

    }

    @Test
    @SneakyThrows
    void updateAllFieldsInProductTest() {
        product2.setCategoryTitle(Category.ELECTRONIC.title);
        product2.setPrice(price);
        product2.setTitle(newTitle);
        Response<Product> response = productService.updateProduct(product2)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id2));
        assertThat(response.body().getTitle(), equalTo(newTitle));
        assertThat(response.body().getPrice(), equalTo(price));
        assertThat(response.body().getCategoryTitle(), equalTo(Category.ELECTRONIC.title));
        assertThat(productsMapper.selectByPrimaryKey((long) id2).getTitle(), equalTo(product2.getTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) id2).getPrice(), equalTo(product2.getPrice()));
        assertThat(productsMapper.selectByPrimaryKey((long) id2).getCategory_id(), equalTo(Long.valueOf(Category.ELECTRONIC.id)));
    }

    @SneakyThrows
    void updateTitleToNullInProductTest() {

        product3.setTitle(null);
        Response<Product> response = productService.updateProduct(product3)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id3));
        assertThat(response.body().getTitle(), is(nullValue()));
        assertThat(response.body().getPrice(), equalTo(product3.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product3.getCategoryTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) id3).getTitle(), is(nullValue()));
    }

    @SneakyThrows
    void updatePriceToNullInProductTest() {
        product3.setPrice(0);
        Response<Product> response = productService.updateProduct(product3)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id3));
        assertThat(response.body().getTitle(), is(nullValue()));
        assertThat(response.body().getPrice(), equalTo(product3.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product3.getCategoryTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) id3).getPrice(), is(0));
    }

    @SneakyThrows
    void updateTitleInRusInProductTest() {
        product1.setTitle(fakerRu.name().fullName());
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), is(product1.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product1.getCategoryTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) id).getTitle(), equalTo(product1.getTitle()));
    }

    @SneakyThrows
    @AfterAll
    static void afterAll() {
        Integer sizeBefore = productsMapper.selectByExample(new ProductsExample()).size();
        assertThat(StepUtils.deleteImagesAfterTests(id).isSuccessful(), CoreMatchers.is(true));
        assertThat(StepUtils.deleteImagesAfterTests(id2).isSuccessful(), CoreMatchers.is(true));
        assertThat(StepUtils.deleteImagesAfterTests(id3).isSuccessful(), CoreMatchers.is(true));
        assertThat(productsMapper.selectByExample(new ProductsExample()).size(), equalTo(sizeBefore - 3));
    }


}
