package com.example.locc_development.Controller;

import com.example.locc_development.DBController.DatabaseRepo;
import com.example.locc_development.Model.Item;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class ScenarioManager {
    DatabaseRepo databaseRepo = new DatabaseRepo();
    private int scenario_id;
    private String scenario_type;
    private String scenario_name;
    private String scenario_description;

    public ScenarioManager() {
    } //Empty constructor

    public ScenarioManager(String scenario_type, String scenario_name, String scenario_description) {
        this.scenario_type = scenario_type;
        this.scenario_name = scenario_name;
        this.scenario_description = scenario_description;
    }// Constructor without the ID, we use this to create the Scenarios locally

    public ScenarioManager(int scenario_id, String scenario_type, String scenario_name, String scenario_description) {
        this.scenario_id = scenario_id;
        this.scenario_type = scenario_type;
        this.scenario_name = scenario_name;
        this.scenario_description = scenario_description;
    }// Full constructor with id, we use this if we chose to have scenarios in the DB, in order to know their primary key

    //region Getters & Setters
    public String getScenario_type() {
        return scenario_type;
    }

    public void setScenario_type(String scenario_type) {
        this.scenario_type = scenario_type;
    }

    public String getScenario_name() {
        return scenario_name;
    }

    public void setScenario_name(String scenario_name) {
        this.scenario_name = scenario_name;
    }

    public String getScenario_description() {
        return scenario_description;
    }

    public void setScenario_description(String scenario_description) {
        this.scenario_description = scenario_description;
    }
    //endregion

    // Made to create specific files for both items and scenarios if we chose to upload via files
    public static void createScenarioFiles() throws IOException {
        File file3 = new File("scenario_data");
        File file4 = new File("item_data");
        if (!file3.exists() && !file4.exists()) {
            if (file3.createNewFile() && file4.createNewFile()) {
                System.out.println("File Created");
            } else {
                System.out.println("File Not Created");
            }
        } else {
            System.out.println("Files Already Exists");
        }

    }
    /*
    public ScenarioManager createScenario() {
        ScenarioManager randomScenarioManager;
        randomScenarioManager = DatabaseRepo.readRandomScenarioFromDB();

        return randomScenarioManager;
    }

    public void showScenarioInfo() {
        DatabaseRepo.readScenario();

    }
    */
    public Item getItem() throws SQLException {
        // Scenario is supposed to create a random item, that will be displayed in its description
        Item randomItem;
        randomItem = databaseRepo.readRandomItemFromDB();

        //createScenario();
        //update scenario with Item
        return randomItem;
    }
    public ScenarioManager readScenario() throws SQLException {
        ScenarioManager scenarioManager;
        scenarioManager = databaseRepo.readRandomScenarioFromDB();
        return scenarioManager;
    }

    @Override
    public String toString() {
        String scenarioMessage =
                ("________________________\n" + this.getScenario_name() + "\n________________________\n" + this.getScenario_type() + "\n" + this.getScenario_description() + ".");
        return scenarioMessage;
    }
}// End
