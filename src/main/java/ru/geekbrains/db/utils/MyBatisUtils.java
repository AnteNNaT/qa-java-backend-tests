package ru.geekbrains.db.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {
    public static CategoriesMapper getCategoriesMapper(String resource) throws IOException {
        SqlSession session = getSqlSession(resource);
        return session.getMapper(CategoriesMapper.class);
    }

    public static ProductsMapper getProductsMapper(String resource) throws IOException {
        SqlSession session = getSqlSession(resource);
        return session.getMapper(ProductsMapper.class);
    }
    private static SqlSession getSqlSession(String resource) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        return session;
    }
}
