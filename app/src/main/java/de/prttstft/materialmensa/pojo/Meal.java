package de.prttstft.materialmensa.pojo;


import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.prttstft.materialmensa.R;

public class Meal implements Parcelable {
    private String name;
    private String category;
    private String price_students;
    private String price_staff;
    private String price_guests;
    private String priceOutput;
    List<String> allergens = new ArrayList<String>();
    List<String> allergens_spelledout = new ArrayList<String>();
    private String badge;
    private int order_info;
    private boolean tara;
    Integer badgeIcon;

    public Meal() {

    }

    public Meal(Parcel input) {
        name = input.readString();
        category = input.readString();
        price_students = input.readString();
        price_staff = input.readString();
        price_guests = input.readString();
        badge = input.readString();
        order_info = input.readInt();
        tara = input.readInt() == 1;
    }

    public Meal(String name,
                String category,
                String price_students,
                String price_staff,
                String price_guests,
                String badge,
                int order_info,
                boolean tara) {
        this.name = name;
        this.category = category;
        this.price_students = price_students;
        this.price_staff = price_staff;
        this.price_guests = price_guests;
        this.badge = badge;
        this.order_info = order_info;
        this.tara = tara;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getPrices() {
        if (tara) {
            return "Per 100gr: " + "Students: " + price_students + " | " + "Staff: " + price_staff + " | " + "Guests: " + price_guests;
        } else {
            return "Students: " + price_students + " | " + "Staff: " + price_staff + " | " + "Guests: " + price_guests;
        }

    }

    public String getPricesDe() {
        if (tara) {
            return "Pro 100gr: " + "Studierende: " + price_students + " | " + "Bedienstete: " + price_staff + " | " + "Gäste: " + price_guests;
        } else {
            return "Studierende: " + price_students + " | " + "Bedienstete: " + price_staff + " | " + "Gäste: " + price_guests;
        }

    }

    public String getPriceStudents() {
        if (tara) {
            return price_students + "/100gr";
        } else {
            return price_students;
        }
    }

    public void setPriceStudents(String price_students) {
        this.price_students = price_students;
    }

    public String getPriceStaff() {
        if (tara) {
            return price_staff + "/100gr";
        } else {
            return price_staff;
        }

    }

    public void setPriceStaff(String price_staff) {
        this.price_staff = price_staff;
    }

    public String getPriceGuests() {
        if (tara) {
            return price_guests + "/100gr";
        } else {
            return price_guests;
        }
    }

    public void setPriceGuests(String price_guests) {
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

    public List<String> getAllergensSpelledOut() {
        return allergens_spelledout;
    }

    public void setAllergensSpelledOut(List<String> allergens_spelledout) {
        this.allergens_spelledout = allergens_spelledout;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getOrderInfo() {
        return order_info;
    }

    public void setOrderInfo(int order_info) {
        this.order_info = order_info;
    }

    public boolean getTara() {
        return tara;
    }

    public void setTara(boolean tara) {
        this.tara = tara;
    }

    public Integer getBadgeIcon() {
        if (badgeIcon != null) {
            return badgeIcon;
        } else {
            return R.drawable.ic_transparent;
        }
    }

    public void setBadgeIcon(int badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    @Override
    public String toString() {
        return "\nName: " + name +
                "\nCategory " + category +
                "\nPrice Students " + price_students +
                "\nPrice Staff " + price_staff +
                "\nPrice Guests " + price_guests +
                "\nAllergensAdditives " + allergens +
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
        dest.writeString(category);
        dest.writeString(price_students);
        dest.writeString(price_staff);
        dest.writeString(price_guests);
        dest.writeString(badge);
        dest.writeInt(order_info);
        if (tara) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }

    }

    public boolean containsAllergens(String[] allergens) {
        List<String> myAllergens = getAllergens();
        if (!myAllergens.isEmpty()) {
            for (String myAllergen : myAllergens) {
                for (String searchAllergen : allergens) {
                    if(myAllergen.startsWith("A")) {
                        if (myAllergen.substring(1).equals(searchAllergen)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };
}