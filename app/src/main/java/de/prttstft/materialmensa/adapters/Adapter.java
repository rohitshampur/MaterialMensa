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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.pojo.Meal;

public class Adapter extends SelectableAdapter<Adapter.ViewHolder> {

    private ArrayList<Meal> items = new ArrayList<>();

    private Context context;
    private ViewHolder.ClickListener clickListener;
    List<String> selectedMealNameList = new ArrayList<String>();


    public Adapter(ViewHolder.ClickListener clickListener) {
        super();
        this.clickListener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_today_items, parent, false);
        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Meal currentMeal = items.get(position);

        holder.meal_name.setText(currentMeal.getName());
        holder.meal_typeicon.setImageResource(currentMeal.getBadgeIcon());
        holder.meal_price.setText(currentMeal.getPriceOutput());

        holder.meal_item.setBackgroundResource(isSelected(position) ? R.drawable.custom_bg_selected : R.drawable.custom_bg);

        if (!currentMeal.getAllergens().toString().equals("[]")) {
            String allergenreturn = "Allergens & Additives:\n";
            List<String> allergensSpelledOutarray = currentMeal.getAllergensSpelledOut();

            for (int i = 0; i < allergensSpelledOutarray.size(); i++) {
                allergenreturn = allergenreturn + "\n- " + allergensSpelledOutarray.get(i);
            }

            holder.meal_contents.setText(currentMeal.getAllergens().toString());
            holder.meal_contents_spelledout.setText(allergenreturn);
           // getSelectedMealNames();

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

        private ClickListener listener;

        private TextView meal_name;
        private TextView meal_price;
        private TextView meal_contents;
        private TextView meal_contents_spelledout;
        private ImageView meal_typeicon;


        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            meal_item = (RelativeLayout) itemView.findViewById(R.id.meal_item);

            this.listener = listener;

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
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }

            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position);

            boolean onItemLongClicked(int position);
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

    public void getSelectedMealNames() {
        Meal currentM;
        Set<String> selectedMealsSet = new HashSet<String>();

        for (int i = 0; i < items.size(); i++) {
            currentM = items.get(i);
            if (isSelected(i)) {
                if (!selectedMealsSet.contains(currentM.getName()))
                    selectedMealsSet.add(currentM.getName());
            }
        }
        if (!selectedMealsSet.isEmpty()) {
            selectedMealNameList.addAll(selectedMealsSet);
        }
    }

    public void emptySelectedMealNameList() {
        selectedMealNameList = new ArrayList<String>();
    }

    public String buildSelectedMealNamesString() {
        getSelectedMealNames();
        String selectedMealsString = "";
        if (!selectedMealNameList.isEmpty()) {
            for (int i = 0; i < selectedMealNameList.size(); i++) {
                selectedMealsString = selectedMealsString + selectedMealNameList.get(i) + "\n";
            }
            return selectedMealsString;
        }
        //ToDo: Strings!
        return "No Meals Selected";
    }


}