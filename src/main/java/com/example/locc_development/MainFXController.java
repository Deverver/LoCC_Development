package com.example.locc_development;

import com.example.locc_development.Controller.InventoryManager;
import com.example.locc_development.Controller.ScenarioManager;
import com.example.locc_development.DBController.DatabaseRepo;

import com.example.locc_development.Model.Consumable;
import com.example.locc_development.Model.Item;
import com.example.locc_development.Model.Resource;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.io.IOException;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private String[] sortType = {"Alphabetically", "Weight", "Value"};

    //endregion

    public ArrayList<String> shownInventory = inventoryManager.shownNames();

    String chosenItem;
    String yesNoAnswer = null;


    // We have to use the Initializable interface in order to use a choiceBox, this is because the listView and the ChoiceBox is not populated with data, or any actions.
    // Therefor we have to give them data, that we then use later in other methods
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventoryListView.getItems().addAll(shownInventory);
        inventoryListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            // We use a changeListener in order to set a Label to the Item from the ListView that has been selected
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                chosenItem = inventoryListView.getSelectionModel().getSelectedItem();
                String rebuildItem = (chosenItem);
                selectedItem.setText(rebuildItem);
            }

        });


        sortInventoryChoiceBox.getItems().addAll(sortType);
        // In order link setting and getting the information from the choicebox, we use the "::" a reference operator.
        sortInventoryChoiceBox.setOnAction(this::getSortTypeChoice);
        refreshInventoryData();
    }


    public void getSortTypeChoice(ActionEvent event) {
        String chosenSortType = sortInventoryChoiceBox.getValue();
    }

    public void refreshInventory() {
        inventoryListView.getItems().clear();
        shownInventory.clear();
        shownInventory = inventoryManager.shownNames();
        inventoryListView.getItems().addAll(shownInventory);
    }

    public void refreshInventoryData() {
        InventorySlots.setText("Inventory Slots: " + inventoryManager.getCurrentInventorySize() + "/" + inventoryManager.getCurrentMaxCapacity());
        GoldAmount.setText("Inventory Gold: " + inventoryManager.getGoldAmount());

        DecimalFormat df = new DecimalFormat("#.##");
        String weightDisplay = ("Inventory Weight: " + df.format(inventoryManager.getCurrentWeight()) + "/" + inventoryManager.getWeightLimit());

        inventoryWeight.setText(weightDisplay);
    }


    // Button Click Area
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
        String splittedItem = chosenItem;
        String[] parts = splittedItem.split("'");
        String part1 = parts[0]; // slot [i]
        String part2 = parts[1]; // item name
        String part3 = parts[2]; // item values


        Item itemToDelete = inventoryManager.searchInventory(part2);
        if (itemToDelete instanceof Consumable || itemToDelete instanceof Resource) {
            int amount = inventoryInputField.getText().equals("") ? 1 : Integer.parseInt(inventoryInputField.getText());
            inventoryManager.removeAmountFromInventory(itemToDelete, amount);
        } else {
            inventoryManager.removeFromInventory(itemToDelete);
        }
        refreshInventory();
        refreshInventoryData();
    }

    @FXML
    public void onUpgradeButtonClick() throws IOException {
        inventoryManager.upgradeInventory();
        refreshInventoryData();
    }

    @FXML
    public void onInventoryGoOnAdventureButtonClick() throws IOException {
        Item item = null;
        ScenarioManager scenario = null;
        try {
            scenario = scenarioManager.readScenario();
            item = scenarioManager.getItem();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        inventoryFeedBackTextArea.setText(scenario.toString() + "\n" + item.showItemInfo());
        inventoryManager.addToInventory(item);
        refreshInventory();
        refreshInventoryData();
    }


    @FXML
    public void onInventorySortInventoryButtonClick() throws IOException {
        switch (sortInventoryChoiceBox.getValue()) {
            case "Alphabetically":
                inventoryFeedBackTextArea.setText("Sorted by Alphabetical Order");
                inventoryManager.sortInventoryAlpha();
                refreshInventory();
                break;

            case "Weight":
                inventoryFeedBackTextArea.setText("Sorted by Weight Value");
                inventoryManager.sortInventoryWeight();
                refreshInventory();
                break;

            case "Value":
                inventoryFeedBackTextArea.setText("Sorted by Item Value");
                inventoryManager.sortInventoryValue();
                refreshInventory();
                break;
        }// Switch End
    }// onInventorySortInventoryButtonClick End

    @FXML
    public void onInventoryExitButtonClick() {
        System.exit(0);
    }


    // Button Drag Area

    // Messages Area


}// MainFXController End