package de.prttstft.materialmensa.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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

    public Meal(String name,
                String category,
                String price_students,
                String price_staff,
                String price_guests,
                String priceOutput,
                List<String> allergens,
                List<String> allergens_spelledout,
                String badge,
                int order_info,
                boolean tara,
                Integer badgeIcon) {
        this.name = name;
        this.category = category;
        this.price_students = price_students;
        this.price_staff = price_staff;
        this.price_guests = price_guests;
        this.priceOutput = priceOutput;
        this.allergens = allergens;
        this.allergens_spelledout = allergens_spelledout;
        this.badge = badge;
        this.order_info = order_info;
        this.tara = tara;
        this.badgeIcon = badgeIcon;
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

    public boolean containsAllergens(String[] allergens) {
        List<String> myAllergens = getAllergens();
        if (!myAllergens.isEmpty()) {
            for (String myAllergen : myAllergens) {
                for (String searchAllergen : allergens) {
                    if (myAllergen.startsWith("A")) {
                        if (myAllergen.substring(1).equals(searchAllergen)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean containsAdditives(String[] additives) {
        List<String> myAllergens = getAllergens();
        if (!myAllergens.isEmpty()) {
            for (String myAdditive : myAllergens) {
                for (String searchAdditive : additives) {
                    if (!myAdditive.startsWith("A")) {
                        if (myAdditive.equals(searchAdditive)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isVegetarian() {
        return getBadge().equals("vegetarian") | getBadge().equals("vegan");
    }

    public boolean isVegan() {
        return getBadge().equals("vegan");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.price_students);
        dest.writeString(this.price_staff);
        dest.writeString(this.price_guests);
        dest.writeString(this.priceOutput);
        dest.writeStringList(this.allergens);
        dest.writeStringList(this.allergens_spelledout);
        dest.writeString(this.badge);
        dest.writeInt(this.order_info);
        dest.writeByte(tara ? (byte) 1 : (byte) 0);
        dest.writeValue(this.badgeIcon);
    }

    protected Meal(Parcel in) {
        this.name = in.readString();
        this.category = in.readString();
        this.price_students = in.readString();
        this.price_staff = in.readString();
        this.price_guests = in.readString();
        this.priceOutput = in.readString();
        this.allergens = in.createStringArrayList();
        this.allergens_spelledout = in.createStringArrayList();
        this.badge = in.readString();
        this.order_info = in.readInt();
        this.tara = in.readByte() != 0;
        this.badgeIcon = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        public Meal createFromParcel(Parcel source) {
            return new Meal(source);
        }

        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };
}