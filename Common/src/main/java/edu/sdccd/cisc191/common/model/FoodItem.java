package edu.sdccd.cisc191.common.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * FoodItem is the model class of the SpringBoot Application.
 * It defines all the attributes of a food and implements methods in compliance with the comparable interface
 * long id: the id associated with each food
 * String name: the name of the food
 * String foodType: a food's categorical type
 * float quantityLeft: the amount of food that is left
 * boolean opened: has the food been open or eaten yet?
 */

@Entity
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public class FoodItem implements Comparable<FoodItem> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String foodType;
    private float quantityLeft;

    @Temporal(TemporalType.DATE)
    private LocalDate expirationDate;

    //add opened property because drink no longer exists
    private boolean opened;

    public FoodItem() {}

    public FoodItem(String name, String foodType, float quantityLeft, LocalDate expirationDate, boolean opened) {
        this.name = name;
        this.foodType = foodType;
        this.quantityLeft = quantityLeft;
        this.expirationDate = expirationDate;
        this.opened = opened;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public float getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(float quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public synchronized LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    //methods that must have an override when implementing the Comparable interface
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FoodItem that = (FoodItem) obj;
        return name.equalsIgnoreCase(that.name);  // or another attribute
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(FoodItem other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}