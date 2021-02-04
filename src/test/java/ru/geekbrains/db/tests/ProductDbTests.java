package ru.geekbrains.db.tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.db.utils.MyBatisUtils;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.Category;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Objects;

public class ProductDbTests {
    private static Integer productId;
    static ProductsMapper productsMapper;
    static ProductService productService;
    private static Product product;
    private static Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() throws IOException {
        productsMapper = MyBatisUtils.getProductsMapper("mybatis-config.xml");
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
        product = new Product()
                .withCategoryTitle(Category.FOOD.title)
                .withTitle(faker.food().ingredient())
                .withPrice((int) (Math.random() * 10000));
        productId = Objects.requireNonNull(
                productService.createProduct(product)
                        .execute()
                        .body()
        )
                .getId();
    }

    @Test
    void updateProductThroughDbTest() {
        product.setId(productId);
        product.setPrice(500);
        Products newProduct = new Products();
        newProduct.setPrice(product.getPrice());
        newProduct.setId(Long.valueOf(product.getId()));
        newProduct.setCategory_id(Long.valueOf(Category.FOOD.id));
        newProduct.setTitle(product.getTitle());

        productsMapper.updateByPrimaryKey(newProduct);
        assertThat(productsMapper.selectByPrimaryKey(productId.longValue()).getPrice(), is(product.getPrice()));

    }

    @Test
    void deleteProductThroughDbTest() {
        Integer sizeBefore = productsMapper.selectByExample(new ProductsExample()).size();
        productsMapper.deleteByPrimaryKey(productId.longValue());
        assertThat(productsMapper.selectByExample(new ProductsExample()).size(), equalTo(sizeBefore - 1));

    }
}
