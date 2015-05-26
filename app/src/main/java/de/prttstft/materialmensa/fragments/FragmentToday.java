package de.prttstft.materialmensa.fragments;


import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.adapters.AdapterToday;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.UrlEndpoints;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.materialmensa.MyApplication;
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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentToday.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentToday newInstance(String param1, String param2) {

        FragmentToday fragment = new FragmentToday();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MEAL, listMeals);

    }

    public static String getRequestUrl() {
        //if (ActivityMain.mensaID == 1) {
        return UrlEndpoints.URL_API
                    + UrlEndpoints.URL_CHAR_AMEPERSAND
                    + UrlEndpoints.URL_RESTAURANT_ACADEMICA
                    + UrlEndpoints.URL_PARAM_DATE
                    ;
                    /*+ de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_QUESTION
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_KOTTEN_TOMATOES
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_AMEPERSAND
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_LIMIT + limit;*/
        /*} else {
            return de.prttstft.materialmensa.extras.UrlEndpoints.URL_BOX_OFFICE
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_QUESTION
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_KOTTEN_TOMATOES
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_AMEPERSAND
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_LIMIT + limit;
        }*/
    }

    public FragmentToday() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

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
                    String menu = "Ficken";
                    String name = Constants.NA;
                    String category = Constants.NA;
                    String type = Constants.NA;
                    Boolean tara = false;
                    String price_students = Constants.NA;
                    String price_staff = Constants.NA;
                    String price_guests = Constants.NA;
                    List<String> allergens = new ArrayList<String>();
                    int badge = 0;

                    //L.t(getActivity(), String.valueOf(i));

                    JSONObject objectMeals = response.getJSONObject(i);

                    //L.t(getActivity(), objectMeals.toString());

                    // Get Name
                    if (objectMeals.has(KEY_MENU) && !objectMeals.isNull(KEY_MENU)) {

                        JSONObject currentMeal = objectMeals.getJSONObject(KEY_MENU);

                        if (currentMeal.has(KEY_NAME) && !currentMeal.isNull(KEY_NAME)) {
                            name = currentMeal.getString(KEY_NAME);
                        }

                        if (currentMeal.has(KEY_CATEGORY) && !currentMeal.isNull(KEY_CATEGORY)) {
                            category = currentMeal.getString(KEY_CATEGORY);
                        }

                        if (currentMeal.has(KEY_TYPE) && !currentMeal.isNull(KEY_TYPE)) {
                            type = currentMeal.getString(KEY_TYPE);
                        }

                        if (currentMeal.has(KEY_PRICES) && !currentMeal.isNull(KEY_PRICES)) {
                            JSONObject objectPrices = currentMeal.getJSONObject(KEY_PRICES);

                            if (objectPrices.has(KEY_TARA) && !objectPrices.isNull(KEY_TARA)) {
                                tara = objectPrices.getBoolean(KEY_TARA);
                            }

                            if (objectPrices.has(KEY_STUDENTS) && !objectPrices.isNull(KEY_STUDENTS)) {
                                price_students = objectPrices.getString(KEY_STUDENTS);
                            }

                            if (objectPrices.has(KEY_STAFF) && !objectPrices.isNull(KEY_STAFF)) {
                                price_staff = objectPrices.getString(KEY_STAFF);

                            }

                            if (objectPrices.has(KEY_GUESTS) && !objectPrices.isNull(KEY_GUESTS)) {
                                price_guests = objectPrices.getString(KEY_GUESTS);
                            }

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
                                badge = arrayBadge.getInt(0);
                           }

                        }


                    }

                    Meal meal = new Meal();
                    meal.setName(name);
                    meal.setPriceStudents(price_students);
                    meal.setPriceStaff(price_staff);
                    meal.setPriceGuests(price_guests);
                    meal.setAllergens(allergens);
                    meal.setBadge(badge);


                    if (!name.equals(Constants.NA)) {

                        listMeals.add(meal);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            adapterToday.setMealList(listMeals);
        } else {
            sendJsonRequest();
        }


        return view;
    }

}