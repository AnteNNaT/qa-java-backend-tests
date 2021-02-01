package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
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
    static void beforeAll() {
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
    }

    @Test
    @SneakyThrows
    void createProductWithNullPriceTest() {
        Response<Product> response = productService.createProduct(product7)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), is(true));
        assertThat(response.body().getPrice(), equalTo(0));
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
    void createProductsWithEqualsParametersTest() {
        Response<Product> response = productService.createProduct(product1)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        Response<Product> response2 = productService.createProduct(product1)
                .execute();
        id2 = response2.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }


    @SneakyThrows
    @AfterEach
    void tearDown() {
        assertThat(StepUtils.deleteImagesAfterTests(id).isSuccessful(), CoreMatchers.is(true));
        if (id2 != 0) {
            assertThat(StepUtils.deleteImagesAfterTests(id2).isSuccessful(), CoreMatchers.is(true));
        }
    }

}

/*
Category
1) получить категорию +
2) получить категорию с несуществующим id+
4) получить категорию после создания новых товаров+
Product
позитивные тесты
POST
1) создать продукт() +
14) создать с русским наименованием +
GET
получить продукт по id+
GET (products)
получить все продукты
PUT
1) обновить цену+
2) обновить наименование+
3) обновить категорию+
4) обновить все сразу+


негативные тесты
POST
1) указать id продукта при создании; +
2) указать дробное число в цене+
3) указать несуществующую категорию +
4) указать id категории +
5) создать с пустым наименованием +
6) создать с пустой ценой+
8) создать с отрицательной ценой+
9) создать с пустой категорией+
10) создать с длинным наименованием +
11) создать с большой ценой+
13) создать с повторяющимся наименованием, ценой+
15) непечатные символы в наименовании+
16) пробелы в наименовании +
7) создать с нулевой ценой+
GET
17) получить несуществующий продукт+
18) получить продукт с пустым id+
19) получить удаленный продукт+
PUT
19) несуществующий id+
20) несуществующая категория+
12) null категория+
21) с null наименованием+
22) с длинным наименованием+
27) с отрицательной ценой+
29) с нулевой ценой+
30) id не указано+
31) с русским наименованием+

DELETE
1) удалить с несуществующим id+
2) удалить с пустым id+

 */