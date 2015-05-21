package prttstft.de.materialmensa;

/**
 * Created by Max on 21.05.2015.
 */
public class data_row_meals {

    int meal_type_id;
    String name;
    String price_student;
    String price_staff;
    String price_guest;
    String contents;

    public data_row_meals (int meal_type_id, String name, String price_student, String price_staff, String price_guest, String contents){
        this.meal_type_id = meal_type_id;
        this.name = name;
        this.price_student = price_student;
        this.price_staff = price_staff;
        this.price_guest = price_guest;
        this.contents = contents;
    }

    public int getMeal_type_id() {
        return meal_type_id;
    }

    public String getName() {
        return name;
    }

    public String getPrice_student() {
        return price_student;
    }

    public String getPrice_staff() {
        return price_staff;
    }

    public String getPrice_guest() {
        return price_guest;
    }

    public String getContents() {
        return contents;
    }

}