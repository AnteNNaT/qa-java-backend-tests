package ru.geekbrains.get.products;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.db.utils.MyBatisUtils;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;


public class GetProductsTest {
    static ProductService productService;
    static ProductsMapper productsMapper;

    @SneakyThrows
    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        productsMapper = MyBatisUtils.getProductsMapper("mybatis-config.xml");
    }

    @Test
    @SneakyThrows
    void getProductsPositiveTest() {
        Response<List<Product>> response = productService.getProducts().execute();
        assertThat((long) response.body().size(), is(productsMapper.countByExample(new ProductsExample())));
        assertThat(response.body(), hasItems(
                hasProperty("categoryTitle", is(Category.FOOD.title)),
                hasProperty("categoryTitle", is(Category.ELECTRONIC.title))
        ));

    }
}
