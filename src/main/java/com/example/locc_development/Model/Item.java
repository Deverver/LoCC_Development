package com.example.locc_development.Model;


import com.example.locc_development.Controller.ItemAction;

/*
The Item class needs to contain a lot of information in order to be properly sorted and used.
In games such as LoCC, items need to display that information to the player or user in various ways.
A great example be the game/genre "Diablo" where items usually are dropped and displayed in large quantities on the ground,
in order for the user to quickly identify and sort through these items, each individual item displays a ray of light depending on its rarity.

Items also usually needs to display their most important information at a glance, which normally is their usage.
As such some games have multiple or shortened descriptions on their items.
Since we don't know the future scope of LoCC we have chosen to use our String itemType as both a way to display and also sort our items.
Depending on the type of list/array we chose items can be sorted in different ways...???
 */
public abstract class Item implements ItemAction {

    private int item_id;
    private String item_type;
    private String item_name;
    private String item_description;
    private double item_weight;
    private double item_value;



    public Item() {}// Empty constructor
    public Item(int item_id, String item_type, String item_name, String item_description, double item_weight, double item_value) {
        this.item_id = item_id;
        this.item_type = item_type;
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_weight = item_weight;
        this.item_value = item_value;
    }
    public Item(int item_id, String item_type, String item_name, String item_description, double item_weight, double item_value, int item_amount) {
        this.item_id = item_id;
        this.item_type = item_type;
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_weight = item_weight;
        this.item_value = item_value;
    }// Full Constructor

    //region Getters & Setters
    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public double getItem_weight() {
        return item_weight;
    }

    public void setItem_weight(double item_weight) {
        this.item_weight = item_weight;
    }

    public double getItem_value() {
        return item_value;
    }

    public void setItem_value(double item_value) {
        this.item_value = item_value;
    }
    //endregion


    public String showItemInfo() {
        String itemMessage;
        itemMessage = ("________________________\n" + this.getItem_name() + "\n________________________\n" + this.getItem_type() + "\n" + this.getItem_description() + ".");
        return itemMessage;
    }


    public void useItem(){
        System.out.println("________________________");
        System.out.println("You used the " + this.getItem_type() + ", " + this.getItem_name() + ".");
    }


}// End

