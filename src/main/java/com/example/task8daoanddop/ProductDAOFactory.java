package com.example.task8daoanddop;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.SQLException;

 // Абстрактная фабрика для переключения
public class ProductDAOFactory {
    public static ProductDAO createProductDAO(String dataSourceType) throws SQLException {
        Config config = new Config(); // Используем конструктор по умолчанию

        try {
            switch (dataSourceType) {
                case "In-Memory":
                    return new ListProductDAO();
                case "PostgreSQL":
                    return new PostgresProductDAO(config);
                case "Excel":
                    return new ExcelProductDAO(config);
                default:
                    throw new IllegalArgumentException("Неизвестный тип источника данных");
            }
        } catch (SQLException e) {
            throw new SQLException("Ошибка создания DAO: " + e.getMessage(), e);
        }
    }
}