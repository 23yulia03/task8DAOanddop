package com.example.task8daoanddop;

/**
 * Класс "Тег", представляет категорию товара.
 * Каждый тег имеет уникальный идентификатор и название.
 */
public class Tag {
    private int id;         // Уникальный идентификатор тега
    private String name;    // Название тега (категории)

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}