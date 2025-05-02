package com.example.task8daoanddop;

import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * Интерфейс "ProductDAO" определяет методы для работы с товарами и категориями.
 * Обеспечивает абстракцию для хранения и управления списком продуктов.
 */
public interface ProductDAO {
    ObservableList<Product> getProducts();
    ObservableList<Tag> getTags();
    void addProduct(String name, int count, Tag tag) throws SQLException;
    void updateProduct(Product product, String newName, int newCount, Tag newTag) throws SQLException;
    void deleteProduct(Product product) throws SQLException;

    void deleteProductById(int id) throws SQLException;

    void addTag(int id, String name) throws SQLException;
}