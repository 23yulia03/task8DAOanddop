package com.example.task8daoanddop;

import com.example.task8daoanddop.Tag;

import java.time.LocalDate;

/**
 * Класс "Продукт", представляет товар в системе.
 * Каждый продукт имеет уникальный идентификатор, название, количество и категорию (тег).
 */
public class Product {
    private int id;         // Уникальный идентификатор продукта
    private String name;    // Название продукта
    private int count;      // Количество на складе
    private Tag tag;        // Категория продукта (тег)

    // Конструктор для создания нового продукта.
    public Product(int id, String name, int count, Tag tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;

        // Проверка на високосный год
        int currentYear = LocalDate.now().getYear();
        if (isLeapYear(currentYear)) {
            this.count = count * 2; // Увеличение количества в 2 раза
        } else {
            this.count = count;
        }
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

    // Метод для проверки, является ли год високосным
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    @Override
    public String toString() {
        return name + " (" + count + " шт, " + tag.getName() + ")";
    }
}