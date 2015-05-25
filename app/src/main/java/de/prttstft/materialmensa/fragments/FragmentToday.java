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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.adapters.AdapterToday;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.UrlEndpoints;
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

    public static final String URL_ROTTEN_TOMATOES_BOX_OFFICE = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
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

    public static String getRequestUrl(int limit) {
        if (ActivityMain.mensaID == 1) {
            return UrlEndpoints.URL_UPCOMING
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_QUESTION
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_KOTTEN_TOMATOES
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_AMEPERSAND
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_LIMIT + limit;
        } else {
            return de.prttstft.materialmensa.extras.UrlEndpoints.URL_BOX_OFFICE
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_QUESTION
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_KOTTEN_TOMATOES
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_AMEPERSAND
                    + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_LIMIT + limit;
        }
    }

    public FragmentToday() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        sendJsonRequest();

    }

    private void sendJsonRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(30),
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

    public void refreshJson() {
        sendJsonRequest();
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

    private ArrayList<Meal> parseJSONResponse(JSONObject response) {
        ArrayList<Meal> listMeals = new ArrayList<>();
        if (response != null || response.length() > 0) {

            try {
                StringBuilder data = new StringBuilder();
                JSONArray arrayMeals = response.getJSONArray(KEY_MENU);
                for (int i = 0; i < arrayMeals.length(); i++) {

                    String name = Constants.NA;
                    String price_students = Constants.NA;
                    String price_staff = Constants.NA;
                    String price_guests = Constants.NA;
                    String allergens = Constants.NA;
                    int badge;

                    JSONObject currentMeal = arrayMeals.getJSONObject(i);
                    // Get Name
                    if (currentMeal.has(KEY_NAME) && !currentMeal.isNull(KEY_NAME)) {
                        name = currentMeal.getString(KEY_NAME);
                    }

                    // Get Price for Students
                    if (currentMeal.has(KEY_PRICES) && !currentMeal.isNull(KEY_PRICES)) {
                        JSONObject objectPrices = currentMeal.getJSONObject(KEY_PRICES);

                        if (objectPrices != null && objectPrices.has(KEY_STUDENTS) && !objectPrices.isNull(KEY_STUDENTS)) {
                            price_students = objectPrices.getString(KEY_STUDENTS);
                        }
                    }


                        // Get Price for Staff
                    if (currentMeal.has(KEY_PRICES) && !currentMeal.isNull(KEY_PRICES)) {
                        JSONObject objectPrices = currentMeal.getJSONObject(KEY_PRICES);

                        if (objectPrices != null && objectPrices.has(KEY_STAFF) && !objectPrices.isNull(KEY_STAFF)) {
                            price_staff = objectPrices.getString(KEY_STAFF);
                        }
                    }

                    // Get Price for Guests
                    if (currentMeal.has(KEY_PRICES) && !currentMeal.isNull(KEY_PRICES)) {
                        JSONObject objectPrices = currentMeal.getJSONObject(KEY_PRICES);

                        if (objectPrices != null && objectPrices.has(KEY_GUESTS) && !objectPrices.isNull(KEY_GUESTS)) {
                            price_guests = objectPrices.getString(KEY_GUESTS);
                        }
                    }

                    /*// Get Allergens
                    JSONObject objectRatings = currentMeal.getJSONObject(KEY_RATINGS);

                    if (objectRatings.has(KEY_AUDIENCE_SCORE) && !objectRatings.isNull(KEY_RATINGS)) {
                        if (objectRatings != null && objectRatings.has(KEY_AUDIENCE_SCORE) && !objectRatings.isNull(KEY_AUDIENCE_SCORE)) {
                            audienceScore = objectRatings.getInt((KEY_AUDIENCE_SCORE));
                        }
                    }

                    // Get Synopsis
                    if (currentMeal.has(KEY_SYNOPSIS) && !currentMeal.isNull(KEY_SYNOPSIS)) {
                        synopsis = currentMeal.getString(KEY_SYNOPSIS);
                    }*/

                    // Get Badge
                    if (currentMeal.has(KEY_BADGE) && !currentMeal.isNull(KEY_BADGE)) {
                        name = currentMeal.getString(KEY_BADGE);
                    }

                    Meal meal = new Meal();
                    meal.setName(name);
                    meal.setPriceStudents(price_students);
                    meal.setPriceStaff(price_staff);
                    meal.setPriceGuests(price_guests);
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