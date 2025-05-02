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
    private final String filePath;
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Tag> tags = FXCollections.observableArrayList();
    private int nextProductId = 1;

    public ExcelProductDAO(Config config) {
        this.filePath = config.getExcelPath();
        loadTags();
        loadProducts();
        calculateNextId();
    }

    private void calculateNextId() {
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
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Пропускаем первую строку, так как это заголовок
            rowIterator.next();  // Пропускаем первую строку

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    // Проверяем и извлекаем данные из ячеек с учетом их типов
                    int id = getNumericCellValue(row.getCell(0));
                    String name = row.getCell(1).getStringCellValue();
                    int count = getNumericCellValue(row.getCell(2));
                    int tagId = getNumericCellValue(row.getCell(3));

                    // Находим тег по ID
                    Tag tag = tags.stream()
                            .filter(t -> t.getId() == tagId)
                            .findFirst()
                            .orElse(null);

                    // Добавляем продукт в список
                    products.add(new Product(id, name, count, tag));
                } catch (Exception e) {
                    System.out.println("Ошибка при обработке строки: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // Если файл не существует, создадим его при первом сохранении
            System.out.println("Файл не найден, будет создан при первом сохранении");
        }
    }

    private int getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0; // или какое-то другое значение по умолчанию
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                // Если строка не может быть преобразована в число, например, в случае с текстом
                return 0; // или можно выбросить исключение в зависимости от логики
            }
        } else {
            // В случае других типов данных, например, если ячейка пустая
            return 0;
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

    @Override
    public void deleteProductById(int id) {
        products.removeIf(p -> p.getId() == id);
        saveToFile();
    }

    private void saveToFile() {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("Products");

            // Заголовки
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Count");
            headerRow.createCell(3).setCellValue("Tag ID");

            // Данные продуктов начинаются с первой строки
            for (int i = 0; i < products.size(); i++) {
                Row row = sheet.createRow(i + 1);  // Данные начинаются с индекса 1
                Product product = products.get(i);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getCount());

                // Проверка на null перед доступом к getTag()
                if (product.getTag() != null) {
                    row.createCell(3).setCellValue(product.getTag().getId());
                } else {
                    row.createCell(3).setCellValue(0);  // Если тег null, ставим дефолтное значение
                }
            }

            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
