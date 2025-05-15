package edu.sdccd.cisc191.common.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "item_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Drink.class, name = "Drink")
})

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "food_type", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "food_type")
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

    public FoodItem(Long id, String name, String foodType, float quantityLeft, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.quantityLeft = quantityLeft;
        this.expirationDate = expirationDate;
    }

    // This constructor lets you use JavaFX/REST/JSON with LocalDate and Boolean
    public FoodItem(Long id, String name, String foodType, double quantityLeft, LocalDate expirationDate, Boolean opened) {
        this.id = id;
        this.name = name;
        this.foodType = foodType;
        this.quantityLeft = (float) quantityLeft;
        this.expirationDate = (expirationDate == null) ? null : java.sql.Date.valueOf(expirationDate);
        // 'opened' ignored; Drink class should handle it
    }

    public static Date convertToDate(String s) {
        return null;
    }


    // ----- Getters and Setters -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getOpened() {
        return null; // Or you can return false if you prefer
    }

}
