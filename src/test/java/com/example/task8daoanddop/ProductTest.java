package com.example.task8daoanddop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ProductTest {

    @Test
    void isLeapYear() {
        Product product = new Product(1, "Test", 10, new Tag(1, "Test"));

        // ��������� ���������� � �� ���������� ����
        // ���������������
        assertFalse(product.isLeapYear(2025)); // �� ����������
        assertTrue(product.isLeapYear(2024)); // ����������
        assertFalse(product.isLeapYear(2023)); // �� ����������
        assertTrue(product.isLeapYear(2000)); // ���������� (������� �� 400)
        assertFalse(product.isLeapYear(1900)); // �� ���������� (������� �� 100, �� �� �� 400)

        // ���������
        assertFalse(product.isLeapYear(100)); // �� ���������� (������� �� 100, �� �� �� 400)
        assertTrue(product.isLeapYear(400)); // ���������� (������� �� 400, �������� �� ��, ��� ������� �� 100)
        assertTrue(product.isLeapYear(4)); // ���������� (������� �� 4)
    }
}