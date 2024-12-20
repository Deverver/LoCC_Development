package com.example.locc_development.Model;


import com.example.locc_development.Controller.SavedInventory;
import java.util.ArrayList;
import java.util.List;

/*
In essence an inventory needs to nothing more than just a list of Item objects, it could be rewards from defeating a monster or boss,
it could be a shops inventory or a rewards screen from a quest - in essence it is the same.
*/

public class Inventory {
    private int containedInventoryMaxCapacity = 32;
    private List<Item> containedItems;

    // Constructor
    public Inventory() {
        this.containedItems = new ArrayList<>();
    }

    //We need these since we have to upgrade our inventory's capacity
    //region Commands for controlling the inventory size
    public int getContainedInventoryMaxCapacity() {
        return containedInventoryMaxCapacity;
    }

    public int setInventoryMaxCapacity(int inventoryMaxCapacity) {
        if (containedInventoryMaxCapacity != inventoryMaxCapacity) {
            containedInventoryMaxCapacity = inventoryMaxCapacity;
            return 1;
        }
        return 0;
    }
    //endregion
    public int addItem(Item item) {
        // Check if item already exists in the Inventory
        int existingItemIndex = findContainedItemByName(item.getItem_name());
        if (existingItemIndex != -1) { // Item already exists
            Item existingItem = containedItems.get(existingItemIndex);

            // Handle Consumable type
            if (item instanceof Consumable) {
                Consumable consumableItem = (Consumable) existingItem;
                consumableItem.setItemAmount(consumableItem.getItemAmount() + 1);
                return 1;
            }

            // Handle Resource type
            if (item instanceof Resource) {
                Resource resourceItem = (Resource) existingItem;
                resourceItem.setItemAmount(resourceItem.getItemAmount() + 1);
                return 1;
            }

            // If item is neither Consumable nor Resource, we won't add duplicates
            return 0;
        }

        // If the inventory has space, add the new item
        if (containedItems.size() < containedInventoryMaxCapacity) {
            containedItems.add(item);
            return 1;
        }

        // If the inventory has no space, reject the item
        return 0;
    }
    public int removeItem(Item item) {
        int existingItemIndex = findContainedItemByName(item.getItem_name());
        if (existingItemIndex != -1) {
            containedItems.remove(existingItemIndex);
            return 1;
        }else{
            return 0;
        }
    }

    public List<Item> getContainedItems() {
        return containedItems;


        /*
        This will be a separate method for when we another ui
        We create a copy of the Inventory to show to users, this keeps the Inventory inside the InventoryManager in control, this should prevent external changes.
        ArrayList<Item> listedInventory = new ArrayList<>(containedItems.size());
        for (Item item : containedItems) {
            listedInventory.add(item);
        }
        return listedInventory;
        */

    }

    public int findContainedItemByName(String searchedItemName) {
        for (Item item : containedItems) {
            if (item.getItem_name().equals(searchedItemName)) {
                return containedItems.indexOf(item);
            }
        }
        return -1;
    }

    public int getSize() {
        if (containedItems.size() <= containedInventoryMaxCapacity) {
            return containedItems.size();
        } else {
            return -1;
        }
    }
    public ArrayList<SavedInventory> createSavedInventory(){
        ArrayList<SavedInventory> savedInventory = new ArrayList<>();

        for (Item item : containedItems) {
            int itemCount = 1;
            if(item.getItem_type().equals("Consumable")){
                itemCount = ((Consumable)item).getItemAmount();
            } else if (item.getItem_type().equals("Resource")) {
                itemCount = ((Resource)item).getItemAmount();
            }
            SavedInventory itemToBeSaved = new SavedInventory(item.getItem_id(), itemCount);
            savedInventory.add(itemToBeSaved);
        }
        return savedInventory;
    }

}// Inventory Class End

