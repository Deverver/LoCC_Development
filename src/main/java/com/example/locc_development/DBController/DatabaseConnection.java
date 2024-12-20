package com.example.locc_development.DBController;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/LoCC";

    private static String USER = "";
    private static String PASSWORD = "";

    public static Connection getConnection() {

        try {
            getLogin();
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }// Connection End

    public static void createFiles() throws IOException {
        File file1 = new File("username");
        File file2 = new File("password");
        if (!file1.exists() && !file2.exists()) {
            if (file1.createNewFile() && file2.createNewFile()) {
                System.out.println("File Created");
            } else {
                System.out.println("File Not Created");
            }
        } else {
            System.out.println("Files Already Exists");
        }
    }

    public static void getLogin() throws FileNotFoundException {
        BufferedReader userReader = new BufferedReader(new FileReader("username"));
        BufferedReader passReader = new BufferedReader(new FileReader("password"));
        try {
            USER = userReader.readLine();
            PASSWORD = passReader.readLine();
            userReader.close();
            passReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

