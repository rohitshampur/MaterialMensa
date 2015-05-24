package de.prttstft.materialmensa.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.adapters.AdapterToday;
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

    public static String getRequestUrl(int limit) {
        return de.prttstft.materialmensa.extras.UrlEndpoints.URL_BOX_OFFICE
                + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_QUESTION
                + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_KOTTEN_TOMATOES
                + de.prttstft.materialmensa.extras.UrlEndpoints.URL_CHAR_AMEPERSAND
                + de.prttstft.materialmensa.extras.UrlEndpoints.URL_PARAM_LIMIT + limit;
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
                        listMeals = parseJSONResponse(response);
                        adapterToday.setMealList(listMeals);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private ArrayList<Meal> parseJSONResponse(JSONObject response) {
        ArrayList<Meal> listMeals = new ArrayList<>();
        if (response != null || response.length() > 0) {

            try {
                StringBuilder data = new StringBuilder();
                JSONArray arrayMeals = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < arrayMeals.length(); i++) {

                    long id = 0;
                    String title = Constants.NA;
                    int audienceScore = -1;
                    String releaseDate = Constants.NA;
                    String synopsis = Constants.NA;
                    String urlThumbnail = Constants.NA;

                    JSONObject currentMeal = arrayMeals.getJSONObject(i);
                    // Get ID
                    if (currentMeal.has(KEY_ID) && !currentMeal.isNull(KEY_ID)) {
                        id = currentMeal.getLong(KEY_ID);
                    }

                    // Get Title
                    if ((currentMeal.has(KEY_TITLE) && !currentMeal.isNull(KEY_TITLE))) {
                        title = currentMeal.getString(KEY_TITLE);
                    }

                    // Get Releasedate
                    if (currentMeal.has(KEY_RELEASE_DATES) && !currentMeal.isNull(KEY_RELEASE_DATES)) {
                        JSONObject objectReleaseDates = currentMeal.getJSONObject(KEY_RELEASE_DATES);

                        if (objectReleaseDates != null && objectReleaseDates.has(KEY_THEATER) && !objectReleaseDates.isNull(KEY_THEATER)) {
                            releaseDate = objectReleaseDates.getString(KEY_THEATER);
                        }
                    }

                    // Get Score
                    JSONObject objectRatings = currentMeal.getJSONObject(KEY_RATINGS);

                    if (objectRatings.has(KEY_AUDIENCE_SCORE) && !objectRatings.isNull(KEY_RATINGS)) {
                        if (objectRatings != null && objectRatings.has(KEY_AUDIENCE_SCORE) && !objectRatings.isNull(KEY_AUDIENCE_SCORE)) {
                            audienceScore = objectRatings.getInt((KEY_AUDIENCE_SCORE));
                        }
                    }

                    // Get Synopsis
                    if (currentMeal.has(KEY_SYNOPSIS) && !currentMeal.isNull(KEY_SYNOPSIS)) {
                        synopsis = currentMeal.getString(KEY_SYNOPSIS);
                    }

                    // Get Thumbnail
                    if (currentMeal.has(KEY_POSTERS) && !currentMeal.isNull(KEY_POSTERS)) {
                        JSONObject objectPosters = currentMeal.getJSONObject(KEY_POSTERS);

                        if (objectPosters != null && objectPosters.has(KEY_THUMBNAIL) && !objectPosters.isNull(KEY_THUMBNAIL)) {
                            urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                    }}


                    Meal movie = new Meal();
                    movie.setId(id);
                    movie.setTitle(title);
                    Date date = null;
                    try {
                        date = dateFormat.parse(releaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    movie.setReleaseDateTheater(date);
                    movie.setAudienceScore(audienceScore);
                    movie.setSynopsis(synopsis);
                    movie.setUrlThumbnail(urlThumbnail);

                    if (id!=-1 && !title.equals(Constants.NA)) {
                        listMeals.add(movie);
                    }

                }

                //L.T(getActivity(), listMeals.toString());

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
//        return inflater.inflate(R.layout.fragment_today, container, false);
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        listToday = (RecyclerView) view.findViewById(R.id.listToday);
        listToday.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterToday = new AdapterToday(getActivity());
        listToday.setAdapter(adapterToday);
        sendJsonRequest();
        return view;
    }


}