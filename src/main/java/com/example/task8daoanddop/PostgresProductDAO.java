package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PostgresProductDAO implements ProductDAO {
    private final String url;
    private final String user;
    private final String password;
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final ObservableList<Tag> tags = FXCollections.observableArrayList();

    public PostgresProductDAO(Config config) throws SQLException {
        this.url = config.getDbUrl();
        this.user = config.getDbUser();
        this.password = config.getDbPassword();
        loadData();
    }

    private void loadData() throws SQLException {
        try (Connection conn = getConnection()) {
            loadTags(conn);
            loadProducts(conn);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void loadTags(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tags")) {
            tags.clear();
            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        }
    }

    private void loadProducts(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            products.clear();
            while (rs.next()) {
                int tagId = rs.getInt("tag_id");
                Tag tag = findTagById(tagId);
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("count"),
                        tag));
            }
        }
    }

    private Tag findTagById(int tagId) {
        return tags.stream()
                .filter(t -> t.getId() == tagId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public ObservableList<Product> getProducts() {
        return FXCollections.unmodifiableObservableList(products);
    }

    @Override
    public ObservableList<Tag> getTags() {
        return FXCollections.unmodifiableObservableList(tags);
    }

    @Override
    public void addProduct(String name, int count, Tag tag) throws SQLException {
        String sql = "INSERT INTO products (name, count, tag_id) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setInt(2, count);
            stmt.setInt(3, tag.getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    products.add(new Product(rs.getInt(1), name, count, tag));
                }
            }
        }
    }

    @Override
    public void updateProduct(Product product, String newName, int newCount, Tag newTag) throws SQLException {
        String sql = "UPDATE products SET name = ?, count = ?, tag_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newName);
            stmt.setInt(2, newCount);
            stmt.setInt(3, newTag.getId());
            stmt.setInt(4, product.getId());
            stmt.executeUpdate();

            product.setName(newName);
            product.setCount(newCount);
            product.setTag(newTag);
        }
    }

    @Override
    public void deleteProduct(Product product) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId());
            stmt.executeUpdate();
            products.remove(product);
        }
    }

    @Override
    public void addTag(int id, String name) throws SQLException {
        String sql = "INSERT INTO tags (id, name) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.executeUpdate();
            tags.add(new Tag(id, name));
        }
    }
}