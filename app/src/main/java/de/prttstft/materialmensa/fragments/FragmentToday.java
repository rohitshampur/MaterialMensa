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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    //public static final String URL_ROTTEN_TOMATOES_BOX_OFFICE = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
    private static final String STATE_MEAL = "state_meal";

    // TODO: Rename and change types of parameters
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

    public FragmentToday() {
        // Required empty public constructor
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
                        // Filter mealList
                        filterMealList();
                        adapterToday.setMealList(listMeals);
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
                        if (category.equals("dish-default") || category.equals("dish-wok") || category.equals("dish-grill") || category.equals("dish-pasta")) {
                            order_info = 1;
                        } else if (category.equals("sidedish")) {
                            order_info = 2;
                        } else if (category.equals("soups")) {
                            order_info = 3;
                        } else {
                            order_info = 4;
                        }

                    }

                    if (currentMeal.has(KEY_STUDENTS) && !currentMeal.isNull(KEY_STUDENTS)) {
                        price_students = currentMeal.getString(KEY_STUDENTS);
                    }

                    if (currentMeal.has(KEY_STAFF) && !currentMeal.isNull(KEY_STAFF)) {
                        price_staff = currentMeal.getString(KEY_STAFF);
                    }

                    if (currentMeal.has(KEY_GUESTS) && !currentMeal.isNull(KEY_GUESTS)) {
                        price_guests = currentMeal.getString(KEY_GUESTS);
                    }

                    if (currentMeal.has(KEY_ALLERGENS) && !currentMeal.isNull(KEY_ALLERGENS)) {
                        JSONArray arrayAllergens = currentMeal.getJSONArray(KEY_ALLERGENS);

                        for (int j = 0; j < arrayAllergens.length(); j++) {
                            allergens.add(arrayAllergens.getString(j));
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
            filterMealList();
            adapterToday.setMealList(listMeals);
        } else {
            sendJsonRequest();
        }


        return view;
    }

    public void filterMealList() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String lifeStyle = SP.getString("prefLifestyle", "1");
        Boolean lactoseFree = SP.getBoolean("prefLactoseFree", false);

        int i = listMeals.size() - 1;

        while (i >= 0) {

            if (lactoseFree) {
                if (!listMeals.get(i).getBadge().equals("lactose-free")) {
                    listMeals.remove(i);
                }
            } else {
                if (lifeStyle.equals("2")) {
                    if (!listMeals.get(i).getBadge().equals("vegetarian") & !listMeals.get(i).getBadge().equals("vegan")) {
                        listMeals.remove(i);
                    }
                } else if (lifeStyle.equals("3")) {
                    if (!listMeals.get(i).getBadge().equals("vegan")) {
                        listMeals.remove(i);
                    }
                }
            }

            i--;
        }
    }
}