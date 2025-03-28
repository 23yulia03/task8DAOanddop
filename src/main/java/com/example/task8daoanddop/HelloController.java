package com.example.task8daoanddop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

/**
 * Класс "HelloController" управляет пользовательским интерфейсом приложения.
 * Позволяет добавлять, редактировать, удалять продукты и теги.
 * Поддерживает выбор источника данных: PostgreSQL, Excel или In-Memory.
 */
public class HelloController {

    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Integer> colCount;
    @FXML private TableColumn<Product, String> colTag;
    @FXML private TextField nameField;
    @FXML private TextField countField;
    @FXML private ComboBox<Tag> tagComboBox;
    @FXML private TextField tagField;
    @FXML private ComboBox<String> dataSourceComboBox;
    @FXML private TextField searchField;

    private ProductDAO productDAO = new PostgresProductDAO();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Настройка таблицы
        configureTableColumns();

        // Загрузка данных
        refreshData();

        // Настройка ComboBox для выбора источника данных
        configureDataSourceComboBox();

        // Настройка поиска
        configureSearch();

        // Настройка обработчика выбора строки
        configureRowSelection();

        // Настройка внешнего вида строк таблицы
        configureTableRowFactory();
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colTag.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTag() != null ?
                                cellData.getValue().getTag().getName() : ""));
    }

    private void configureDataSourceComboBox() {
        dataSourceComboBox.setItems(FXCollections.observableArrayList(
                "PostgreSQL", "Excel", "In-Memory"));
        dataSourceComboBox.setValue("PostgreSQL");
    }

    private void configureSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts();
        });
    }

    private void configureRowSelection() {
        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        fillFieldsWithSelectedProduct(newValue);
                    }
                });
    }

    private void configureTableRowFactory() {
        table.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    row.setStyle("-fx-background-color: #0e74be;");
                } else {
                    row.setStyle("");
                }
            });
            return row;
        });
    }

    private void fillFieldsWithSelectedProduct(Product product) {
        nameField.setText(product.getName() != null ? product.getName() : "");
        countField.setText(String.valueOf(product.getCount()));

        if (product.getTag() != null) {
            tagComboBox.getSelectionModel().select(product.getTag());
        } else {
            tagComboBox.getSelectionModel().clearSelection();
        }
    }

    private void refreshData() {
        allProducts.setAll(productDAO.getProducts());
        table.setItems(allProducts);
        tagComboBox.setItems(productDAO.getTags());
    }

    @FXML
    private void searchProducts() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            table.setItems(allProducts);
            return;
        }

        ObservableList<Product> filteredProducts = allProducts.stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(searchText)) ||
                        (p.getTag() != null && p.getTag().getName() != null &&
                                p.getTag().getName().toLowerCase().contains(searchText)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        table.setItems(filteredProducts);
    }

    @FXML
    private void resetSearch() {
        searchField.clear();
        table.setItems(allProducts);
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
            refreshData();
            clearFields();
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
            refreshData();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Количество должно быть числом!");
        }
    }

    @FXML
    private void deleteProduct() {
        Product selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            productDAO.deleteProduct(selected);
            refreshData();
            clearFields();
        } else {
            showAlert("Ошибка", "Выберите продукт для удаления!");
        }
    }

    @FXML
    private void addTag() {
        String tagName = tagField.getText();
        if (!tagName.isEmpty()) {
            productDAO.addTag(productDAO.getTags().size() + 1, tagName);
            tagComboBox.setItems(productDAO.getTags());
            tagField.clear();
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
        table.getSelectionModel().clearSelection();
    }

    @FXML
    private void cancelSelection() {
        clearFields();
    }

    @FXML
    private void switchDataSource() {
        String selectedSource = dataSourceComboBox.getValue();

        if ("PostgreSQL".equals(selectedSource)) {
            productDAO = new PostgresProductDAO();
        } else if ("Excel".equals(selectedSource)) {
            productDAO = new ExcelProductDAO();
        } else if ("In-Memory".equals(selectedSource)) {
            productDAO = new ListProductDAO();
        }

        refreshData();
        clearFields();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}