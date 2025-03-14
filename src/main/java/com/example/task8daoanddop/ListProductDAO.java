package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Класс "ListProductDAO" реализует интерфейс ProductDAO.
 * Использует коллекции для хранения списка продуктов и категорий.
 */
public class ListProductDAO implements ProductDAO {
    private ObservableList<Product> products = FXCollections.observableArrayList(); // Список продуктов
    private ObservableList<Tag> tags = FXCollections.observableArrayList();         // Список тегов (категорий)

    @Override
    public ObservableList<Product> getProducts() {
        return products;
    }

    @Override
    public ObservableList<Tag> getTags() {
        return tags;
    }

    @Override
    public void addProduct(int id, String name, int count, Tag tag) {
        products.add(new Product(id, name, count, tag));
    }

    @Override
    public void updateProduct(Product product, String newName, int newCount, Tag newTag) {
        product.setName(newName);
        product.setCount(newCount);
        product.setTag(newTag);
    }

    @Override
    public void deleteProduct(Product product) {
        products.remove(product);
    }

    @Override
    public void addTag(int id, String name) {
        tags.add(new Tag(id, name));
    }
}