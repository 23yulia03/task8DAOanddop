package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Тесты с заглушками (Mock Objects)
@ExtendWith(MockitoExtension.class)
class ProductDAOTest {

    @Mock
    private ProductDAO mockDao;

    private ObservableList<Product> testProducts;
    private ObservableList<Tag> testTags;

    @BeforeEach
    void setUp() {
        testTags = FXCollections.observableArrayList(
                new Tag(1, "Электроника"),
                new Tag(2, "Одежда")
        );

        testProducts = FXCollections.observableArrayList(
                new Product(1, "Ноутбук", 10, testTags.get(0)),
                new Product(2, "Футболка", 20, testTags.get(1))
        );
    }

    @Test
    void testGetProducts() {
        // Настраиваем мок только для этого теста
        when(mockDao.getProducts()).thenReturn(testProducts);

        ObservableList<Product> products = mockDao.getProducts();
        assertEquals(2, products.size());
        assertEquals("Ноутбук", products.get(0).getName());

        // Проверяем, что метод был вызван
        verify(mockDao, times(1)).getProducts();
    }

    @Test
    void testGetTags() {
        // Настраиваем мок только для этого теста
        when(mockDao.getTags()).thenReturn(testTags);

        ObservableList<Tag> tags = mockDao.getTags();
        assertEquals(2, tags.size());
        assertEquals("Электроника", tags.get(0).getName());

        verify(mockDao, times(1)).getTags();
    }

    @Test
    void testAddProduct() throws SQLException {
        Tag newTag = new Tag(3, "Мебель");

        // Настраиваем мок
        doNothing().when(mockDao).addProduct(anyString(), anyInt(), any(Tag.class));

        // Вызываем тестируемый метод
        mockDao.addProduct("Стул", 5, newTag);

        // Проверяем, что метод был вызван с правильными параметрами
        verify(mockDao, times(1)).addProduct("Стул", 5, newTag);
    }
}