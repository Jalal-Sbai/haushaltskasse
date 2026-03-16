package haushaltskasse.dao;

public class CategorieDAO {
}
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.AnchorPane?>


<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="400.0" prefWidth="600.0" xmlns="http://javafx.com/javafx/25" xmlns:fx="http://javafx.com/fxml/1" fx:controller="de.einausgaben.haushaltskasse1.ViewController">
   <children>
      <Button fx:id="clickmebutton1" layoutX="243.0" layoutY="168.0" mnemonicParsing="false" onAction="#performwelcoming" prefHeight="63.0" prefWidth="214.0" text="Klick me" />
      <Label fx:id="welcominglabel1" layoutX="271.0" layoutY="261.0" text="Label" visible="false" />
   </children>
</AnchorPane>



        package de.einausgaben.haushaltskasse1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ViewController {

    @FXML
    private Button clickmebutton1;

    @FXML
    private Label welcominglabel1;

    @FXML
    void performwelcoming(ActionEvent event) {
        welcominglabel1.setVisible(true);
        welcominglabel1.setText("welcome !!!");

    }

}
