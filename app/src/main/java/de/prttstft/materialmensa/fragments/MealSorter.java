package de.prttstft.materialmensa.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.prttstft.materialmensa.pojo.Meal;

public class MealSorter {

    public void sortMealsByOrderInfo(ArrayList<Meal> meals) {
        Collections.sort(meals, new Comparator<Meal>() {
            @Override
            public int compare(Meal one, Meal two) {
                return Integer.toString(one.getOrderInfo()).compareTo(Integer.toString(two.getOrderInfo()));
            }
        });
    }
}