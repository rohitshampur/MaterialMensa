package de.prttstft.materialmensa.services;

import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.extras.MealSorter;
import de.prttstft.materialmensa.extras.URLBuilder;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.materialmensa.MyApplication;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_ALLERGENS;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_BADGE;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_CATEGORY;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_GUESTS;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_NAME_DE;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_NAME_EN;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_STAFF;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_STUDENTS;
import static de.prttstft.materialmensa.extras.Keys.EndpointToday.KEY_TARA;

public class MyService extends JobService {

    private RequestQueue requestQueue;
    private MealSorter mSorter = new MealSorter();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "onStartJob");
        new MyTask(this).execute(jobParameters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        L.t(this, "onStopJob");
        return false;
    }

    public class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {
        MyService myService;


        MyTask(MyService myService) {
            this.myService = myService;
            VolleySingleton volleySingleton = VolleySingleton.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            JSONArray response = sendJsonRequest();
            ArrayList<Meal> listMeals = parseJSONResponse(response);
            MyApplication.getWritableDatabase().insertMeals(listMeals, false, getApplicationContext());
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters, false);
        }
    }

    /*
    public class MyManualTask extends AsyncTask<Void, Void, ArrayList<Meal>> {

        MyManualTask() {
            VolleySingleton volleySingleton = VolleySingleton.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected ArrayList<Meal> doInBackground(Void... voids) {
            JSONArray response = sendJsonRequest();
            ArrayList<Meal> listMeals = parseJSONResponse(response);
            MyApplication.getWritableDatabase().insertMeals(setUpAndFilterMealList(listMeals), false, getApplicationContext());

            return listMeals;
        }

        @Override
        protected void onPostExecute(ArrayList<Meal> meals) {
            super.onPostExecute(meals);
        }
    }
    */

    private JSONArray sendJsonRequest() {
        JSONArray response = null;
        RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();

            /*
            listMeals = parseJSONResponse(response);
                                        dbHandler.insertMeals(listMeals, false);
                                        adapter.setMealList(setUpAndFilterMealList(dbHandler.getAllMeals()));
             */


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                URLBuilder.getRequestUrl(),
                (String) null, requestFuture, requestFuture);

        requestQueue.add(request);
        try

        {
            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException |
                TimeoutException e
                )

        {
            L.m(e + "");
        }

        return response;
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
                    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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




    public String formatCurrency(String prize) {
        double prizeDouble = Double.valueOf(prize);

        Locale german = new Locale("de", "DE");
        NumberFormat format = NumberFormat.getCurrencyInstance(german);

        return format.format(prizeDouble);
    }

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


}
