package de.prttstft.materialmensa.adapters;

import android.content.Context;
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
import de.prttstft.materialmensa.fragments.Constants;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;

public class AdapterToday extends RecyclerView.Adapter<AdapterToday.ViewHolderToday> {

    private ArrayList<Meal> listMeals = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public AdapterToday(Context context) {
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
        View view = layoutInflater.inflate(R.layout.custom_today, parent, false);
        ViewHolderToday viewHolder = new ViewHolderToday(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolderToday holder, int position) {
        Meal currentMeal = listMeals.get(position);
        holder.meal_name.setText(currentMeal.getTitle());
        holder.meal_price.setText(currentMeal.getTitle());
        holder.meal_contents.setText(currentMeal.getReleaseDateTheater().toString());
        String urlThumbnail = currentMeal.getUrlThumbnail();
        loadImages(urlThumbnail, holder);



    }

    private void loadImages(String urlThumbnail, final ViewHolderToday holder) {
        if (!urlThumbnail.equals(Constants.NA)) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
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

    }

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