package de.prttstft.materialmensa.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.adapters.AdapterToday;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.MealSorter;
import de.prttstft.materialmensa.extras.UrlEndpoints;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;

import static de.prttstft.materialmensa.extras.Keys.EndpointToday.*;

public class FragmentToday extends Fragment {
    private RequestQueue requestQueue;
    public ArrayList<Meal> listMeals = new ArrayList<>();
    private AdapterToday adapterToday;
    private TextView textVolleyError;
    private MealSorter mSorter = new MealSorter();

    public static FragmentToday newInstance() {
        return new FragmentToday();
    }

    public FragmentToday() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        sendJsonRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        RecyclerView listToday = (RecyclerView) view.findViewById(R.id.listToday);
        listToday.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterToday = new AdapterToday(getActivity());
        listToday.setAdapter(adapterToday);
        sendJsonRequest();
        return view;
    }

    ///////////////////////////////////////////////////////////////////////
    //                     This should be outsourced                     //

    private String getAllergens(String allergen) {
        switch (allergen) {
            case "A1":
                return getResources().getString(R.string.gluten);
            case "A2":
                return getResources().getString(R.string.crab);
            case "A3":
                return getResources().getString(R.string.egg);
            case "A4":
                return getResources().getString(R.string.fish);
            case "A5":
                return getResources().getString(R.string.peanuts);
            case "A6":
                return getResources().getString(R.string.soy);
            case "A7":
                return getResources().getString(R.string.milk);
            case "A8":
                return getResources().getString(R.string.nuts);
            case "A9":
                return getResources().getString(R.string.celery);
            case "A10":
                return getResources().getString(R.string.mustard);
            case "A11":
                return getResources().getString(R.string.sesame);
            case "A12":
                return getResources().getString(R.string.sulphites);
            case "A13":
                return getResources().getString(R.string.lupins);
            case "A14":
                return getResources().getString(R.string.molluscs);
            default:
                return "";
        }
    }

    private String getAdditives(String additive) {
        switch (additive) {
            case "1":
                return getResources().getString(R.string.dyes);
            case "2":
                return getResources().getString(R.string.preservatives);
            case "3":
                return getResources().getString(R.string.antioxidant);
            case "4":
                return getResources().getString(R.string.flavorenhancers);
            case "5":
                return getResources().getString(R.string.phosphate);
            case "6":
                return getResources().getString(R.string.sulphurised);
            case "7":
                return getResources().getString(R.string.waxed);
            case "8":
                return getResources().getString(R.string.blackend);
            case "9":
                return getResources().getString(R.string.sweeteners);
            case "10":
                return getResources().getString(R.string.phenylalanine);
            case "11":
                return getResources().getString(R.string.taurine);
            case "12":
                return getResources().getString(R.string.nitratesaltingmix);
            case "13":
                return getResources().getString(R.string.caffeine);
            case "14":
                return getResources().getString(R.string.quinine);
            case "15":
                return getResources().getString(R.string.milkprotein);
            default:
                return "";
        }
    }

    public static String getRequestUrl() {
        if (ActivityMain.mensaID == 1) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_FORUM
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;

        } else if (ActivityMain.mensaID == 2) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_CAFETE
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 3) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_GRILLCAFE
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 4) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_CAMPUSDOENER
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 5) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_ONEWAYSNACK
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else if (ActivityMain.mensaID == 6) {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_MENSULA
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        } else {
            return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_RESTAURANT
                    + UrlEndpoints.URL_RESTAURANT_ACADEMICA
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_PARAM_DATE
                    + UrlEndpoints.URL_PARAM_TODAY
                    ;
        }
    }

    private void sendJsonRequest() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getRequestUrl(),
                (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        textVolleyError.setVisibility(View.GONE);
                        listMeals = parseJSONResponse(response);
                        adapterToday.setMealList(filterMealList(listMeals));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });
        requestQueue.add(request);
    }

    private ArrayList<Meal> parseJSONResponse(JSONArray response) {

        ArrayList<Meal> listMeals = new ArrayList<>();
        if (response != null || response.length() > 0) {

            try {
                StringBuilder data = new StringBuilder();

                for (int i = 0; i < response.length(); i++) {
                    String name = Constants.NA;
                    String category = Constants.NA;
                    Boolean tara = false;
                    String price_students = Constants.NA;
                    String price_staff = Constants.NA;
                    String price_guests = Constants.NA;
                    List<String> allergens = new ArrayList<String>();
                    List<String> allergens_spelledout = new ArrayList<String>();
                    String badge = "";
                    int order_info = 0;
                    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String ls = SP.getString("prefLifestyle", "1");

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
                            case "dish-pasta":
                                order_info = 2;
                                break;
                            case "dish-wok":
                                order_info = 3;
                                break;
                            case "dish-grill":
                                order_info = 4;
                                break;
                            case "sidedish":
                                order_info = 5;
                                break;
                            case "soups":
                                order_info = 6;
                                break;
                            default:
                                order_info = 7;
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
                                allergens_spelledout.add(getAdditives(arrayAllergens.getString(j)));

                            } else {
                                allergens.add(arrayAllergens.getString(j));
                                allergens_spelledout.add(getAllergens(arrayAllergens.getString(j)));
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

                    if (!name.equals(Constants.NA)) {
                        listMeals.add(meal);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mSorter.sortMealsByOrderInfo(listMeals);
        return listMeals;
    }

    private void handleVolleyError(VolleyError error) {
        textVolleyError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);
        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth_failure);

        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_server);

        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.error_network);

        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_parse);
        }

    }

    public ArrayList<Meal> filterMealList(ArrayList<Meal> unfilteredMealList) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String personCategory = SP.getString("prefPersonCategory", "1");
        String lifeStyle = SP.getString("prefLifestyle", "1");
        Set<String> selectionsAllergens = SP.getStringSet("prefAllergens", Collections.<String>emptySet());
        Set<String> selectionsAdditives = SP.getStringSet("prefAdditives", Collections.<String>emptySet());
        String[] selectedAllergens = selectionsAllergens.toArray(new String[selectionsAllergens.size()]);
        String[] selectedAdditives = selectionsAdditives.toArray(new String[selectionsAdditives.size()]);
        ArrayList<Meal> filteredMealList = new ArrayList<>();

        for (int i = 0; i < unfilteredMealList.size(); i++) {
            String getAllergensAdditives = unfilteredMealList.get(i).getAllergens().toString();
            String getBadge = unfilteredMealList.get(i).getBadge();
            String getPrices = unfilteredMealList.get(i).getPrices();
            String getPricesDe = unfilteredMealList.get(i).getPricesDe();
            String getStudentPrice = unfilteredMealList.get(i).getPriceStudents();
            String getStaffPrice = unfilteredMealList.get(i).getPriceStaff();
            String getGuestPrice = unfilteredMealList.get(i).getPriceGuests();
            Boolean isCleared = true;

            switch (getBadge) {
                case "vegetarian":
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_vegeterian);
                    break;
                case "vegan":
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_vegan);
                    break;
                case "lactose-free":
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_lactose_free);
                    break;
                case "low-calorie":
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_low_calorie);
                    break;
                case "vital-food":
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_vital_food);
                    break;
                case "nonfat":
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_nonfat);
                    break;
                default:
                    unfilteredMealList.get(i).setBadgeIcon(R.drawable.ic_transparent);
                    break;
            }

            if (personCategory != null) {
                switch (personCategory) {
                    case "2":
                        unfilteredMealList.get(i).setPriceOutput(getStudentPrice);
                        break;
                    case "3":
                        unfilteredMealList.get(i).setPriceOutput(getStaffPrice);
                        break;
                    case "4":
                        unfilteredMealList.get(i).setPriceOutput(getGuestPrice);
                        break;
                    default:
                        if (Locale.getDefault().getISO3Language().equals("deu")) {
                            unfilteredMealList.get(i).setPriceOutput(getPricesDe);
                        } else {
                            unfilteredMealList.get(i).setPriceOutput(getPrices);
                        }
                        break;
                }
            }


            if (doesContainsFilteredAllergens(getAllergensAdditives, selectedAllergens)) {
                isCleared = false;
            }

            if (doesContainsFilteredAdditives(getAllergensAdditives, selectedAdditives)) {
                isCleared = false;
            }

            if (lifeStyle.equals("2") & !isVegetarian(getBadge)) {
                isCleared = false;
            } else if (lifeStyle.equals("3") & !isVegan(getBadge)) {
                isCleared = false;
            }

            if (isCleared) {
                filteredMealList.add(unfilteredMealList.get(i));
            }
        }
        return filteredMealList;
    }

    public boolean doesContainsFilteredAllergens(String inputString, String[] items) {

        for (String item : items) {
            if (inputString.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean doesContainsFilteredAdditives(String inputString, String[] items) {

        for (String item : items) {

            String patternA = "(A{1}\\d{1,2})";
            String inputStringRegExd = inputString.replaceAll(patternA, "");

            Pattern patternZ = Pattern.compile(item + "{1}\\D");
            Matcher matcher = patternZ.matcher(inputStringRegExd);

            if (matcher.find()) {
                return true;
            }

        }
        return false;
    }

    public boolean isVegetarian(String badge) {
        return badge.equals("vegetarian") | badge.equals("vegan");
    }

    public boolean isVegan(String badge) {
        return badge.equals("vegan");
    }

    public String formatCurrency(String prize) {
        double prizeDouble = Double.valueOf(prize);

        Locale german = new Locale("de", "DE");
        NumberFormat format = NumberFormat.getCurrencyInstance(german);

        return format.format(prizeDouble);
    }

    //                     This should be outsourced                     //
    ///////////////////////////////////////////////////////////////////////

}