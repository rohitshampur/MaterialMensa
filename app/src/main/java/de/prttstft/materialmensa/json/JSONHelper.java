package de.prttstft.materialmensa.json;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.MealSorter;
import de.prttstft.materialmensa.extras.URLBuilder;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.pojo.Meal;

import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_ALLERGENS;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_BADGE;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_CATEGORY;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_GUESTS;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_NAME_DE;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_NAME_EN;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_RESTAURANT;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_STAFF;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_STUDENTS;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_TARA;

public class JSONHelper {

    public JSONArray sendJsonRequest(RequestQueue requestQueue) {
        JSONArray response = null;
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                URLBuilder.getRequestUrl(),
                (String) null, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            L.m(e + "");
        }

        return response;
    }

    public ArrayList<Meal> parseJSONResponse(JSONArray response, Context context) {
        MealSorter mSorter = new MealSorter();
        ArrayList<Meal> listMeals = new ArrayList<>();
        if (response != null || response.length() > 0) {

            try {
                StringBuilder data = new StringBuilder();

                for (int i = 0; i < response.length(); i++) {
                    String name = Constants.NA;
                    String category = Constants.NA;
                    boolean tara = false;
                    String price_students = Constants.NA;
                    String price_staff = Constants.NA;
                    String price_guests = Constants.NA;
                    List<String> allergens = new ArrayList<String>();
                    List<String> allergens_spelledout = new ArrayList<String>();
                    String badge = "";
                    int order_info = 0;
                    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
                    String ls = SP.getString("prefLifestyle", "1");
                    boolean starred = false;
                    int restaurant = 0;

                    JSONObject currentMeal = response.getJSONObject(i);

                    if (currentMeal.has(KEY_NAME_DE) && !currentMeal.isNull(KEY_NAME_DE)) {
                        if (Locale.getDefault().getISO3Language().equals("deu")) {
                            name = currentMeal.getString(KEY_NAME_DE);
                        } else {
                            name = currentMeal.getString(KEY_NAME_EN);
                        }
                    }

                    if (currentMeal.has(KEY_CATEGORY) && !currentMeal.isNull(KEY_CATEGORY)) {
                        category = currentMeal.getString(KEY_CATEGORY);
                        switch (category) {
                            case "dish-default":
                                order_info = 1;
                                break;
                            case "quicklunch":
                                order_info = 1;
                                break;
                            case "dish":
                                order_info = 1;
                                break;
                            case "classic":
                                order_info = 1;
                                break;
                            case "dish-pasta":
                                order_info = 2;
                                break;
                            case "happydinner":
                                order_info = 2;
                                break;
                            case "appetizer":
                                order_info = 2;
                                break;
                            case "dish-wok":
                                order_info = 3;
                                break;
                            case "classics-evening":
                                order_info = 3;
                                break;
                            case "dish-grill":
                                order_info = 4;
                                break;
                            case "snacks":
                                order_info = 4;
                                break;
                            case "sidedish":
                                order_info = 5;
                                break;
                            case "soups":
                                order_info = 6;
                                break;
                            case "maincourses":
                                order_info = 7;
                                break;
                            case "salads":
                                order_info = 8;
                                break;
                            case "dessert":
                                order_info = 9;
                                break;
                            default:
                                order_info = 99;
                                break;
                        }
                    }

                    if (currentMeal.has(KEY_STUDENTS) && !currentMeal.isNull(KEY_STUDENTS)) {
                        price_students = formatCurrency(currentMeal.getString(KEY_STUDENTS));
                    }

                    if (currentMeal.has(KEY_STAFF) && !currentMeal.isNull(KEY_STAFF)) {
                        price_staff = formatCurrency(currentMeal.getString(KEY_STAFF));
                    }

                    if (currentMeal.has(KEY_GUESTS) && !currentMeal.isNull(KEY_GUESTS)) {
                        price_guests = formatCurrency(currentMeal.getString(KEY_GUESTS));
                    }

                    if (currentMeal.has(KEY_ALLERGENS) && !currentMeal.isNull(KEY_ALLERGENS)) {
                        JSONArray arrayAllergens = currentMeal.getJSONArray(KEY_ALLERGENS);

                        for (int j = 0; j < arrayAllergens.length(); j++) {
                            Pattern pattern = Pattern.compile("(^)(\\d+)");
                            Matcher matcher = pattern.matcher(arrayAllergens.getString(j));
                            if (matcher.find()) {
                                allergens.add(arrayAllergens.getString(j));
                                allergens_spelledout.add(getAdditives(arrayAllergens.getString(j), context));

                            } else {
                                allergens.add(arrayAllergens.getString(j));
                                allergens_spelledout.add(getAllergens(arrayAllergens.getString(j), context));
                            }
                        }
                    }

                    if (currentMeal.has(KEY_BADGE) && !currentMeal.isNull(KEY_BADGE)) {
                        JSONArray arrayBadge = currentMeal.getJSONArray(KEY_BADGE);
                        if (arrayBadge.length() != 0) {
                            badge = arrayBadge.getString(0);
                        }
                    }

                    if (currentMeal.has(KEY_TARA) && !currentMeal.isNull(KEY_TARA)) {
                        if (currentMeal.getString(KEY_TARA).equals("weighted")) {
                            tara = true;
                        }
                    }

                    if (currentMeal.has(KEY_RESTAURANT) && !currentMeal.isNull(KEY_RESTAURANT)) {
                        restaurant = convertRestaurantNameToInt(currentMeal.getString(KEY_RESTAURANT));
                    }

                    Meal meal = new Meal();
                    meal.setName(name);
                    meal.setCategory(category);
                    meal.setPriceStudents(price_students);
                    meal.setPriceStaff(price_staff);
                    meal.setPriceGuests(price_guests);
                    meal.setAllergens(allergens);
                    meal.setAllergensSpelledOut(allergens_spelledout);
                    meal.setBadge(badge);
                    meal.setOrderInfo(order_info);
                    meal.setTara(tara);
                    meal.setStarred(starred);
                    meal.setRestaurant(restaurant);

                    if (!name.equals(Constants.NA)) {
                        listMeals.add(meal);
                    }
                }

            } catch (JSONException e) {
                L.m(e + "");
            }
        }
        mSorter.sortMealsByOrderInfo(listMeals);
        return listMeals;
    }

    public ArrayList<Meal> setUpAndFilterMealList(ArrayList<Meal> unfilteredMealList, Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String personCategory = SP.getString("prefPersonCategory", "1");
        String lifeStyle = SP.getString("prefLifestyle", "1");
        Set<String> selectionsAllergens = SP.getStringSet("prefAllergens", Collections.<String>emptySet());
        Set<String> selectionsAdditives = SP.getStringSet("prefAdditives", Collections.<String>emptySet());
        String[] selectedAllergens = selectionsAllergens.toArray(new String[selectionsAllergens.size()]);
        String[] selectedAdditives = selectionsAdditives.toArray(new String[selectionsAdditives.size()]);
        ArrayList<Meal> filteredMealList = new ArrayList<>();

        for (Meal nextMeal : unfilteredMealList) {
            String getBadge = nextMeal.getBadge();
            String getPrices = nextMeal.getPrices();
            String getPricesDe = nextMeal.getPricesDe();
            String getStudentPrice = nextMeal.getPriceStudents();
            String getStaffPrice = nextMeal.getPriceStaff();
            String getGuestPrice = nextMeal.getPriceGuests();
            Boolean isCleared = true;

            switch (getBadge) {
                case "vegetarian":
                    nextMeal.setBadgeIcon(R.drawable.ic_vegeterian);
                    break;
                case "vegan":
                    nextMeal.setBadgeIcon(R.drawable.ic_vegan);
                    break;
                case "lactose-free":
                    nextMeal.setBadgeIcon(R.drawable.ic_lactose_free);
                    break;
                case "low-calorie":
                    nextMeal.setBadgeIcon(R.drawable.ic_low_calorie);
                    break;
                case "vital-food":
                    nextMeal.setBadgeIcon(R.drawable.ic_vital_food);
                    break;
                case "nonfat":
                    nextMeal.setBadgeIcon(R.drawable.ic_nonfat);
                    break;
                default:
                    nextMeal.setBadgeIcon(R.drawable.ic_transparent);
                    break;
            }

            if (personCategory != null) {
                switch (personCategory) {
                    case "2":
                        nextMeal.setPriceOutput(getStudentPrice);
                        break;
                    case "3":
                        nextMeal.setPriceOutput(getStaffPrice);
                        break;
                    case "4":
                        nextMeal.setPriceOutput(getGuestPrice);
                        break;
                    default:
                        if (Locale.getDefault().getISO3Language().equals("deu")) {
                            nextMeal.setPriceOutput(getPricesDe);
                        } else {
                            nextMeal.setPriceOutput(getPrices);
                        }
                        break;
                }
            }

            if (ActivityMain.mensaID != nextMeal.getRestaurant()) isCleared = false;

            if (nextMeal.containsAllergens(selectedAllergens)) isCleared = false;

            if (nextMeal.containsAdditives(selectedAdditives)) isCleared = false;

            if (lifeStyle.equals("2") & !nextMeal.isVegetarian()) isCleared = false;

            if (lifeStyle.equals("3") & !nextMeal.isVegan()) isCleared = false;

            if (isCleared) filteredMealList.add(nextMeal);

        }
        return filteredMealList;
    }

    private String formatCurrency(String prize) {
        double prizeDouble = Double.valueOf(prize);

        Locale german = new Locale("de", "DE");
        NumberFormat format = NumberFormat.getCurrencyInstance(german);

        return format.format(prizeDouble);
    }

    private String getAllergens(String allergen, Context context) {
        switch (allergen) {
            case "A1":
                return context.getResources().getString(R.string.gluten);
            case "A2":
                return context.getResources().getString(R.string.crab);
            case "A3":
                return context.getResources().getString(R.string.egg);
            case "A4":
                return context.getResources().getString(R.string.fish);
            case "A5":
                return context.getResources().getString(R.string.peanuts);
            case "A6":
                return context.getResources().getString(R.string.soy);
            case "A7":
                return context.getResources().getString(R.string.milk);
            case "A8":
                return context.getResources().getString(R.string.nuts);
            case "A9":
                return context.getResources().getString(R.string.celery);
            case "A10":
                return context.getResources().getString(R.string.mustard);
            case "A11":
                return context.getResources().getString(R.string.sesame);
            case "A12":
                return context.getResources().getString(R.string.sulphites);
            case "A13":
                return context.getResources().getString(R.string.lupins);
            case "A14":
                return context.getResources().getString(R.string.molluscs);
            default:
                return "";
        }
    }

    private String getAdditives(String additive, Context context) {
        switch (additive) {
            case "1":
                return context.getResources().getString(R.string.dyes);
            case "2":
                return context.getResources().getString(R.string.preservatives);
            case "3":
                return context.getResources().getString(R.string.antioxidant);
            case "4":
                return context.getResources().getString(R.string.flavorenhancers);
            case "5":
                return context.getResources().getString(R.string.phosphate);
            case "6":
                return context.getResources().getString(R.string.sulphurised);
            case "7":
                return context.getResources().getString(R.string.waxed);
            case "8":
                return context.getResources().getString(R.string.blackend);
            case "9":
                return context.getResources().getString(R.string.sweeteners);
            case "10":
                return context.getResources().getString(R.string.phenylalanine);
            case "11":
                return context.getResources().getString(R.string.taurine);
            case "12":
                return context.getResources().getString(R.string.nitratesaltingmix);
            case "13":
                return context.getResources().getString(R.string.caffeine);
            case "14":
                return context.getResources().getString(R.string.quinine);
            case "15":
                return context.getResources().getString(R.string.milkprotein);
            default:
                return "";
        }
    }

    private int convertRestaurantNameToInt(String restaurant) {
        switch (restaurant) {
            case "mensa-academica-paderborn":
                return 0;
            case "mensa-forum-paderborn":
                return 1;
            case "cafete":
                return 2;
            case "grill-cafe":
                return 3;
            case "campus-doener":
                return 4;
            case "one-way-snack":
                return 5;
            case "mensula":
                return 6;
            case "mensa-hamm":
                return 7;
            case "mensa-lippstadt":
                return 8;
            case "bistro-hotspot1":
                return 9;
            default:
                return 10;
        }
    }
}
