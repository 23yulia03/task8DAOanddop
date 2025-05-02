// PostgresProductDAOIntegrationTest.java
package com.example.task8daoanddop;

import org.junit.jupiter.api.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Интеграционные тесты
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostgresProductDAOIntegrationTest {
    private PostgresProductDAO dao;
    private Config config;

    @BeforeAll
    void prepareDatabase() throws SQLException {
        config = new Config();
        dao = new PostgresProductDAO(config);

        try (var connection = dao.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DELETE FROM products");
            statement.execute("INSERT INTO tags (id, name) VALUES (999, 'Test Category')");
        }
    }

    @Test
    void testProductAddition() throws SQLException {
        Tag testTag = new Tag(999, "Test Category");
        int initialCount = dao.getProducts().size();

        dao.addProduct("Test Product", 5, testTag);
        var products = dao.getProducts();

        assertEquals(initialCount + 1, products.size());
        assertEquals("Test Product", products.get(products.size()-1).getName());
    }

    @AfterAll
    void cleanDatabase() throws SQLException {
        try (var connection = dao.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DELETE FROM products WHERE name = 'Test Product'");
            statement.execute("DELETE FROM tags WHERE id = 999");
        }
    }
}