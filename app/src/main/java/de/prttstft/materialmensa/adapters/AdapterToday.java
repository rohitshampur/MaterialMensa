package de.prttstft.materialmensa.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.extras.Constants;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;

public class AdapterToday extends RecyclerView.Adapter<AdapterToday.ViewHolderToday> {

    private ArrayList<Meal> listMeals = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private Context context;


    public AdapterToday(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMealList(ArrayList<Meal> listMeals) {
        this.listMeals = listMeals;
        notifyItemRangeChanged(0, listMeals.size());

    }

    @Override
    public ViewHolderToday onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_today_items, parent, false);
        ViewHolderToday viewHolder = new ViewHolderToday(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(final ViewHolderToday holder, int position) {
        Meal currentMeal = listMeals.get(position);
        holder.meal_name.setText(currentMeal.getName());

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String personCategory = SP.getString("prefPersonCategory", "1");
        String lifeStyle = SP.getString("prefLifestyle", "1");

        if (personCategory != null) {
            if (personCategory.equals("2")) {
                holder.meal_price.setText(currentMeal.getPriceStudents());
            } else {
                holder.meal_price.setText(currentMeal.getPrices());
            }
        }
        if (!currentMeal.getAllergens().equals("[]")) {
            holder.meal_contents.setText(currentMeal.getAllergens());
        } else {
            holder.meal_contents.setText("Keine Allergene");
        }

        if (currentMeal.getBadge().equals("vegetarian")) {
            holder.meal_typeicon.setImageResource(R.drawable.ic_vegeterian);
        } else if (currentMeal.getBadge().equals("vegan")) {
            holder.meal_typeicon.setImageResource(R.drawable.ic_vegan);
        }

    }


    /*private void loadImages(String typeIcon, final ViewHolderToday holder) {
        if (!typeIcon.equals(Constants.NA)) {

            if (current)

            holder.meal_typeicon.setImageResource(R.drawable.ic_ndrawer_icon2);
        }*/
            /*imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.meal_typeicon.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
//                    holder.meal_typeicon.setImageBitmap();
                }
            });
        }

    }*/

    @Override
    public int getItemCount() {
        return listMeals.size();
    }

    static class ViewHolderToday extends RecyclerView.ViewHolder {
        private ImageView meal_typeicon;
        private TextView meal_name;
        private TextView meal_price;
        private TextView meal_contents;

        public ViewHolderToday(View itemView) {
            super(itemView);
            meal_typeicon = (ImageView) itemView.findViewById(R.id.meal_typeicon);
            meal_name = (TextView) itemView.findViewById(R.id.meal_name);
            meal_price = (TextView) itemView.findViewById(R.id.meal_price);
            meal_contents = (TextView) itemView.findViewById(R.id.meal_contents);

        }
    }
}