package de.prttstft.materialmensa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class AdapterToday extends RecyclerView.Adapter<AdapterToday.ViewHolder> {

    private ArrayList<Meal> listMeals = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterToday(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_today_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meal currentMeal = listMeals.get(position);

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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView meal_typeicon;
        private TextView meal_name;
        private TextView meal_price;
        private TextView meal_contents_spelledout;
        private RelativeLayout meal_item;
        private TextView meal_contents;

        public ViewHolder(View itemView) {
            super(itemView);
            meal_item = (RelativeLayout) itemView.findViewById(R.id.meal_item);
            meal_item.setOnClickListener(this);
            meal_item.setOnLongClickListener(this);
            meal_typeicon = (ImageView) itemView.findViewById(R.id.meal_typeicon);
            meal_name = (TextView) itemView.findViewById(R.id.meal_name);
            meal_price = (TextView) itemView.findViewById(R.id.meal_price);
            meal_contents = (TextView) itemView.findViewById(R.id.meal_contents);
            meal_contents_spelledout = (TextView) itemView.findViewById(R.id.meal_contents_spelledout);
        }


        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return listMeals.size();
    }

    public void setMealList(ArrayList<Meal> listMeals) {
        this.listMeals = listMeals;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, listMeals.size());
    }
}