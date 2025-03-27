package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Класс "ExcelProductDAO" реализует интерфейс ProductDAO.
 * Использует Excel-файл для хранения списка продуктов и категорий.
 * Позволяет загружать, добавлять, обновлять и удалять данные в файле.
 */
public class ExcelProductDAO implements ProductDAO {
    private static final String FILE_PATH = "C:/Users/kozlo/IdeaProjects/task8DAOanddop/products.xlsx";

    // Списки для хранения продуктов и тегов
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();

    // Конструктор, загружающий теги и продукты из файла
    public ExcelProductDAO() {
        loadTags();
        loadProducts();
    }

    /**
     * Загружает предустановленные теги в список.
     */
    private void loadTags() {
        tags.add(new Tag(1, "Электроника"));
        tags.add(new Tag(2, "Одежда"));
        tags.add(new Tag(3, "Мебель"));
    }

    /**
     * Загружает продукты из файла Excel и добавляет их в список products.
     */
    private void loadProducts() {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Получаем первый лист из книги
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Перебираем строки в файле
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int id = (int) row.getCell(0).getNumericCellValue();  // ID продукта
                String name = row.getCell(1).getStringCellValue();    // Название продукта
                int count = (int) row.getCell(2).getNumericCellValue(); // Количество
                int tagId = (int) row.getCell(3).getNumericCellValue(); // ID тега

                // Ищем тег по ID
                Tag tag = tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);

                // Добавляем продукт в список
                products.add(new Product(id, name, count, tag));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает список продуктов.
     */
    @Override
    public ObservableList<Product> getProducts() {
        return products;
    }

    /**
     * Возвращает список тегов.
     */
    @Override
    public ObservableList<Tag> getTags() {
        return tags;
    }

    /**
     * Добавляет новый продукт в список и сохраняет изменения в файл.
     */
    @Override
    public void addProduct(int id, String name, int count, Tag tag) {
        products.add(new Product(id, name, count, tag));
        saveToFile(); // Сохраняем изменения в Excel
    }

    /**
     * Обновляет данные существующего продукта и сохраняет изменения.
     */
    @Override
    public void updateProduct(Product product, String newName, int newCount, Tag newTag) {
        product.setName(newName);
        product.setCount(newCount);
        product.setTag(newTag);
        saveToFile(); // Сохраняем изменения
    }

    /**
     * Удаляет продукт из списка и обновляет файл.
     */
    @Override
    public void deleteProduct(Product product) {
        products.remove(product);
        saveToFile(); // Обновляем файл
    }

    /**
     * Добавляет новый тег в список.
     */
    @Override
    public void addTag(int id, String name) {
        tags.add(new Tag(id, name));
    }

    /**
     * Сохраняет текущий список продуктов в файл Excel.
     */
    private void saveToFile() {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(FILE_PATH)) {

            // Создаем новый лист
            Sheet sheet = workbook.createSheet("Products");

            // Записываем каждый продукт в отдельную строку
            for (int i = 0; i < products.size(); i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(products.get(i).getId());
                row.createCell(1).setCellValue(products.get(i).getName());
                row.createCell(2).setCellValue(products.get(i).getCount());
                row.createCell(3).setCellValue(products.get(i).getTag().getId());
            }

            // Записываем данные в файл
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
