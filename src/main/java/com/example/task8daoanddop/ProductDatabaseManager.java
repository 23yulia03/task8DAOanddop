package com.example.task8daoanddop;

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
        postgresDAO.updateProduct(product, newName, newCount, newTag);
        excelDAO.updateProduct(product, newName, newCount, newTag);
        memoryDAO.updateProduct(product, newName, newCount, newTag);
    }

    public void deleteProductFromAll(Product product) throws SQLException {
        postgresDAO.deleteProduct(product);
        excelDAO.deleteProduct(product);
        memoryDAO.deleteProduct(product);
    }

    public ProductDAO getDAO(String source) {
        return switch (source) {
            case "Excel" -> excelDAO;
            case "In-Memory" -> memoryDAO;
            default -> postgresDAO;
        };
    }
}
