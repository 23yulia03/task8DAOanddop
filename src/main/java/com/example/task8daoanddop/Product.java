package com.example.task8daoanddop;

/**
 * Класс "Продукт", представляет товар в системе.
 * Каждый продукт имеет уникальный идентификатор, название, количество и категорию (тег).
 */
public class Product {
    private int id;         // Уникальный идентификатор продукта
    private String name;    // Название продукта
    private int count;      // Количество на складе
    private Tag tag;        // Категория продукта (тег)

    public Product(int id, String name, int count, Tag tag) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return name + " (" + count + " шт, " + tag.getName() + ")";
    }
}