package de.prttstft.materialmensa.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.prttstft.materialmensa.R;
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

        notifyDataSetChanged();
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
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String personCategory = SP.getString("prefPersonCategory", "1");

        Meal currentMeal = listMeals.get(position);

        holder.meal_name.setText(currentMeal.getName());


        switch (currentMeal.getBadge()) {
            case "vegetarian":
                holder.meal_typeicon.setImageResource(R.drawable.ic_vegeterian);
                break;
            case "vegan":
                holder.meal_typeicon.setImageResource(R.drawable.ic_vegan);
                break;
            case "lactose-free":
                holder.meal_typeicon.setImageResource(R.drawable.ic_lactose_free);
                break;
            case "low-calorie":
                holder.meal_typeicon.setImageResource(R.drawable.ic_low_calorie);
                break;
            case "vital-food":
                holder.meal_typeicon.setImageResource(R.drawable.ic_vital_food);
                break;
            case "nonfat":
                holder.meal_typeicon.setImageResource(R.drawable.ic_nonfat);
                break;
            default:
                holder.meal_typeicon.setVisibility(View.GONE);
                break;
        }

        if (personCategory != null) {
            switch (personCategory) {
                case "2":
                    holder.meal_price.setText(currentMeal.getPriceStudents());
                    break;
                case "3":
                    holder.meal_price.setText(currentMeal.getPriceStaff());
                    break;
                case "4":
                    holder.meal_price.setText(currentMeal.getPriceGuests());
                    break;
                default:
                    if (Locale.getDefault().getISO3Language().equals("deu")) {
                        holder.meal_price.setText(currentMeal.getPricesDe());
                    } else {
                        holder.meal_price.setText(currentMeal.getPrices());
                    }
                    break;
            }
        }


        if (!currentMeal.getAllergens().toString().equals("[]")) {
            String allergenreturn = "Allergens & Additives:\n";
            List<String> allergenarray = currentMeal.getAllergens();
            List<String> allergensSpelledOutarray = currentMeal.getAllergensSpelledOut();

            for (int i = 0; i < allergensSpelledOutarray.size(); i++) {
                allergenreturn = allergenreturn + "\n- " + allergensSpelledOutarray.get(i) + " (" + allergenarray.get(i) + ")";
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

    @Override
    public int getItemCount() {
        return listMeals.size();
    }

    public void delete(int position) {
        listMeals.remove(position);
        notifyItemRemoved(position);
    }

    //static class ViewHolderToday extends RecyclerView.ViewHolder implements View.OnClickListener {
    class ViewHolderToday extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView meal_typeicon;
        private TextView meal_name;
        private TextView meal_price;
        private TextView meal_contents_spelledout;

        private TextView meal_contents;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String pC = SP.getString("prefPersonCategory", "1");
        String ls = SP.getString("prefLifestyle", "1");
        Boolean lactoseFree = SP.getBoolean("prefLactoseFree", false);

        public ViewHolderToday(View itemView) {
            super(itemView);
            meal_typeicon = (ImageView) itemView.findViewById(R.id.meal_typeicon);
            meal_name = (TextView) itemView.findViewById(R.id.meal_name);
            meal_price = (TextView) itemView.findViewById(R.id.meal_price);
            meal_contents = (TextView) itemView.findViewById(R.id.meal_contents);
            meal_contents_spelledout = (TextView) itemView.findViewById(R.id.meal_contents_spelledout);
            meal_contents.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

            final AlertDialog.Builder contentsAlert = new AlertDialog.Builder(context);
            contentsAlert.setMessage(String.valueOf(meal_contents_spelledout.getText())).
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).
                    create();
            contentsAlert.show();

        }

    }

}