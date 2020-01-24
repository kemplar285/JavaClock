/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaclock;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import oracle.jrockit.jfr.events.Bits;

/**
 *
 * @author User
 */
public class JavaClock extends Application {
    public static final double HEIGHT = 750;
    public static final double WIDTH = 500;
    public static double totalHourRotation = 0;
    public static double totalMinuteRotation = 0;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
       
        TextField hourRotateField = new TextField();
        hourRotateField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        hourRotateField.setLayoutY(HEIGHT-200);
        hourRotateField.setLayoutX(210);
        hourRotateField.setMinWidth(30);
        hourRotateField.setMaxWidth(30);
        
        Label symbol = new Label(":");
        symbol.setStyle("-fx-font-size: 25px;");
        symbol.setLayoutX(242);
        symbol.setLayoutY(HEIGHT-208);
        
        Label currentTime = new Label();
        currentTime.setStyle("-fx-font-size: 14px;");
        currentTime.setLayoutX(10);
        currentTime.setLayoutY(HEIGHT-195);
        
        Label angle = new Label();
        angle.setStyle("-fx-font-size: 14px;");
        angle.setLayoutX(10);
        angle.setLayoutY(HEIGHT-150);
        
        TextField minuteRotateField = new TextField();
        minuteRotateField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        minuteRotateField.setLayoutY(HEIGHT-200);
        minuteRotateField.setLayoutX(250);
        minuteRotateField.setMinWidth(30);
        minuteRotateField.setMaxWidth(30);
        
        Line hourHand = new Line();
        hourHand.setStrokeLineCap(StrokeLineCap.ROUND);
        hourHand.setStyle("-fx-stroke-width: 15;");
        hourHand.setStartX(WIDTH/2);
        hourHand.setStartY(HEIGHT/3);
        hourHand.setEndY(100);
        hourHand.setEndX(WIDTH/2);
        
        Line minuteHand = new Line();
        minuteHand.setStrokeLineCap(StrokeLineCap.ROUND);
        minuteHand.setStyle("-fx-stroke-width: 12;");
        minuteHand.setStartX(WIDTH/2);
        minuteHand.setStartY(HEIGHT/3);
        minuteHand.setEndY(80);
        minuteHand.setEndX(WIDTH/2);
                   
        Button btn = new Button("Подтвердить");
        btn.setTranslateY(HEIGHT-100);
        btn.setTranslateX(200);
        btn.setOnAction(new EventHandler<ActionEvent>() {   
            @Override
            public void handle(ActionEvent event) {
                if(hourRotateField.getText().length() > 0 && hourRotateField.getText().matches("[0-9]*")
                        && minuteRotateField.getText().length() > 0 && minuteRotateField.getText().matches("[0-9]*")){
                    
                    rotateClockHand(-totalHourRotation, hourHand);
                    rotateClockHand(-totalMinuteRotation, minuteHand);
                    
                    totalHourRotation = 0;
                    totalMinuteRotation = 0;
                    
                    Double hours = Double.valueOf(Integer.valueOf(hourRotateField.getText()) % 12);
                    Double minutes = Double.valueOf(Integer.valueOf(minuteRotateField.getText()) % 60);
                    currentTime.setText("Время на часах: " 
                            + (Integer.valueOf(hourRotateField.getText()) == 12 ? "12" : Bits.intValue(hours)) 
                            + " : " 
                            + (Integer.valueOf(minuteRotateField.getText())%60 == 0 ? "00" : Bits.intValue(minutes))
                    );
                    
                    Double hoursAngle = hours * 30 + minutes * 0.5;
                    Double minutesAngle = minutes * 6;
                    
                    totalHourRotation += hoursAngle;
                    totalMinuteRotation += minutesAngle;
                    
                    hourRotateField.clear();
                    minuteRotateField.clear();
                    
                    rotateClockHand(hoursAngle, hourHand); 
                    rotateClockHand(minutesAngle, minuteHand);
                    
                    angle.setText("Углы: \nУгол 1 - " + calculateAngles(totalHourRotation, totalMinuteRotation)[0] + "\u00B0\nУгол 2 - " + calculateAngles(totalHourRotation, totalMinuteRotation)[1] + "\u00B0");
                } else{
                    hourRotateField.clear();
                    minuteRotateField.clear();
                }
            }
        });
        
        AnchorPane root = new AnchorPane();
        root.setId("pane");
        root.getChildren().add(btn);
        root.getChildren().add(hourHand);
        root.getChildren().add(minuteHand);
        root.getChildren().add(symbol);
        root.getChildren().add(angle);
        root.getChildren().add(currentTime);
        root.getChildren().add(hourRotateField);  
        root.getChildren().add(minuteRotateField);
        
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().addAll(this.getClass().getResource("/style/style.css").toExternalForm());

        primaryStage.setTitle("Clock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void rotateClockHand(Double angle, Line line){
        Rotate rotate = new Rotate();  
        rotate.setAngle(angle);  
        rotate.setPivotX(WIDTH/2);  
        rotate.setPivotY(HEIGHT/3);
        line.getTransforms().add(rotate);
    }
    
    public static Double[] calculateAngles(double minAngle, double hourAngle){
        Double[] angles = new Double[2];
        angles[0] = Math.abs((360 - minAngle) - (360 - hourAngle));
        angles[1] = Math.abs(360 - angles[0]);
        return angles;
    }

    }
     
