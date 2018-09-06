package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLOutput;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Main extends Application {

    boolean debug=false;

    private final AtomicReference<Float> x = new AtomicReference<>(0.0f);    //without draw funct like in processing easy to forget u need atomic ref in th callback
    private final AtomicReference<Float> y = new AtomicReference<>(0.0f);
    private final AtomicInteger hsweep = new AtomicInteger();
    private final AtomicReference<Float> hue = new AtomicReference<>(0.7f);
    private final AtomicReference<Float> saturation = new AtomicReference<>(0.9f);
    private final AtomicReference<Float> brightness = new AtomicReference<>(0.8f);
    private float spacing= 20.0f;


    @Override
    public void start(Stage primaryStage) throws Exception{
        //  Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Pane mypane=new Pane();
        mypane.setPrefSize(600,600);



        Timeline time=new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Line choosen;
                if(Math.random()>.8){
                    choosen = new Line();
                    choosen.setStartX(x.get());
                    choosen.setStartY(y.get());
                    choosen.setEndX(x.get() + spacing);
                    choosen.setEndY(y.get() + spacing);
                    x.set(x.get()+spacing);
                }else{
                    choosen = new Line();
                    choosen.setStartX(x.get());
                    choosen.setStartY(y.get() + spacing);
                    choosen.setEndX(x.get() + spacing);
                    choosen.setEndY(y.get());
                    x.set(x.get()+spacing);
                }
                //styling
                choosen.setStrokeWidth(5);

                //clearing
                if(debug)System.out.print( (int) choosen.getLayoutBounds().getMinX() +", "+(int) choosen.getLayoutBounds().getMaxY()+ " : \t\t");

                for(int x=0;x<mypane.getChildren().size();x++ ){
                    Node node=mypane.getChildren().get(x);
                    //simply a collide function. wth output true evn on partial intersect
                    if(debug)System.out.print((int)node.getLayoutBounds().getMinX() +"\t" );if(debug)System.out.print((int)node.getLayoutBounds().getMaxY() +"\t \t" );
                    if (((int) node.getLayoutBounds().getMinX() >= (int) choosen.getLayoutBounds().getMinX()))
                        if (((int) node.getLayoutBounds().getMaxX() <= (int) choosen.getLayoutBounds().getMaxX())) {
                            if(debug)System.out.print("R");
                            if (((int) node.getLayoutBounds().getMinY() >= (int) choosen.getLayoutBounds().getMinY()))
                                if (((int) node.getLayoutBounds().getMaxY() <= (int) choosen.getLayoutBounds().getMaxY())) {
                                    if(debug)System.out.print("C\t \t");
                                    mypane.getChildren().remove(node);

                                    choosen.setStroke(Color.hsb(hue.get(), saturation.get(), brightness.get()));
                                } else {
                                    //do nothing is jst a new line
                                }
                            else {
                                //do nothing is jst a new line
                            }
                        } else {
                            //do nothing
                        }
                    else {
                        //do nothing
                    }
                }
                if(debug)System.out.println("");
                choosen.setStroke(Color.hsb(hue.get(), saturation.get(), brightness.get()));


                mypane.getChildren().add(choosen);
                if(x.get() >mypane.getPrefWidth()){
                    y.set(y.get()+spacing);
                    x.set(0.0f);
                    if(y.get()>mypane.getPrefHeight()){
                        y.set(0.0f);
                        if(debug)System.out.println("  ");
                        if(debug)System.out.println(" started ");
                        hsweep.set(hsweep.get()+1);
                        if(hue.get()<=360){
                            hue.set(hue.get()+10);
                        }else{
                            hue.set(0.4f);
                        }
                    }
                }
            }
        });
        time.getKeyFrames().add(keyFrame);

        time.play();
        if(debug)System.out.println( "x "+ x +" y"+y);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(mypane));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
