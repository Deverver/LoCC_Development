package com.example.locc_development;

import com.example.locc_development.Controller.MenuController;
import com.example.locc_development.DBController.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    // JavaFX Scene Builder Start
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("startup_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 360);
        stage.setTitle("Launch Screen!");
        stage.setScene(scene);
        stage.show();
    }// JavaFX Scene Builder Start End


    public static void main(String[] args) {
        MenuController menuController = new MenuController();

        try {
            DatabaseConnection.createFiles();
        } catch (IOException e2) {
            throw new RuntimeException(e2.getMessage());
        } catch (Exception e3) {
            e3.printStackTrace();
        }

        boolean run = true;
        menuController.menu(run);

        //launch();



    }// main End
}// Main END