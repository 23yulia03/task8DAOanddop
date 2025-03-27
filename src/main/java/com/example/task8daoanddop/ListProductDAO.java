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

    public ListProductDAO() {
        // Примеры тестовых данных
        tags.add(new Tag(1, "Электроника"));
        tags.add(new Tag(2, "Одежда"));
        tags.add(new Tag(3, "Мебель"));

        products.add(new Product(1, "Ноутбук", 10, tags.get(0)));
        products.add(new Product(2, "Футболка", 20, tags.get(1)));
        products.add(new Product(3, "Стол", 30, tags.get(2)));
    }
}