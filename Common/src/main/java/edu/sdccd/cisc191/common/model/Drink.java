package edu.sdccd.cisc191.common.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Date;

@Entity
@DiscriminatorValue("Drink")
public class Drink extends FoodItem {
    private boolean isOpened;

    public Drink() {
        super();
    }

    public Drink(String name, String foodType, float quantityLeft, LocalDate expirationDate, boolean isOpened) {
        super(name, foodType, quantityLeft, expirationDate);
        this.isOpened = isOpened;
    }

    public void setOpened(boolean opened) {
        this.isOpened = isOpened;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
