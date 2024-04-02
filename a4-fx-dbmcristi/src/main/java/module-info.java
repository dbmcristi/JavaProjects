module com.example.a4fxdbmcristi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.a4fxdbmcristi to javafx.fxml;
    exports com.example.a4fxdbmcristi;
}