package com.example.task8daoanddop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProductTest {

    @Test
    void isLeapYear() {
        Product product = new Product(1, "Test", 10, new Tag(1, "Test"));

        // Проверяем високосные и не високосные годы
        // Эквивалентности
        assertFalse(product.isLeapYear(2025)); // Не високосный
        assertTrue(product.isLeapYear(2024)); // Високосный
        assertFalse(product.isLeapYear(2023)); // Не високосный
        assertTrue(product.isLeapYear(2000)); // Високосный (делится на 400)
        assertFalse(product.isLeapYear(1900)); // Не високосный (делится на 100, но не на 400)

        // Граничные
        assertFalse(product.isLeapYear(100)); // Не високосный (делится на 100, но не на 400)
        assertTrue(product.isLeapYear(400)); // Високосный (делится на 400, несмотря на то, что делится на 100)
        assertTrue(product.isLeapYear(4)); // Високосный (делится на 4)
    }
}