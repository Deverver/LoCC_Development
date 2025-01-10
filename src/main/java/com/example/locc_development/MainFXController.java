package com.example.locc_development;

import com.example.locc_development.Controller.InventoryManager;
import com.example.locc_development.Controller.ScenarioManager;
import com.example.locc_development.DBController.DatabaseRepo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFXController implements Initializable {
    private final InventoryManager inventoryManager = new InventoryManager();
    private final ScenarioManager scenarioManager = new ScenarioManager();
    private final DatabaseRepo database = new DatabaseRepo();



    //region FX:id Region
    // Labels
    @FXML
    private Label InventorySlots;

    @FXML
    private Label GoldAmount;

    @FXML
    private Label inventoryWeight;

    @FXML
    private Label selectedItem;

    // TextFields
    @FXML
    private TextArea inventoryFeedBackTextArea;

    @FXML
    private TextField inventoryInputField;

    // ListViews
    @FXML
    private ListView<String> inventoryListView;

    // ChoiceBoxes
    @FXML
    private ChoiceBox<String> sortInventoryChoiceBox;
    private String[] sortType = {"Alphabetically","Weight","Value"};

    //endregion

    public String[] ShownInventory;
    String chosenItem;
    String yesNoAnswer;



    // We have to use the Initializable interface in order to use a choiceBox, this is because the listView and the ChoiceBox is not populated with data, or any actions.
    // Therefor we have to give them data, that we then use later in other methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       inventoryListView.getItems().addAll(ShownInventory);
        inventoryListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
           // We use a changeListener in order to set a Label to the Item from the ListView that has been selected
            @Override
           public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            chosenItem = inventoryListView.getSelectionModel().getSelectedItem();
            selectedItem.setText(chosenItem);
            }
       });




        sortInventoryChoiceBox.getItems().addAll(sortType);
        // In order link setting and getting the information from the choicebox, we use the "::" a reference operator.
        sortInventoryChoiceBox.setOnAction(this::getSortTypeChoice);

    }

    public void getSortTypeChoice(ActionEvent event) {
        String chosenSortType = sortInventoryChoiceBox.getValue();

    }


    // Button Click Area
    @FXML
    public void onStartScreenContinueButtonClick(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("inventory_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 360);
        stage.setTitle("inventory Screen");
        stage.setScene(scene);
        stage.show();

        Main.launch();
    }

    @FXML
    public void onYesButtonClick() throws IOException {
        yesNoAnswer = "Yes";
    }

    @FXML
    public void onNoButtonClick() throws IOException {
        yesNoAnswer = "No";
    }

    @FXML
    public void onInventorySellItemButtonClick() throws IOException {

    }

    @FXML
    public void onInventoryGoOnAdventureButtonClick() throws IOException {

    }

    @FXML
    public void onInventorySortInventoryButtonClick(InventoryManager inventoryManager) throws IOException {
    switch (sortInventoryChoiceBox.getValue()) {
        case "Alphabetically":
            inventoryFeedBackTextArea.setText("Sorted by Alphabetical Order");
            inventoryManager.sortInventoryAlpha();
            inventoryListView.getItems().clear();
            inventoryListView.getItems().addAll(inventoryManager.showInventory().toString());
        break;

        case "Weight":
            inventoryFeedBackTextArea.setText("Sorted by Weight Value");
            inventoryManager.sortInventoryWeight();
            inventoryListView.getItems().clear();
            inventoryListView.getItems().addAll(inventoryManager.showInventory().toString());
        break;

        case "Value":
            inventoryFeedBackTextArea.setText("Sorted by Item Value");
            inventoryManager.sortInventoryValue();
            inventoryListView.getItems().clear();
            inventoryListView.getItems().addAll(inventoryManager.showInventory().toString());
        break;
    }// Switch End
    }// onInventorySortInventoryButtonClick End

    @FXML
    public void onInventoryExitButtonClick() throws IOException {
        System.exit(0);
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public DatabaseRepo getDatabase() {
        return database;
    }


    // Button Drag Area

    // Messages Area



}