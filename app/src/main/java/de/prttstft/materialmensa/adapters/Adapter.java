package de.prttstft.materialmensa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.pojo.Meal;

public class Adapter extends SelectableAdapter<Adapter.ViewHolder> {

    private ArrayList<Meal> items = new ArrayList<>();

    private Context context;

    public Adapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_today_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meal currentMeal = items.get(position);

        holder.meal_name.setText(currentMeal.getName());
        holder.meal_typeicon.setImageResource(currentMeal.getBadgeIcon());
        holder.meal_price.setText(currentMeal.getPriceOutput());

        if (!currentMeal.getAllergens().toString().equals("[]")) {
            String allergenreturn = "Allergens & Additives:\n";
            List<String> allergensSpelledOutarray = currentMeal.getAllergensSpelledOut();

            for (int i = 0; i < allergensSpelledOutarray.size(); i++) {
                allergenreturn = allergenreturn + "\n- " + allergensSpelledOutarray.get(i);
            }
            holder.meal_contents.setText(currentMeal.getAllergens().toString());
            holder.meal_contents_spelledout.setText(allergenreturn);
        } else {
            if (Locale.getDefault().getISO3Language().equals("deu")) {
                holder.meal_contents.setText("Keine Allergene oder Zusatzstoffe");
            } else {
                holder.meal_contents.setText("No Allergens or Additives");
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @SuppressWarnings("unused")
        private static final String TAG = ViewHolder.class.getSimpleName();

        private RelativeLayout meal_item;

        private TextView meal_name;
        private TextView meal_price;
        private TextView meal_contents;
        private TextView meal_contents_spelledout;
        private ImageView meal_typeicon;

        public ViewHolder(View itemView) {
            super(itemView);
            meal_item = (RelativeLayout) itemView.findViewById(R.id.meal_item);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            meal_name = (TextView) itemView.findViewById(R.id.meal_name);
            meal_price = (TextView) itemView.findViewById(R.id.meal_price);
            meal_contents = (TextView) itemView.findViewById(R.id.meal_contents);
            meal_contents_spelledout = (TextView) itemView.findViewById(R.id.meal_contents_spelledout);
            meal_typeicon = (ImageView) itemView.findViewById(R.id.meal_typeicon);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Item clicked at position " + getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "Item long-clicked at position " + getAdapterPosition());
            return true;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setMealList(ArrayList<Meal> listMeals) {
        this.items = listMeals;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, listMeals.size());
    }
}