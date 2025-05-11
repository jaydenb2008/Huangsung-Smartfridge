package edu.sdccd.cisc191.common.model;

import jakarta.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public class FoodItem implements Comparable<FoodItem> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String foodType;
    private float quantityLeft;

    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    public FoodItem() {}

    public FoodItem(String name, String foodType, float quantityLeft, Date expirationDate) {
        this.name = name;
        this.foodType = foodType;
        this.quantityLeft = quantityLeft;
        this.expirationDate = expirationDate;
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

    public synchronized Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }



    /**
     * Converts the user's valid String expiration date input into a Date for constructing the FoodItem object
     * @param userInputDate = the String expiration date the user inputs in the UI
     * @return the user's date converted from a String to a Date
     */
    public static Date convertToDate(String userInputDate) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        try {
            date = formatter.parse(userInputDate);
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }

        return date;
    }

    /**
     * Checks if the food item is expired based on the current date and expirationDate attribute
     * @param expiration the expiration date of the food item
     * @return true if the current date is after the expiration date, false otherwise
     */
    public synchronized boolean isExpired(Date expiration) {
        return new Date().after(expiration);
    }

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