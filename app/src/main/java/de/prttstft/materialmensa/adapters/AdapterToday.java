package de.prttstft.materialmensa.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.logging.L;
import de.prttstft.materialmensa.network.VolleySingleton;
import de.prttstft.materialmensa.pojo.Meal;

public class AdapterToday extends RecyclerView.Adapter<AdapterToday.ViewHolderToday> {
//
    private ArrayList<Meal> listMeals = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;
    private Context context;
    //private SparseBooleanArray selectedItems;
    private List<Integer> selectedItems = new ArrayList<Integer>();
    ActionMode mActionMode;


    public AdapterToday(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
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
        return new ViewHolderToday(view);

    }

    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = new MenuInflater(context);
            menuInflater.inflate(R.menu.menu_cam, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.favorite) {
                L.t(context, "Favorite");
                return true;
            } else if (menuItem.getItemId() == R.id.share) {
                L.t(context, "Share");
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            clearSelections();
            clearSelectedItems();
            mActionMode = null;
        }
    };


    @Override
    public void onBindViewHolder(final ViewHolderToday holder, int position) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String personCategory = SP.getString("prefPersonCategory", "1");
        Meal currentMeal = listMeals.get(position);

        holder.meal_name.setText(currentMeal.getName());
        holder.meal_typeicon.setImageResource(currentMeal.getBadgeIcon());
        holder.meal_price.setText(currentMeal.getPriceOutput());

        if (!currentMeal.getAllergens().toString().equals("[]")) {
            String allergenreturn = "Allergens & Additives:\n";
            List<String> allergenarray = currentMeal.getAllergens();
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

    @Override
    public int getItemCount() {
        return listMeals.size();
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void addSelectedItem(int pos) {
        selectedItems.add(pos);
    }

    public void removeSelectedItem(int pos) {
        for (int i = 0; i < getItemCount(); i++) {
            if (selectedItems.get(i).equals(pos))
                selectedItems.remove(i);
        }
    }

    public List<Integer> getSelectedItems() {
        return selectedItems;
    }

    public int getSelectedItemsSize() {
        return selectedItems.size();
    }

    public void clearSelectedItems() {
        RelativeLayout meal_item;
        TextView meal_selected;

        for (int i = selectedItems.size() - 1; i >= 0; i--) {
            selectedItems.remove(i);
        }
    }


    class ViewHolderToday extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView meal_typeicon;
        private TextView meal_name;
        private TextView meal_price;
        private TextView meal_contents_spelledout;
        private RelativeLayout meal_item;
        private TextView meal_contents;
        private TextView meal_selected;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String pC = SP.getString("prefPersonCategory", "1");
        String ls = SP.getString("prefLifestyle", "1");
        Boolean lactoseFree = SP.getBoolean("prefLactoseFree", false);

        public ViewHolderToday(View itemView) {
            super(itemView);
            meal_item = (RelativeLayout) itemView.findViewById(R.id.meal_item);
            meal_item.setOnClickListener(this);
            meal_item.setOnLongClickListener(this);
            meal_selected = (TextView) itemView.findViewById(R.id.meal_selected);
            meal_typeicon = (ImageView) itemView.findViewById(R.id.meal_typeicon);
            meal_name = (TextView) itemView.findViewById(R.id.meal_name);
            meal_price = (TextView) itemView.findViewById(R.id.meal_price);
            meal_contents = (TextView) itemView.findViewById(R.id.meal_contents);
            meal_contents_spelledout = (TextView) itemView.findViewById(R.id.meal_contents_spelledout);
        }


        @Override
        public void onClick(View view) {
            if (mActionMode != null) {
                toggleSelection(getLayoutPosition());
                if (getSelectedItemsSize() == 0) {
                    mActionMode.finish();
                }

            } /*else {
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
            }*/
        }

        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            } else {
                clearSelections();
                mActionMode = ((ActivityMain) context).startActionMode(callback);
                toggleSelection(getLayoutPosition());
                notifyItemChanged(getLayoutPosition());
                return true;
            }
        }

        public void toggleSelection(int pos) {
            if (meal_selected.getText().equals("false")) {
                meal_item.setBackgroundResource(R.drawable.custom_bg_selected);
                meal_selected.setText("true");
                addSelectedItem(pos);
                notifyItemChanged(getLayoutPosition());

            } else if (meal_selected.getText().equals("true")) {
                meal_item.setBackgroundResource(R.drawable.custom_bg);
                meal_selected.setText("false");
                removeSelectedItem(pos);
                notifyItemChanged(getLayoutPosition());
            }
            mActionMode.setTitle(String.valueOf(getSelectedItemsSize()));
        }

        public void clearSelections() {
            for (int i = 0; i < getItemCount(); i++) {
                if (meal_selected.getText().equals("true")) {
                    meal_item.setBackgroundResource(R.drawable.custom_bg);
                    meal_selected.setText("false");
                    notifyItemChanged(getLayoutPosition());
                }
            }
        }
    }
}