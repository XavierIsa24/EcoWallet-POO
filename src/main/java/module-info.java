module com.projeto.ecowallet {

    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.projeto.ecowallet to javafx.fxml;
    opens com.projeto.ecowallet.controller to javafx.fxml;
    opens com.projeto.ecowallet.model to
            com.fasterxml.jackson.databind,
            com.fasterxml.jackson.annotation;

    exports com.projeto.ecowallet;
    exports com.projeto.ecowallet.controller;
    exports com.projeto.ecowallet.model;
    exports com.projeto.ecowallet.factory;
    exports com.projeto.ecowallet.persistence;
}