/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cjl.term;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author ciaran
 */
public class TerminalFX extends Application {
    private short width = 80;
    private short height = 24;
    private Font defaultFont = new Font("Courier New", 12);
    private Coord2D cursor = new Coord2D(0, 0);
    
    class Coord2D {
        private int x;
        private int y;

        public Coord2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
    
    class Char {
        private char character;
    }
    
    class Line {
        private List<Char> line = new ArrayList<>();
    }
    
    class Screen {
        private List<Line> screen = new ArrayList<>();
    }
    
    
    //private charp[=]
 
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("cjl Terminal");
        //Group root = new Group();
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(640, 480);
        root.setStyle("-fx-background-color: black");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        drawShapes(gc);
        root.getChildren().add(canvas);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFont(new Font("Lucia", 14));
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        
        gc.fillText("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz", 10, 30);
        //gc.fillText("Chickens", 10, 30);
        /*
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                       new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                         new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                          new double[]{210, 210, 240, 240}, 4);
        */
    }    
    
    
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
