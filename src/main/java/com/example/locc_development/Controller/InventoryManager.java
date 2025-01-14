package com.example.locc_development.Controller;

import com.example.locc_development.DBController.DatabaseRepo;
import com.example.locc_development.Model.Consumable;
import com.example.locc_development.Model.Inventory;
import com.example.locc_development.Model.Item;
import com.example.locc_development.Model.Resource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InventoryManager {
    private double goldAmount;
    private double weightLimit = 50;
    private double currentWeight;
    private double remainingWeightCapacity;

    private int absoluteMaxCapacity = 192;
    private int upgradeValue = 32;
    private int currentMaxCapacity = 32;

    private final Inventory inventory;
    private final DatabaseRepo databaseRepo = new DatabaseRepo();

    // Constructor
    public InventoryManager() {
        this.inventory = new Inventory();
    }

    //region Getters & Setters
    public double getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(double goldAmount) {
        this.goldAmount = goldAmount;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(double weightLimit) {
        this.weightLimit = weightLimit;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getRemainingWeightCapacity() {
        return remainingWeightCapacity;
    }

    public void setRemainingWeightCapacity(double remainingWeightCapacity) {
        this.remainingWeightCapacity = remainingWeightCapacity;
    }

    public int getAbsoluteMaxCapacity() {
        return absoluteMaxCapacity;
    }

    public void setAbsoluteMaxCapacity(int absoluteMaxCapacity) {
        this.absoluteMaxCapacity = absoluteMaxCapacity;
    }

    public int getUpgradeValue() {
        return upgradeValue;
    }

    public void setUpgradeValue(int upgradeValue) {
        this.upgradeValue = upgradeValue;
    }

    public int getCurrentMaxCapacity() {
        return currentMaxCapacity;
    }

    public void setCurrentMaxCapacity(int wantedMaxCapacity) {
        if (currentMaxCapacity != wantedMaxCapacity) {
            if (inventory.setInventoryMaxCapacity(wantedMaxCapacity) == 1) {
                this.currentMaxCapacity = wantedMaxCapacity;
            }
        }
    }
    //endregion

    public int addToInventory(Item item) {
        if (item.getItem_weight() + getCurrentWeight() < getWeightLimit()) {
            inventory.addItem(item);
            refreshInventory();
            return 1;
        } else {
            return 0;
        }
    }

    public int removeFromInventory(Item item) {
        if (inventory.removeItem(item) == 1) {
            goldAmount += item.getItem_value();
            refreshInventory();
            return 1;
        } else {
            return 0;
        }
    }

    public int removeAmountFromInventory(Item item, int amountToRemove) {
        if (inventory.removeItemAmount(item, amountToRemove) == 1) {
            if (item instanceof Consumable) {
                goldAmount += ((item.getItem_value() / ((Consumable) item).getItemAmount()) * amountToRemove);
            } else if (item instanceof Resource) {
                goldAmount += ((item.getItem_value() / ((Resource) item).getItemAmount()) * amountToRemove);
            }
            refreshInventory();
            return 1;
        } else {
            return 0;
        }
    }
    public int removeUsedItem(Item item) {
        inventory.removeItemAmount(item, 1);
        return 1;
    }

    /*
    public int removeAmountFromInventory(Resource item, int amountToRemove) {
        goldAmount += (item.getItem_value() * amountToRemove);
        System.out.println(item.getItem_value() + " 2");
        System.out.println(item.getItem_value() * amountToRemove + " 3");
        if (inventory.removeItemAmount(item, amountToRemove) == 1) {
            System.out.println(goldAmount + " 1");
            System.out.println(item.getItem_value() + " 2");
            System.out.println(item.getItem_value() * amountToRemove + " 3");
            refreshInventory();
            return 1;
        } else {
            return 0;
        }
    }
*/
    public List<Item> showInventory() {

        return inventory.getContainedItems();
    }

    public int getCurrentInventorySize() {
        return inventory.getSize();
    }

    public void sortInventoryAlpha() {
        inventory.getContainedItems().sort(Comparator.comparing(Item::getItem_name));
    }

    public void sortInventoryWeight() {
        inventory.getContainedItems().sort(Comparator.comparingDouble(Item::getItem_weight).reversed());
    }

    public void sortInventoryValue() {
        inventory.getContainedItems().sort(Comparator.comparingDouble(Item::getItem_value).reversed());
    }

    public Item searchInventory(String searchedName) {
        int index;
        index = inventory.findContainedItemByName(searchedName);
        Item item;
        item = inventory.findContainedItemByIndex(index);

        return item;
    }

    public boolean inventoryFull() {
        if (inventory.getSize() == inventory.getContainedInventoryMaxCapacity()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean inventoryEmpty() {
        if (inventory.getSize() == 0) {
            return true;
        }
        return false;
    }

    public int upgradeInventory() {
        if (inventory.getContainedInventoryMaxCapacity() <= (absoluteMaxCapacity - upgradeValue)) {
            this.setCurrentMaxCapacity(this.getCurrentMaxCapacity() + upgradeValue);
            this.inventory.setContainedInventoryMaxCapacity(this.getCurrentMaxCapacity());
            this.setWeightLimit(getWeightLimit() + 25);
            return 1;
        } else {
            return 0;
        }
    }

    public void refreshInventory() {
        double updatedWeight = 0;
        for (Item item : inventory.getContainedItems()) {
            updatedWeight += item.getItem_weight();
        }
        setCurrentWeight(updatedWeight);
        setRemainingWeightCapacity(getWeightLimit() - getCurrentWeight());
    }
/*
    public void saveInventory() {
        inventory.createSavedInventory();
    }
*/
    public ArrayList<String> shownNames() {
        Item item = null;
        ArrayList<String> shownNames = new ArrayList<>();

        inventory.getContainedItems();
        for (int i = 0; i < inventory.getContainedItems().size(); i++) {
            item = inventory.getContainedItems().get(i);
            if (item instanceof Consumable) {
                String buildString = ("Slot [" + (i + 1) + "] " + "'" + item.getItem_name() + "'" + ", amount: " + ((Consumable) item).getItemAmount() + ", value: " + item.getItem_value() + ", weight: " + item.getItem_weight());
                shownNames.add(buildString);
            } else if (item instanceof Resource) {
                String buildString = ("Slot [" + (i + 1) + "] " + "'" + item.getItem_name() + "'" + ", amount: " + ((Resource) item).getItemAmount() + ", value: " + item.getItem_value() + ", weight: " + item.getItem_weight());
                shownNames.add(buildString);
            } else {
                String buildString = ("Slot [" + (i + 1) + "] " + "'" + item.getItem_name() + "'" + ", value: " + item.getItem_value() + ", weight: " + item.getItem_weight());
                shownNames.add(buildString);
            }
        }
        return shownNames;
    }
/*
    public void readSaved() {
        //ArrayList<Item> savedList = databaseRepo.readSavedInventory();

    }

 */
}// InventoryManager End

