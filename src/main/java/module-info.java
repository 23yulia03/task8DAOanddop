module com.example.task8daoanddop {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;


    opens com.example.task8daoanddop to javafx.fxml;
    exports com.example.task8daoanddop;
}