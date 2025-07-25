module fr.esgi.pajavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires org.json;
    requires com.google.gson;
    requires java.sql;


    opens fr.esgi.pajavafx to javafx.fxml;
    exports fr.esgi.pajavafx;
    exports fr.esgi.pajavafx.controllers;
    opens fr.esgi.pajavafx.controllers to javafx.fxml;
    opens fr.esgi.pajavafx.models to javafx.base, com.google.gson;
}