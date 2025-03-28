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
    private static final String FILE_PATH = "products.xlsx";
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();
    private int nextProductId = 1;

    public ExcelProductDAO() {
        loadTags();
        loadProducts();
        // Инициализируем nextProductId
        nextProductId = products.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0) + 1;
    }

    private void loadTags() {
        tags.add(new Tag(1, "Электроника"));
        tags.add(new Tag(2, "Одежда"));
        tags.add(new Tag(3, "Мебель"));
    }

    private void loadProducts() {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                int count = (int) row.getCell(2).getNumericCellValue();
                int tagId = (int) row.getCell(3).getNumericCellValue();

                Tag tag = tags.stream()
                        .filter(t -> t.getId() == tagId)
                        .findFirst()
                        .orElse(null);

                products.add(new Product(id, name, count, tag));
            }
        } catch (IOException e) {
            // Если файл не существует, создадим его при первом сохранении
            System.out.println("Файл не найден, будет создан при первом сохранении");
        }
    }

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
        int newId = nextProductId++;
        products.add(new Product(newId, name, count, tag));
        saveToFile();
    }

    @Override
    public void updateProduct(Product product, String newName, int newCount, Tag newTag) {
        product.setName(newName);
        product.setCount(newCount);
        product.setTag(newTag);
        saveToFile();
    }

    @Override
    public void deleteProduct(Product product) {
        products.remove(product);
        saveToFile();
    }

    @Override
    public void addTag(int id, String name) {
        tags.add(new Tag(id, name));
    }

    private void saveToFile() {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(FILE_PATH)) {

            Sheet sheet = workbook.createSheet("Products");

            // Заголовки
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Count");
            headerRow.createCell(3).setCellValue("Tag ID");

            // Данные
            for (int i = 0; i < products.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Product product = products.get(i);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getCount());
                row.createCell(3).setCellValue(product.getTag().getId());
            }

            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}