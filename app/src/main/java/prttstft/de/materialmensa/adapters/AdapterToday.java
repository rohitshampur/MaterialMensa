package prttstft.de.materialmensa.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import prttstft.de.materialmensa.R;
import prttstft.de.materialmensa.pojo.Meal;

public class AdapterToday extends RecyclerView.Adapter<AdapterToday.ViewHolderToday> {

    private ArrayList<Meal> listMeals = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public AdapterToday(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setMealList(ArrayList<Meal> listMeals) {
        this.listMeals = listMeals;
        notifyItemRangeChanged(0, listMeals.size());
    }

    @Override
    public ViewHolderToday onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.meal_custom_row, parent, false);
        ViewHolderToday viewHolder = new ViewHolderToday(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolderToday holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
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
