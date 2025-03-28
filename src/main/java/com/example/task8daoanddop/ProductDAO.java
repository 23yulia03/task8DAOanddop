package com.example.task8daoanddop;

import javafx.collections.ObservableList;

/**
 * Интерфейс "ProductDAO" определяет методы для работы с товарами и категориями.
 * Обеспечивает абстракцию для хранения и управления списком продуктов.
 */
public interface ProductDAO {
    ObservableList<Product> getProducts();
    ObservableList<Tag> getTags();
    void addProduct(String name, int count, Tag tag);
    void updateProduct(Product product, String newName, int newCount, Tag newTag);
    void deleteProduct(Product product);
    void addTag(int id, String name);
}