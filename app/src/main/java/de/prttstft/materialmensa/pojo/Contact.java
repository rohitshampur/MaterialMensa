package de.prttstft.materialmensa.pojo;

import java.util.ArrayList;
import java.util.List;


public class Contact {
    private int _id;
    private String name;
    private String category;
    private String price_students;
    private String price_staff;
    private String price_guests;
    private String priceOutput;
    private List<String> allergens = new ArrayList<String>();
    private List<String> allergens_full = new ArrayList<String>();
    private String badge;
    private int order_info;
    private boolean tara;
    private Integer badgeIcon;


    public Contact(int id,
                   String name,
                   String category,
                   String price_students,
                   String price_staff,
                   String price_guests,
                   String priceOutput,
                   List<String> allergens,
                   List<String> allergens_full,
                   String badge,
                   int order_info,
                   boolean tara,
                   Integer badgeIcon) {
        this._id = id;
        this.name = name;
        this.category = category;
        this.price_students = price_students;
        this.price_staff = price_staff;
        this.price_guests = price_guests;
        this.priceOutput = priceOutput;
        this.allergens = allergens;
        this.allergens_full = allergens_full;
        this.badge = badge;
        this.order_info = order_info;
        this.tara = tara;
        this.badgeIcon = badgeIcon;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice_students() {
        return price_students;
    }

    public void setPrice_students(String price_students) {
        this.price_students = price_students;
    }

    public String getPrice_staff() {
        return price_staff;
    }

    public void setPrice_staff(String price_staff) {
        this.price_staff = price_staff;
    }

    public String getPrice_guests() {
        return price_guests;
    }

    public void setPrice_guests(String price_guests) {
        this.price_guests = price_guests;
    }

    public String getPriceOutput() {
        return priceOutput;
    }

    public void setPriceOutput(String priceOutput) {
        this.priceOutput = priceOutput;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public List<String> getAllergens_full() {
        return allergens_full;
    }

    public void setAllergens_full(List<String> allergens_full) {
        this.allergens_full = allergens_full;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getOrder_info() {
        return order_info;
    }

    public void setOrder_info(int order_info) {
        this.order_info = order_info;
    }

    public boolean getTara() {
        return tara;
    }

    public void setTara(boolean tara) {
        this.tara = tara;
    }

    public Integer getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(Integer badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    public String toString() {
        return "Name: " + name +
                " Category: " + category +
                " Price Students: " + price_students +
                " Price Staff: " + price_staff +
                " Price Guests: " + price_guests +
                " PriceOutput: " + priceOutput +
                " Allergens: " + deleteBrackets(allergens.toString()) +
                " Allergens Full: " + deleteBrackets(allergens_full.toString()) +
                " Badge: " + badge +
                " Order Info: " + order_info +
                " Tara: " + tara +
                " BadgeIcon: " + String.valueOf(badgeIcon)
                ;
    }

    private String deleteBrackets(String input) {
        return input.replace("[", "").replace("]", "");
    }
}
