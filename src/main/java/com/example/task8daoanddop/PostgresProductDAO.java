package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Класс "PostgresProductDAO" реализует интерфейс ProductDAO.
 * Использует базу данных PostgreSQL для хранения списка продуктов и категорий.
 * Позволяет загружать, добавлять, обновлять и удалять данные в БД.
 */
public class PostgresProductDAO implements ProductDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/Products";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123123123";

    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();

    public PostgresProductDAO() {
        loadTags();
        loadProducts();
    }

    private void loadTags() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tags")) {
            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                int tagId = rs.getInt("tag_id");
                Tag tag = tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("count"), tag));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        String sql = "INSERT INTO products (name, count, tag_id) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setInt(2, count);
            stmt.setInt(3, tag.getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    products.add(new Product(generatedId, name, count, tag));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product, String newName, int newCount, Tag newTag) {
        String sql = "UPDATE products SET name = ?, count = ?, tag_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newCount);
            stmt.setInt(3, newTag.getId());
            stmt.setInt(4, product.getId());
            stmt.executeUpdate();
            product.setName(newName);
            product.setCount(newCount);
            product.setTag(newTag);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getId());
            stmt.executeUpdate();
            products.remove(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTag(int id, String name) {
        String sql = "INSERT INTO tags (id, name) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.executeUpdate();
            tags.add(new Tag(id, name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}