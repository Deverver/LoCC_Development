package com.example.locc_development.Controller;

public class SavedInventory {
    private int Fk;
    private int amount;

    public SavedInventory(int fk, int amount) {
        this.Fk = fk;
        this.amount = amount;
    }
    public SavedInventory(int fk) {
        this.Fk = fk;
    }
    public int getFk() {
        return Fk;
    }
    public void setFk(int fk) {
        Fk = fk;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
