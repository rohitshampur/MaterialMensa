package de.prttstft.materialmensa.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.squareup.otto.Bus;

import org.json.JSONArray;

import java.util.ArrayList;

import de.prttstft.materialmensa.events.MealsLoadedEvent;
import de.prttstft.materialmensa.json.JSONHelper;
import de.prttstft.materialmensa.materialmensa.MyApplication;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;

public class MyManualTask extends AsyncTask<Void, Void, ArrayList<Meal>> {
    private RequestQueue requestQueue;
    private Context context;

    public MyManualTask(Context context) {
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.context = context;
        Bus bus = MyApplication.getBus();
    }

    @Override
    protected ArrayList<Meal> doInBackground(Void... voids) {
        JSONHelper jsonHelper = new JSONHelper();

        JSONArray response = jsonHelper.sendJsonRequest(requestQueue);
        ArrayList<Meal> listMeals = jsonHelper.parseJSONResponse(response, context);

        MyApplication.getWritableDatabase().insertMeals(listMeals, false, context);
        return listMeals;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Meal> meals) {
        MyApplication.getBus().post(new MealsLoadedEvent(meals));
    }
}