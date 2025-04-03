package com.example.task8daoanddop;

public class Config {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final String excelPath;

    // Конструктор по умолчанию
    public Config() {
        this(
                System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/Products"),
                System.getenv().getOrDefault("DB_USER", "postgres"),
                System.getenv().getOrDefault("DB_PASSWORD", "123123123"),
                System.getenv().getOrDefault("EXCEL_PATH", "products.xlsx")
        );
    }

    // Конструктор с параметрами
    public Config(String dbUrl, String dbUser, String dbPassword, String excelPath) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.excelPath = excelPath;
    }

    // Геттеры
    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getExcelPath() {
        return excelPath;
    }
}