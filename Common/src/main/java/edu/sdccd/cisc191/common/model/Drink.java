package edu.sdccd.cisc191.common.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

import java.util.Date;

@Entity
@DiscriminatorValue("Drink")
public class Drink extends FoodItem {

    @Column(name = "is_opened")
    private Boolean opened;  // Use Boolean for compatibility with FoodItem and nullability

    public Drink() {
        super();
    }

    public Drink(String name, String foodType, float quantityLeft, Date expirationDate, Boolean opened) {
        super(name, foodType, quantityLeft, expirationDate);
        this.opened = opened;
    }

    @Override
    public Boolean getOpened() {
        return opened;
    }





    public Boolean isOpened() {
        return getOpened();
    }


    public void setOpened(Boolean opened) {
        this.opened = opened;
    }
}
