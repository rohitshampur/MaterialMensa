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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.adapters.AdapterToday;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.UrlEndpoints;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;

import static de.prttstft.materialmensa.extras.Keys.EndpointToday.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentToday#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentToday extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final String STATE_MEAL = "state_meal";

    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    public ArrayList<Meal> listMeals = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private AdapterToday adapterToday;
    private RecyclerView listToday;
    private TextView textVolleyError;
    private MealSorter mSorter = new MealSorter();


    public static FragmentToday newInstance(String param1, String param2) {

        FragmentToday fragment = new FragmentToday();
    /*    Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MEAL, listMeals);

    }

    // Required empty public constructor
    public FragmentToday() {
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    // This should be outsourced
    private String getAllergens(String allergen) {
        if (allergen.equals("A1")) {
            return getResources().getString(R.string.gluten);
        } else if (allergen.equals("A2")) {
            return getResources().getString(R.string.crab);
        } else if (allergen.equals("A3")) {
            return getResources().getString(R.string.egg);
        } else if (allergen.equals("A4")) {
            return getResources().getString(R.string.fish);
        } else if (allergen.equals("A5")) {
            return getResources().getString(R.string.peanuts);
        } else if (allergen.equals("A6")) {
            return getResources().getString(R.string.soy);
        } else if (allergen.equals("A7")) {
            return getResources().getString(R.string.milk);
        } else if (allergen.equals("A8")) {
            return getResources().getString(R.string.nuts);
        } else if (allergen.equals("A9")) {
            return getResources().getString(R.string.celery);
        } else if (allergen.equals("A10")) {
            return getResources().getString(R.string.mustard);
        } else if (allergen.equals("A11")) {
            return getResources().getString(R.string.sesame);
        } else if (allergen.equals("A12")) {
            return getResources().getString(R.string.sulphites);
        } else if (allergen.equals("A13")) {
            return getResources().getString(R.string.lupins);
        } else if (allergen.equals("A14")) {
            return getResources().getString(R.string.molluscs);
        } else {
            return "";
        }
    }

    private String getAdditives(String additive) {
        if (additive.equals("1")) {
            return getResources().getString(R.string.dyes);
        } else if (additive.equals("2")) {
            return getResources().getString(R.string.preservatives);
        } else if (additive.equals("3")) {
            return getResources().getString(R.string.antioxidant);
        } else if (additive.equals("4")) {
            return getResources().getString(R.string.flavorenhancers);
        } else if (additive.equals("5")) {
            return getResources().getString(R.string.phosphate);
        } else if (additive.equals("6")) {
            return getResources().getString(R.string.sulphurised);
        } else if (additive.equals("7")) {
            return getResources().getString(R.string.waxed);
        } else if (additive.equals("8")) {
            return getResources().getString(R.string.blackend);
        } else if (additive.equals("9")) {
            return getResources().getString(R.string.sweeteners);
        } else if (additive.equals("10")) {
            return getResources().getString(R.string.phenylalanine);
        } else if (additive.equals("11")) {
            return getResources().getString(R.string.taurine);
        } else if (additive.equals("12")) {
            return getResources().getString(R.string.nitratesaltingmix);
        } else if (additive.equals("13")) {
            return getResources().getString(R.string.caffeine);
        } else if (additive.equals("14")) {
            return getResources().getString(R.string.quinine);
        } else if (additive.equals("15")) {
            return getResources().getString(R.string.milkprotein);
        } else {
            return "";
        }
    }
    /////////////////////////////////////////////

    // This should be outsourced
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        sendJsonRequest();
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
                        if (category.equals("dish-default")) {
                            order_info = 1;
                        } else if (category.equals("dish-pasta")) {
                            order_info = 2;
                        } else if (category.equals("dish-wok")) {
                            order_info = 3;
                        } else if (category.equals("dish-grill")) {
                            order_info = 4;
                        } else if (category.equals("sidedish")) {
                            order_info = 5;
                        } else if (category.equals("soups")) {
                            order_info = 6;
                        } else {
                            order_info = 7;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        listToday = (RecyclerView) view.findViewById(R.id.listToday);
        listToday.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterToday = new AdapterToday(getActivity());
        listToday.setAdapter(adapterToday);
        if (savedInstanceState != null) {
            listMeals = savedInstanceState.getParcelableArrayList(STATE_MEAL);
            adapterToday.setMealList(filterMealList(listMeals));
        } else {
            sendJsonRequest();
        }
        return view;
    }

    public ArrayList<Meal> filterMealList(ArrayList<Meal> unfilteredMealList) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String lifeStyle = SP.getString("prefLifestyle", "1");
        Set<String> selectionsAllergens = SP.getStringSet("prefAllergens", Collections.<String>emptySet());
        Set<String> selectionsAdditives = SP.getStringSet("prefAdditives", Collections.<String>emptySet());
        String[] selectedAllergens = selectionsAllergens.toArray(new String[selectionsAllergens.size()]);
        String[] selectedAdditives = selectionsAdditives.toArray(new String[selectionsAdditives.size()]);
        ArrayList<Meal> filteredMealList = new ArrayList<>();

        for (int i = 0; i < unfilteredMealList.size(); i++) {
            String getAllergensAdditives = unfilteredMealList.get(i).getAllergens().toString();
            String getBadge = unfilteredMealList.get(i).getBadge();
            Boolean isCleared = true;

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

        for (int i = 0; i < items.length; i++) {
            if (inputString.contains(items[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean doesContainsFilteredAdditives(String inputString, String[] items) {

        for (int i = 0; i < items.length; i++) {

            String patternA = "(A{1}\\d{1,2})";
            String inputStringRegExd = inputString.replaceAll(patternA, "");

            Pattern patternZ = Pattern.compile(items[i] + "{1}\\D");
            Matcher matcher = patternZ.matcher(inputStringRegExd);

            if (matcher.find()) {
                return true;
            }

        }
        return false;
    }

    public boolean isVegetarian(String badge) {
        if (!(badge.equals("vegetarian") | badge.equals("vegan"))) {
            return false;
        }
        return true;
    }

    public boolean isVegan(String badge) {
        if (!badge.equals("vegan")) {
            return false;
        }
        return true;
    }

    public String formatCurrency(String prize) {
        double prizeDouble = Double.valueOf(prize);

        Locale german = new Locale("de", "DE");
        NumberFormat format = NumberFormat.getCurrencyInstance(german);

        return format.format(prizeDouble);
    }
}