package com.example.locc_development.Controller;

import com.example.locc_development.Model.Inventory;
import com.example.locc_development.Model.Item;

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

    public void setCurrentMaxCapacity(int WantedMaxCapacity) {
        if (currentMaxCapacity != WantedMaxCapacity) {
            if (inventory.setInventoryMaxCapacity(WantedMaxCapacity) == 1) {
                this.currentMaxCapacity = WantedMaxCapacity;
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
            this.goldAmount += item.getItem_value();
            refreshInventory();
            return 1;
        } else {
            return 0;
        }
    }

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
        inventory.getContainedItems().sort(Comparator.comparingDouble(Item::getItem_weight));
    }

    public void sortInventoryValue() {
        inventory.getContainedItems().sort(Comparator.comparingDouble(Item::getItem_value));
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
        if (inventory.getContainedInventoryMaxCapacity() <= absoluteMaxCapacity - upgradeValue) {
            inventory.setInventoryMaxCapacity(inventory.getContainedInventoryMaxCapacity() + upgradeValue);
            setCurrentMaxCapacity(inventory.getContainedInventoryMaxCapacity());
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

    public void saveInventory() {

        inventory.createSavedInventory();
    }

}// InventoryManager End

