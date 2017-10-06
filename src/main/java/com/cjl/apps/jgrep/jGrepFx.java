/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.apps.jgrep;

import com.cjl.streams.NumberedLine;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author cpd
 */
public class jGrepFx extends Application {

    public static final ObservableList data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("jGrep");

        final HBox hbox = new HBox();
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        final VBox vbox = new VBox();
        vbox.setPadding(new Insets(5, 5, 5, 5));
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #336699;");

        final HBox pathHbox = new HBox();
        pathHbox.setSpacing(2);
        pathHbox.setStyle("-fx-background-color: #336699;");

        final Label pathLabel = new Label("Path");
        final TextField pathTextField = new TextField();
        pathTextField.setText("C:\\");
        //pathTextField.
                
        final Button pathButton = new Button();
        pathButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory == null) {
                System.out.println("No Directory selected");
            } else {
                pathTextField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        pathHbox.getChildren().add(pathLabel);
        pathHbox.getChildren().add(pathTextField);
        pathHbox.getChildren().add(pathButton);

        final Label globLabel = new Label("Glob");
        final TextField globTextField = new TextField();

        final Label patternLabel = new Label("Pattern");
        final TextField patternTextField = new TextField();

        final Button findButton = new Button("Find");

        final TableView<NumberedLine> mainTable = new TableView<>();
        mainTable.setEditable(false);
        mainTable.setMaxWidth(Double.MAX_VALUE);

        TableColumn filenameCol = new TableColumn("File Name");
        TableColumn noCol = new TableColumn("No");
        TableColumn lineCol = new TableColumn("Line");

        noCol.setCellValueFactory(
                new PropertyValueFactory<>("number")
        );
        lineCol.setCellValueFactory(
                new PropertyValueFactory<>("line")
        );
        filenameCol.setCellValueFactory(
                new PropertyValueFactory<>("path")
        );
        mainTable.getColumns().addAll(noCol, lineCol, filenameCol);

        mainTable.setItems(data);

        findButton.setOnAction(e -> {
            Search search = new Search(pathTextField.getText(),
                    globTextField.getText(),
                    patternTextField.getText());

            try {
                search.run(false);

                data.clear();
                search.getResults().stream().forEach(data::add);
            } catch (IOException ex) {
                Logger.getLogger(jGrepFx.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//        hbox.getChildren().add(pathLabel);
//        hbox.getChildren().add(pathTextField);
        hbox.getChildren().add(pathHbox);

        hbox.getChildren().add(globLabel);
        hbox.getChildren().add(globTextField);

        hbox.getChildren().add(patternLabel);
        hbox.getChildren().add(patternTextField);

        hbox.getChildren().add(findButton);

        vbox.getChildren().add(hbox);
        vbox.getChildren().add(mainTable);

        final StackPane root = new StackPane();
        root.getChildren().add(vbox);

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }
}
