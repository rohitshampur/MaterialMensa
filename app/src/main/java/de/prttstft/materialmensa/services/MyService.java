package de.prttstft.materialmensa.services;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.prttstft.materialmensa.extras.URLBuilder;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.materialmensa.MyApplication;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

import de.prttstft.materialmensa.json.JSONHelper;

public class MyService extends JobService {


    private RequestQueue requestQueue;


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
            JSONHelper jsonHelper = new JSONHelper();

            JSONArray response = jsonHelper.sendJsonRequest(requestQueue);
            ArrayList<Meal> listMeals = jsonHelper.parseJSONResponse(response, getApplicationContext());

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






}
