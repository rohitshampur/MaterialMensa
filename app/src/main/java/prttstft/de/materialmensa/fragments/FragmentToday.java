package prttstft.de.materialmensa.fragments;


import android.graphics.Movie;
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
import java.util.List;

import prttstft.de.materialmensa.R;
import prttstft.de.materialmensa.adapters.data_row_meals_adapter;
import prttstft.de.materialmensa.extras.Keys;
import prttstft.de.materialmensa.logging.L;
import prttstft.de.materialmensa.materialmensa.MyApplication;
import prttstft.de.materialmensa.network.VolleySingleton;
import prttstft.de.materialmensa.pojo.Meal;
import prttstft.de.materialmensa.views.data_row_meals;

import static prttstft.de.materialmensa.extras.Keys.EndpointToday.*;

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
        return prttstft.de.materialmensa.extras.UrlEndpoints.URL_BOX_OFFICE
                + prttstft.de.materialmensa.extras.UrlEndpoints.URL_CHAR_QUESTION
                + prttstft.de.materialmensa.extras.UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_KOTTEN_TOMATOES
                + prttstft.de.materialmensa.extras.UrlEndpoints.URL_CHAR_AMEPERSAND
                + prttstft.de.materialmensa.extras.UrlEndpoints.URL_PARAM_LIMIT + limit;
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
                getRequestUrl(10),
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSONResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private void parseJSONResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            return;
        }

        try {
            StringBuilder data = new StringBuilder();
            JSONArray arrayMeals = response.getJSONArray(KEY_MOVIES);
            for (int i = 0; i < arrayMeals.length(); i++) {

                JSONObject currentMeal = arrayMeals.getJSONObject(i);
                // Get ID
                long id = currentMeal.getLong(KEY_ID);
                // Get Title
                String title = currentMeal.getString(KEY_TITLE);
                // Get Releasedate
                JSONObject objectReleaseDates = currentMeal.getJSONObject(KEY_RELEASE_DATES);
                String  releaseDate = null;
                if (objectReleaseDates.has(KEY_THEATER)) {
                    releaseDate = objectReleaseDates.getString(KEY_THEATER);
                } else {
                    releaseDate = "N/A";
                }

                // Get Score
                JSONObject objectRatings = currentMeal.getJSONObject(KEY_RATINGS);
                int audienceScore = -1;
                if (objectRatings.has(KEY_AUDIENCE_SCORE)) {
                    audienceScore = objectRatings.getInt((KEY_AUDIENCE_SCORE));
                }

                // Get Synopsis
                String synopsis = currentMeal.getString(KEY_SYNOPSIS);

                // Get Thumbnail
                JSONObject objectPosters = currentMeal.getJSONObject(KEY_POSTERS);
                String urlThumbnail = null;
                if (objectPosters.has(KEY_POSTERS)) {
                    urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                }



                Meal movie = new Meal();
                movie.setId(id);
                movie.setTitle(title);
                Date date = dateFormat.parse(releaseDate);
                movie.setReleaseDateTheater(date);
                movie.setAudienceScore(audienceScore);
                movie.setSynopsis(synopsis);
                movie.setUrlThumbnail(urlThumbnail);

                listMeals.add(movie);

            }

            //L.T(getActivity(), listMeals.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_today, container, false);
        View view = inflater.inflate(R.layout.fragment_today,container,false);
        listToday = (RecyclerView) view.findViewById(R.id.listToday);
        listToday.setLayoutManager(new LinearLayoutManager(getActivity()));
        // sendJsonRequest();
        return view;
    }


}
