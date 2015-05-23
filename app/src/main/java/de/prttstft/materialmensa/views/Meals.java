package de.prttstft.materialmensa.views;

public class Meals {

    private String meal_name;
    private String meal_price;
    private String meal_contents;
    //private String meal_typeicon;

    public Meals(String meal_name, String meal_price, String meal_contents){
        // , String meal_typeicon
        this.meal_name = meal_name;
        this.meal_price = meal_price;
        this.meal_contents = meal_contents;
      //  this.meal_typeicon = meal_typeicon;
    }

    public String getMeal_name() {
        return meal_name;
    }

    public String getMeal_price() {
        return meal_price;
    }

    public String getMeal_contents() {
        return meal_contents;
    }

    /*public String getMeal_typeicon() {
        return meal_contents;
    }*/


}