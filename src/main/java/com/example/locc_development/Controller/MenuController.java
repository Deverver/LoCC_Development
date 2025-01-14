package com.example.locc_development.Controller;


import com.example.locc_development.DBController.DatabaseRepo;
import com.example.locc_development.Model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuController {
    private Scanner input = new Scanner(System.in);
    private final InventoryManager inventoryManager = new InventoryManager();
    private final ScenarioManager scenarioManager = new ScenarioManager();
    private final DatabaseRepo database = new DatabaseRepo();

    public boolean menu(boolean running) {
        while (running) {
            System.out.println("""
                    __________________________________
                    ********** MENU SYSTEM **********
                    
                    These are your options:
                    [1] Show Inventory
                    [2] Scenario Options -Gain random item-
                    [3] Sell an Item
                    [4] Upgrade Bag space
                    [5] Exit program
                    """);

            switch (input.nextInt()) {
                case 1:
                    if (inventoryManager.inventoryEmpty()) {
                        System.out.println(inventoryManager.inventoryEmpty());
                    } else {
                        int itemSlot = 1;
                        for (Item item : inventoryManager.showInventory()) {
                            if (item.getItem_type().equals("Consumable")) {
                                System.out.println("Inventory space " + itemSlot + " contains " + item.getItem_type() + " " + item.getItem_name() + " " + ((Consumable) item).getItemAmount() + " Stack weight " + item.getItem_weight());
                            } else if (item.getItem_type().equals("Resource")) {
                                System.out.println("Inventory space " + itemSlot + " contains " + item.getItem_type() + " " + item.getItem_name() + " " + ((Resource) item).getItemAmount() + " Stack weight " + item.getItem_weight());
                            } else {
                                System.out.println("Inventory space " + itemSlot + " contains " + item.getItem_type() + " " + item.getItem_name() + " Item weight " + item.getItem_weight());
                            }
                            itemSlot++;
                        }// loop

                        System.out.println("""
                                __________________________________
                                ************ MENU [1] ************
                                
                                These are your options:
                                [1] Sort Inventory
                                [2] Search Inventory
                                """);
                        if (input.nextInt() == 1) {
                            System.out.println("""
                                    __________________________________
                                    ******** SUB MENU [1] [1] ********
                                         - - Inventory Sorting - -
                                    Select how Inventory is Sorted:
                                    [1] Sort by Alphabetical Order
                                    [2] Sort by Weight Value
                                    [3] Sort by Item Value
                                    """);

                            switch (input.nextInt()) {
                                case 1:
                                    System.out.println("Sorted Inventory by Alphabetical Order...");
                                    inventoryManager.sortInventoryAlpha();
                                    itemSlot = 1;
                                    for (Item item : inventoryManager.showInventory()) {
                                        System.out.println("Inventory space " + itemSlot + " contains " + item.getItem_type() + " " + item.getItem_name());
                                        itemSlot++;
                                    }
                                    break;
                                case 2:
                                    System.out.println("Sorted Inventory by Weight Value...");
                                    inventoryManager.sortInventoryWeight();
                                    itemSlot = 1;
                                    for (Item item : inventoryManager.showInventory()) {
                                        System.out.println("Inventory space " + itemSlot + " contains " + item.getItem_type() + " " + item.getItem_name());
                                        itemSlot++;
                                    }
                                    break;
                                case 3:
                                    System.out.println("Sorted Inventory by Item Value...");
                                    inventoryManager.sortInventoryValue();
                                    itemSlot = 1;
                                    for (Item item : inventoryManager.showInventory()) {
                                        System.out.println("Inventory space " + itemSlot + " contains " + item.getItem_type() + " " + item.getItem_name());
                                        itemSlot++;
                                    }
                                    break;
                            }// Sub Switch case
                            inventoryManager.showInventory();
                        } else if (input.nextInt() == 2) {
                            System.out.println("""
                                    __________________________________
                                    ******** SUB MENU [1] [2] ********
                                         - - Inventory Search - -
                                    
                                    Enter the name of the item you wish to search for
                                    """);
                            String userSearchInput = input.next();
                            System.out.println("Searching inventory for " + userSearchInput + "...");
                            Item item;
                            item = inventoryManager.searchInventory(userSearchInput);
                            System.out.println(item.getItem_name() + " " + item.getItem_description());
                        }
                    }
                    break;
                // Case 1 End
                case 2:
                    System.out.println("""
                            __________________________________
                            ************ MENU [2] ************
                            
                            These are your options:
                            [1] Play Random Scenario
                            [2] Show All Scenarios
                            """);

                    if (input.nextInt() == 1) {
                        System.out.println("Playing Random Scenario...");
                        Item item = null;
                        ScenarioManager scenario = null;
                        try {
                            scenario = scenarioManager.readScenario();
                            System.out.println(scenario.toString());
                            item = scenarioManager.getItem();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(item.showItemInfo());
                        System.out.println("""
                                __________________________________
                                ************ MENU [2] [1] ************
                                
                                These are your options:
                                [1] keep item
                                [2] discard item
                                """);
                        if (input.nextInt() == 1) {
                            inventoryManager.addToInventory(item);
                        } else {
                            break;
                        }

                        break;
                    } else if (input.nextInt() == 2) {
                        System.out.println("Displaying All Possible Scenarios.../n");
                        List<ScenarioManager> scenarios = database.readALLScenarios();
                        for (ScenarioManager scenario : scenarios) {
                            System.out.println(scenario.toString());
                        }


                        break;
                    }
                    break;
                // Case 2 Emd
                case 3:
                    System.out.println("""
                            __________________________________
                            ************ MENU [3] ************
                            
                            These are your options:
                            [1] Sell Item
                            [2] Sell All non equipped Items
                            """);
                    switch (input.nextInt()) {
                        case 1:
                            System.out.println("""
                                    __________________________________
                                    ******** SUB MENU [3] [1] ********
                                          - - Item Selling - -
                                    
                                    Enter the name of the item you wish to Sell
                                    """);
                            String searchedItem = input.next();
                            Item itemToDelete = inventoryManager.searchInventory(searchedItem);
                            inventoryManager.removeFromInventory(itemToDelete);
                            //database.deleteSavedInventory(inventoryManager.searchInventory(searchedItem).getItem_id());

                            break;
                        case 2:
                            System.out.println("""
                                    __________________________________
                                    ******** SUB MENU [3] [2] ********
                                      - Sell All non equipped Items -
                                    
                                    Are you sure you wish to do this?
                                               (YES / NO)
                                    """);
                            if (input.next().toLowerCase().trim().equals("yes")) {
                                for (Item item : inventoryManager.showInventory()) {

                                    if (item instanceof Consumable) {
                                        Consumable consumableItem = (Consumable) item;
                                        int timesToSell = consumableItem.getItemAmount();
                                        inventoryManager.removeFromInventory(item);
                                    }
                                    if (item instanceof Resource) {
                                        Resource ressourceItem = (Resource) item;
                                        int timesToSell = ressourceItem.getItemAmount();
                                        inventoryManager.removeFromInventory(item);
                                    }
                                    if (item instanceof Armor) {
                                        Armor armorItem = (Armor) item;
                                        boolean equipped = armorItem.isEquipped();
                                        if (!equipped){
                                            inventoryManager.removeFromInventory(item);
                                        }
                                    }
                                    if (item instanceof Weapon) {
                                        Weapon weaponItem = (Weapon) item;
                                        boolean equipped = weaponItem.isEquipped();
                                        if (!equipped){
                                            inventoryManager.removeFromInventory(item);
                                        }
                                    }
                                }
                            }
                            break;

                    }
                case 4:
                    System.out.println("""
                            __________________________________
                            ************ MENU [4] ************
                            
                            These are your options:
                            [1] Upgrade Inventory Capacity
                            """);
                    switch (input.nextInt()) {
                        case 1:
                            System.out.println("Upgrading Inventory Capacity...");
                            try {
                                inventoryManager.upgradeInventory();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    break;
                case 5:
                    System.out.println("""
                            __________________________________
                            ********** MENU SYSTEM **********
                              - - - - SHUTTING DOWN - - - -
                            __________________________________
                            """);

                    running = false;
                    break;

            }
        }// main Switch case

        return running;
    }

}


// MenuController End
