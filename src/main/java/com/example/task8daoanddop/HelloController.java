package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, Integer> colId;       // Новая колонка для ID
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Integer> colCount;
    @FXML private TableColumn<Product, String> colTag;
    @FXML private TextField nameField;
    @FXML private TextField countField;
    @FXML private ComboBox<Tag> tagComboBox;
    @FXML private TextField tagField;
    @FXML private ComboBox<String> dataSourceComboBox;

    private ProductDAO productDAO = new PostgresProductDAO(); // Изначально используем PostgreSQL

    @FXML
    public void initialize() {
        // Привязка колонок к свойствам Product
        colId.setCellValueFactory(new PropertyValueFactory<>("id")); // Привязка для ID
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colTag.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTag().getName()));

        // Загрузка данных
        table.setItems(productDAO.getProducts());
        tagComboBox.setItems(productDAO.getTags());

        // Инициализация ComboBox для выбора источника данных
        dataSourceComboBox.setItems(FXCollections.observableArrayList("PostgreSQL", "Excel", "In-Memory"));
        dataSourceComboBox.setValue("PostgreSQL");
    }

    @FXML
    private void addProduct() {
        String name = nameField.getText();
        String countText = countField.getText();
        Tag tag = tagComboBox.getValue();

        if (name.isEmpty() || countText.isEmpty() || tag == null) {
            showAlert("Ошибка", "Заполните все поля!");
            return;
        }

        try {
            int count = Integer.parseInt(countText);
            productDAO.addProduct(productDAO.getProducts().size() + 1, name, count, tag);
            table.refresh();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Количество должно быть числом!");
        }
    }

    @FXML
    private void updateProduct() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите продукт для редактирования!");
            return;
        }

        String newName = nameField.getText();
        String countText = countField.getText();
        Tag newTag = tagComboBox.getValue();

        if (newName.isEmpty() || countText.isEmpty() || newTag == null) {
            showAlert("Ошибка", "Заполните все поля!");
            return;
        }

        try {
            int newCount = Integer.parseInt(countText);
            productDAO.updateProduct(selected, newName, newCount, newTag);
            table.refresh();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Количество должно быть числом!");
        }
    }

    @FXML
    private void deleteProduct() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            productDAO.deleteProduct(selected);
            table.getItems().remove(selected);
        } else {
            showAlert("Ошибка", "Выберите продукт для удаления!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addTag() {
        String tagName = tagField.getText();
        if (!tagName.isEmpty()) {
            productDAO.addTag(productDAO.getTags().size() + 1, tagName);
            tagComboBox.setItems(productDAO.getTags()); // Обновление списка тегов
            tagField.clear(); // Очистка поля ввода
        } else {
            showAlert("Ошибка", "Введите название тега!");
        }
    }

    @FXML
    private void clearFields() {
        nameField.clear();
        countField.clear();
        tagComboBox.getSelectionModel().clearSelection();
        tagField.clear();
    }

    @FXML
    private void switchDataSource() {
        String selectedSource = dataSourceComboBox.getValue();

        // Переключаем источник данных
        if ("PostgreSQL".equals(selectedSource)) {
            productDAO = new PostgresProductDAO();
        } else if ("Excel".equals(selectedSource)) {
            productDAO = new ExcelProductDAO();
        } else if ("In-Memory".equals(selectedSource)) {
            productDAO = new ListProductDAO();
        }

        // Обновляем данные в таблице
        table.setItems(productDAO.getProducts());
        tagComboBox.setItems(productDAO.getTags());
    }
}