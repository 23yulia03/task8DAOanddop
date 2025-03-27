package com.example.task8daoanddop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Products";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123123123";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Подключение успешно!");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения!");
            e.printStackTrace();
        }
    }
}
