package de.prttstft.materialmensa.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meal implements Parcelable {
    private String name;
    private String price_students;
    private String price_staff;
    private String price_guests;
    List<String> allergens = new ArrayList<String>();
    private int badge;

    public Meal() {

    }

    public Meal(Parcel input) {
        name = input.readString();
        price_students = input.readString();
        price_staff = input.readString();
        price_guests = input.readString();
        badge = input.readInt();

    }

    public Meal(String name,
                String price_students,
                String price_staff,
                String price_guests,
                int badge) {
        this.name = name;
        this.price_students = price_students;
        this.price_staff = price_staff;
        this.price_guests = price_guests;
        this.badge = badge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrices() {
        return "Studierende: " + price_students + "€" + " // " + "Bedienstete: " + price_staff + "€" + " // " + "Gäste: " + price_guests + "€";
    }

    public String getPriceStudents() {
        return price_students + "€";
    }

    public void setPriceStudents(String price_students) {
        this.price_students = price_students;
    }

    public String getPriceStaff() {
        return price_staff + "€";
    }

    public void setPriceStaff(String price_staff) {
        this.price_staff = price_staff;
    }

    public String getPriceGuests() {
        return price_guests + "€";
    }

    public void setPriceGuests(String price_guests) {
        this.price_guests = price_guests;
    }

    public String getAllergens() {
        return allergens.toString();
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    @Override
    public String toString() {
        return "\nName: " + name +
                "\nPrice Students " + price_students +
                "\nPrice Staff " + price_staff +
                "\nPrice Guests " + price_guests +
                "\nAllergens " + allergens +
                "\nBadge " + badge +
                "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price_students);
        dest.writeString(price_staff);
        dest.writeString(price_guests);
        dest.writeInt(badge);
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        public Meal[] newArray (int size) {
            return new Meal[size];
        }
    };
}