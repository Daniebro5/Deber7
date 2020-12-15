/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deber7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author dannibrito
 */
public class Deber7 extends Application {
    
    Stage mainstage; // Pantalla principal
    Random rnd = new Random(); //Generador de Aleatoios
    TextField txtNivel; // NIvel de recursividad
    
    int max; // Número de puntos trayectoria máxima
    
    public Group getCuadros () {
        Group grupo = new Group();
        
        double anchoEscena = 512;
        double altoEscena = 512;
        
        Rectangle fondo = new Rectangle(0, 0, anchoEscena, altoEscena);
        fondo.setFill(Color.TRANSPARENT);
        grupo.getChildren().add(fondo);
        
        int nivel = Integer.valueOf( txtNivel.getText() );
        
        double anchoInicial = 128;
        
        cuadro((anchoEscena - anchoInicial)/2, (altoEscena - anchoInicial)/2, anchoInicial, nivel, grupo);
        
        
        return grupo;
    }
    
    public void cuadro(double x, double y, double lado, int n, Group grupo) {

        Color colorL = Color.hsb(20+n*15, .8, .7);
        
        if(n == 1) {
            Rectangle cuadro = new Rectangle(x, y, lado * 3, lado * 3);
            cuadro.setFill( colorL );
            grupo.getChildren().add(cuadro);
            return;
        }
        
        cuadro(x, y, lado/3, n - 1, grupo);
        cuadro(x - lado, y - lado, lado/3, n - 1, grupo);
        cuadro(x + lado, y - lado, lado/3, n - 1, grupo);
        cuadro(x - lado, y + lado, lado/3, n - 1, grupo);
        cuadro(x + lado, y + lado, lado/3, n - 1, grupo);

    }
    
    public Group getCirculos () {
        Group grupo = new Group();
        
        double ancho = 512;
        double alto = 512;
        
        Rectangle fondo = new Rectangle(0, 0, ancho, alto);
        fondo.setFill( Color.TRANSPARENT );
        grupo.getChildren().add(fondo);
        
        int nivel = Integer.valueOf( txtNivel.getText() );
        double lado=128;
        
        circulo((ancho-lado)/2, 10, lado, 0, nivel, grupo);
               
        return grupo;
    }
    
    public void circulo(double x, double y, double lado, double anguloBase, int n, Group grupo) {
        
        double anguloA = 30, anguloB = 50;

        if (n == 0) {
            return;
        }
        
        double anguloC = 180 - anguloA - anguloB;
        double ladoB = Math.sin(Math.toRadians(anguloB)) * lado / Math.sin(Math.toRadians(anguloC));
        double ladoA = Math.sin(Math.toRadians(anguloA)) * lado / Math.sin(Math.toRadians(anguloC));
    
        //Color colorL = Color.hsb(280, 0.8, 1-Math.pow(0.8, n));
        Color colorL = Color.hsb(20+n*15, .8, .7);
        //Random rnd = new Random();
        //Color colorL = Color.hsb(20+rnd.nextInt(300), .8, .7);

        Rectangle cuadro = new Rectangle(x, y, lado, lado);
        //cuadro.setStroke(Color.BLACK);
        //cuadro.setStrokeWidth(1);
        cuadro.setFill( colorL );
        cuadro.getTransforms().add( new Rotate( anguloBase, x, y ) );
       
        // Dibujar el cuadro izquierdo
        double xd = x - lado*Math.sin(Math.toRadians(anguloBase));
        double yd = y + lado*Math.cos(Math.toRadians(anguloBase));
        circulo(xd, yd, ladoB, anguloBase+anguloA, n-1, grupo);
        
        // Dibujar el cuadro derecho
        double xdB = xd + ladoB*Math.cos(Math.toRadians(anguloBase+anguloA));
        double ydB = yd + ladoB*Math.sin(Math.toRadians(anguloBase+anguloA));
        circulo(xdB, ydB, ladoA, anguloBase-anguloB, n-1, grupo); 
        
        grupo.getChildren().add(cuadro);

    }
    
    @Override
    public void start(Stage primaryStage) {
        mainstage = primaryStage;
        
        Insets insets = new Insets(4);
        
        BorderPane root = new BorderPane();
        
        HBox botones = new HBox();
        botones.setSpacing(4);
        
        Label lblNivel = new Label("Nivel:");
        txtNivel = new TextField("3");
        txtNivel.setPrefWidth(60);
        txtNivel.setPromptText("nivel");
        
        Button btnSalir = new Button();
        btnSalir.setText("Salir");
        btnSalir.setOnAction( e -> mainstage.close() );
        
        Button btnCuadros = new Button();
        btnCuadros.setText("Fractal A");
        btnCuadros.setOnAction( e -> {
            root.setCenter(null);
            root.setCenter( getCuadros() );
                });
        
        Button btnCirculos = new Button();
        btnCirculos.setText("Fractal B");
        btnCirculos.setOnAction( e -> {
            root.setCenter(null);
            root.setCenter( getCirculos() );
                });

        botones.getChildren().addAll(lblNivel, txtNivel, btnCuadros, btnCirculos, btnSalir);
        
        BorderPane.setMargin(botones, insets);
        root.setBottom(botones);
        
        root.setCenter( getCuadros() );
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Recursividad");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
