package com.example.locc_development.DBController;

import com.example.locc_development.Controller.SavedInventory;
import com.example.locc_development.Controller.ScenarioManager;
import com.example.locc_development.Model.*;

import java.sql.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseRepo {
    public List<Item> item_list = new ArrayList<>();

    //region Scenario Commands
    public static void uploadToScenarios() {
        String filePath = "scenario_data";
        String sql = "INSERT INTO scenarioList(scenarioType, scenarioName, scenarioDescription) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             BufferedReader reader1 = new BufferedReader(new FileReader(filePath));
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            String line;
            while ((line = reader1.readLine()) != null) {
                // Since we are taking data from a file and not an Object, we split the String so we can use each part
                String[] values = line.split(",");
                String TYPE = values[0];
                String NAME = values[1];
                String DESCRIPTION = values[2];

                preparedStatement.setString(1, TYPE);
                preparedStatement.setString(2, NAME);
                preparedStatement.setString(3, DESCRIPTION);
                // Learned why we have to use a Batch, next comment
                preparedStatement.addBatch();
            }
            reader1.close();
            // Learned that you have to do a Batch instead of an Update, this is because our rowsInserted is an array -
            // this also makes it easier to get info out like how many Entries we have put into the DB
            int[] rowsInserted = preparedStatement.executeBatch();

            System.out.println(rowsInserted.length + " rows inserted successfully.");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void createScenario(ScenarioManager scenarioManager) {
        // In order to transfer data to the DB we have to make our object readable to MySQL
        // We do this by choosing where our object data goes in our SQL Query
        String sql = "INSERT INTO scenarioList (scenario_type, scenario_name, scenario_description) VALUES (?, ?, ?)";


        // In order to do anything in our DB we have to make a connection
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Selects where the data goes into our query
            preparedStatement.setString(1, scenarioManager.getScenario_type());
            preparedStatement.setString(2, scenarioManager.getScenario_name());
            preparedStatement.setString(3, scenarioManager.getScenario_description());

            // Should "do" execute the query, and report if something went wrong
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println(scenarioManager.getScenario_type() + " Scenario " + scenarioManager.getScenario_name() + " has been created in DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// createScenario End

    public void updateScenario(ScenarioManager scenarioManager, int DBIndex) {
        // In order to update in the DB, we have to provide and SET new data, and choose WHERE it goes
        String sql = "UPDATE scenarioList SET scenario_type = ?, scenario_name = ?, scenario_description = ?, WHERE id = " + DBIndex;

        // We have to make a new connection to our DB
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, scenarioManager.getScenario_name());
            preparedStatement.setString(2, scenarioManager.getScenario_type());
            preparedStatement.setString(3, scenarioManager.getScenario_description());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Scenario " + scenarioManager.getScenario_name() + "Type: " + scenarioManager.getScenario_type() + " has been updated in DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// updateScenario End

    // Same as updating we have to choose which game to delete via ID
    public void deleteScenario(int DBIndex) {
        String sql = "DELETE FROM scenarioList WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, DBIndex);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Scenario on DBIndex: " + DBIndex + " has been deleted from the DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// deleteScenario End


    public List<ScenarioManager> readALLScenarios() {
        List<ScenarioManager> scenarios = new ArrayList<>();
        String sql = "SELECT * FROM scenarioList";

      /*
      The reason we use a Statement is in order to transfer the read data from the DB into a result-set
      that we can read from separately.
      this way we can create object from the stored data from the ResultSet.
      */
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Loops through the resultset table, and creates the Scenario objects
            while (resultSet.next()) {
                int primaryKey = resultSet.getInt("id");
                String scenario_type = resultSet.getString("scenarioType");
                String scenario_name = resultSet.getString("scenarioName");
                String scenario_description = resultSet.getString("scenarioDescription");

                scenarios.add(new ScenarioManager(primaryKey, scenario_type, scenario_name, scenario_description));

            }// WLoop End
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scenarios;

    }// readScenario End

    public ScenarioManager readRandomScenarioFromDB() throws SQLException {

        String sql = "SELECT * FROM scenarioList ORDER BY RAND() LIMIT 1";
        ScenarioManager randomScenario;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int primaryKey = resultSet.getInt("id");
                String scenario_type = resultSet.getString("scenarioType");
                String scenario_name = resultSet.getString("scenarioName");
                String scenario_description = resultSet.getString("scenarioDescription");
                randomScenario = new ScenarioManager(scenario_type, scenario_name, scenario_description);
                return randomScenario;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region Item Commands
    public void createItem(Item item) {
        String sql = "INSERT INTO itemList (itemType, itemName, itemDescription, itemWeight, itemValue) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, item.getItem_type());
            preparedStatement.setString(2, item.getItem_name());
            preparedStatement.setString(3, item.getItem_description());
            preparedStatement.setDouble(4, item.getItem_weight());
            preparedStatement.setDouble(5, item.getItem_value());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item: " + item.getItem_name() + " type: " + item.getItem_type() + " has been created in DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// createItem End


    public Item itemBuilder(int buildItemID, String buildItemName, String buildItemType, String buildItemDescription, double buildItemWeight, double buildItemValue, int buildItemAmount, boolean buildItemStackable) {
        Item buildItem = null;
        int itemID = buildItemID;
        String itemName = buildItemName;
        String itemType = buildItemType;
        String itemDescription = buildItemDescription;
        double itemWeight = buildItemWeight;
        double itemValue = buildItemValue;
        int itemAmount = buildItemAmount;

        switch (itemType) {
            case "Weapon":
                buildItem = new Weapon(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, 1);
                break;
            case "Helmet":
                buildItem = new Armor(itemID, itemType, itemName, itemDescription, itemWeight, itemValue);
                break;
            case "Shoulder":
                buildItem = new Armor(itemID, itemType, itemName, itemDescription, itemWeight, itemValue);
                break;
            case "Chest":
                buildItem = new Armor(itemID, itemType, itemName, itemDescription, itemWeight, itemValue);
                break;
            case "Legs":
                buildItem = new Armor(itemID, itemType, itemName, itemDescription, itemWeight, itemValue);
                break;
            case "Boots":
                buildItem = new Armor(itemID, itemType, itemName, itemDescription, itemWeight, itemValue);
                break;
            case "Consumable":
                buildItem = new Consumable(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, itemAmount);
                break;
            case "Resource":
                buildItem = new Resource(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, itemAmount);
                break;
        }

        return buildItem;
    }


    public Item readRandomItemFromDB() throws SQLException {
        String sql = "SELECT * FROM itemList ORDER BY RAND() LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                Item buildItem = null;
                int itemID = resultSet.getInt("itemID");
                String itemName = resultSet.getString("itemName");
                String itemType = resultSet.getString("itemType");
                String itemDescription = resultSet.getString("itemDescription");
                double itemWeight = resultSet.getFloat("itemWeight");
                double itemValue = resultSet.getFloat("itemValue");
                boolean itemStackable = resultSet.getBoolean("itemStackable");
                switch (itemType) {
                    case "Weapon":
                        buildItem = new Weapon(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, 1);
                        break;
                    case "Armor":
                        buildItem = new Armor(itemID, itemType, itemName, itemDescription, itemWeight, itemValue);
                        break;
                    case "Consumable":
                        buildItem = new Consumable(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, 1);
                        break;
                    case "Resource":
                        buildItem = new Resource(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, 1);
                        break;
                }
                return buildItem;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }// readItem End

    public List<Item> readAllItemsFromDB() {
        item_list.clear();
        String sql = "SELECT * FROM itemList";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            int itemID = resultSet.getInt("itemID");
            String itemName = resultSet.getString("itemName");
            String itemType = resultSet.getString("itemType");
            String itemDescription = resultSet.getString("itemDescription");
            double itemWeight = resultSet.getFloat("itemWeight");
            double itemValue = resultSet.getFloat("itemValue");
            boolean itemStackable = resultSet.getBoolean("itemStackable");

            Item item2 = itemBuilder(itemID, itemName, itemType, itemDescription, itemWeight, itemValue,1, itemStackable);

            item_list.add(item2);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item_list;
    }// readAllItems End

    public void updateItem(Item item, int DBIndex) {
        String sql = "UPDATE Itemlist SET itemName = ?, itemType = ?, itemDescription = ?, itemWeight = ?, itemValue = ?,itemStackable = ? WHERE itemID = " + DBIndex;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, item.getItem_name());
            preparedStatement.setString(2, item.getItem_type());
            preparedStatement.setString(3, item.getItem_description());
            preparedStatement.setDouble(4, item.getItem_weight());
            preparedStatement.setDouble(5, item.getItem_value());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Item " + item.getItem_name() + " has been updated in DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// updateItem End

    public void deleteItem(Item item, int DBIndex) {
        String sql = "DELETE FROM Itemlist WHERE itemID = " + DBIndex;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, item.getItem_id());
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Item from DB index: " + DBIndex + "has been deleted from DB");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting item with DB index: " + DBIndex);
            e.printStackTrace();
        }
    }// deleteItem End
    //endregion

    //region Inventory Commands
    public void createSavedInventory(ArrayList<SavedInventory> savedInv) {
        String sql = "INSERT INTO Savedinventory (Fkitemid, Amount,) VALUES (?, ?,)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (SavedInventory savedInventory : savedInv) {
                preparedStatement.setInt(1, savedInventory.getAmount());
                preparedStatement.setInt(2, savedInventory.getFk());
            }
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Your inventory has been opdated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Item> readSavedInventory() {
        ArrayList<Item> savedinventories = new ArrayList<>();
        String sql = "SELECT  amount, itemName, itemType, itemDescription, itemWeight, itemValue FROM Savedinventory,Itemlist WHERE ID = itemID ";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int itemID = resultSet.getInt("itemID");
                int amount = resultSet.getInt("amount");
                String itemName = resultSet.getString("itemName");
                String itemType = resultSet.getString("itemType");
                String itemDescription = resultSet.getString("itemDescription");
                double itemWeight = resultSet.getFloat("itemWeight");
                double itemValue = resultSet.getFloat("itemValue");
                boolean itemStackable = resultSet.getBoolean("itemStackable");

                savedinventories.add(itemBuilder(itemID, itemType, itemName, itemDescription, itemWeight, itemValue, amount, itemStackable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // get data from or DB

        // Convert Data to a new Sql Query to call ItemList

        // get data from itemlist DB

        // build items from the data

        // add items to the presumed empty inventory on system startup


        return savedinventories;
    }

    public void updateSavedInventory(SavedInventory savedinventory) {
        String sql = "UPDATE Savedinventory SET Fkitemid=?, Amount=?, WHERE id=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, savedinventory.getFk());
            preparedStatement.setInt(2, savedinventory.getAmount());


            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("The inventory has been opdated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSavedInventory(int id) {
        String sql = "DELETE FROM Savedinventory WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Item has been deleted from inventory");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion
}// DatabaseRepo End

