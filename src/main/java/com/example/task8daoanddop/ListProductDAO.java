package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Класс "ListProductDAO" реализует интерфейс ProductDAO.
 * Использует коллекции для хранения списка продуктов и категорий.
 */
public class ListProductDAO implements ProductDAO {
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();
    private int nextProductId = 1; // Счетчик для генерации ID

    @Override
    public ObservableList<Product> getProducts() {
        return products;
    }

    @Override
    public ObservableList<Tag> getTags() {
        return tags;
    }

    @Override
    public void addProduct(String name, int count, Tag tag) {
        // Генерируем новый ID
        int newId = nextProductId++;
        products.add(new Product(newId, name, count, tag));
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

        // Добавление продуктов
        products.add(new Product(1, "Смартфон", 15, tags.get(0)));  // Электроника
        products.add(new Product(2, "Ноутбук", 8, tags.get(0)));    // Электроника
        products.add(new Product(3, "Футболка", 30, tags.get(1)));  // Одежда
        products.add(new Product(4, "Диван", 5, tags.get(2)));      // Мебель
        products.add(new Product(5, "Стул", 12, tags.get(2)));      // Мебель
        products.add(new Product(6, "Наушники", 25, tags.get(0)));  // Электроника
        products.add(new Product(7, "Куртка", 10, tags.get(1)));    // Одежда
        products.add(new Product(8, "Стол", 7, tags.get(2)));       // Мебель
        products.add(new Product(9, "Планшет", 9, tags.get(0)));   // Электроника
        products.add(new Product(10, "Стол", 32, tags.get(2)));     // Мебель
        products.add(new Product(11, "Кресло", 25, tags.get(2)));   // Мебель

        // Устанавливаем следующий ID
        nextProductId = products.size() + 1;
    }
}