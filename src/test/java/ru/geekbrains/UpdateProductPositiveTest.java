package ru.geekbrains;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.dto.Product;
import ru.geekbrains.dto.ProductWithDoublePrice;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UpdateProductPositiveTest {
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
    private static int id;
    private static String newTitle;
    private int id2;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        product1 = new Product()
                .withTitle(faker.app().name())
                .withCategoryTitle(Category.ELECTRONIC.title)
                .withPrice((int)(Math.random() * 10000));
        Response<Product> response =productService.createProduct(product1).execute();
        id=response.body().getId();
        newTitle=faker.app().name();
        product1.setId(response.body().getId());
        product1.setTitle(newTitle);

        product2=new Product()
                .withTitle(fakerRu.name().fullName())
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int)(Math.random() * 10000));
        product3=new Product()
                .withTitle(" ")
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int)(Math.random() * 10000));
        product4=new Product()
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int)(Math.random() * 10000));
        product5=new Product()
                .withTitle("$%@")
                .withCategoryTitle(Category.FOOD.title)
                .withPrice((int)(Math.random() * 10000));
        product6=new Product()
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
    void createProductWithTooLongPriceTest() {
        Response<Product> response = productService.updateProduct(product1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(newTitle));
        assertThat(response.body().getPrice(), equalTo(product1.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product1.getCategoryTitle()));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        if (id2!=0){Response<ResponseBody> response2 = productService.deleteProduct(id2).execute();
            assertThat(response2.isSuccessful(), CoreMatchers.is(true));}
    }
}
