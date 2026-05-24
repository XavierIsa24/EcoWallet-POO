module com.projeto.ecowallet {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.projeto.ecowallet to javafx.fxml;
    opens com.projeto.ecowallet.controller to javafx.fxml;

    exports com.projeto.ecowallet;
    exports com.projeto.ecowallet.controller;
}