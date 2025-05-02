package com.example.task8daoanddop;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ProductDatabaseManager {
    private final ProductDAO postgresDAO;
    private final ProductDAO excelDAO;
    private final ProductDAO memoryDAO;

    public ProductDatabaseManager(Config config) throws SQLException {
        this.postgresDAO = new PostgresProductDAO(config);
        this.excelDAO = new ExcelProductDAO(config);
        this.memoryDAO = new ListProductDAO();
    }

    public void addProductToAll(String name, int count, Tag tag) throws SQLException {
        postgresDAO.addProduct(name, count, tag);
        excelDAO.addProduct(name, count, tag);
        memoryDAO.addProduct(name, count, tag);
    }

    public void updateProductInAll(Product product, String newName, int newCount, Tag newTag) throws SQLException {
        int id = product.getId();
        // Получаем продукт из каждого DAO по ID
        Product postgresProduct = findProductById(postgresDAO.getProducts(), id);
        Product excelProduct = findProductById(excelDAO.getProducts(), id);
        Product memoryProduct = findProductById(memoryDAO.getProducts(), id);

        if (postgresProduct != null)
            postgresDAO.updateProduct(postgresProduct, newName, newCount, newTag);
        if (excelProduct != null)
            excelDAO.updateProduct(excelProduct, newName, newCount, newTag);
        if (memoryProduct != null)
            memoryDAO.updateProduct(memoryProduct, newName, newCount, newTag);
    }

    private Product findProductById(ObservableList<Product> products, int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void deleteProductFromAll(Product product) throws SQLException {
        // Удаляем по ID, а не по объекту
        int id = product.getId();
        postgresDAO.deleteProductById(id);
        excelDAO.deleteProductById(id);
        memoryDAO.deleteProductById(id);
    }

    public ProductDAO getDAO(String source) {
        return switch (source) {
            case "Excel" -> excelDAO;
            case "In-Memory" -> memoryDAO;
            default -> postgresDAO;
        };
    }
}
