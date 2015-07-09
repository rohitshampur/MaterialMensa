package de.prttstft.materialmensa.events;


import java.util.ArrayList;

import de.prttstft.materialmensa.pojo.Meal;

public class MealsLoadedEvent {
    public final ArrayList<Meal> meals;

    public MealsLoadedEvent(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}