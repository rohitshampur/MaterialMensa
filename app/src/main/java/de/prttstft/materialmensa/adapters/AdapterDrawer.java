package de.prttstft.materialmensa.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.prttstft.materialmensa.R;
import de.prttstft.materialmensa.activities.ActivityAbout;
import de.prttstft.materialmensa.activities.ActivityMain;
import de.prttstft.materialmensa.views.Drawer;


public class AdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Drawer> data = Collections.emptyList();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final LayoutInflater infalter;
    private Context context;

    public AdapterDrawer(Context context, List<Drawer> data) {
        this.context = context;
        infalter = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = infalter.inflate(R.layout.fragment_drawer_header, parent, false);
            HeaderHolder holder = new HeaderHolder(view);

            return holder;


        } else {
            View view = infalter.inflate(R.layout.fragment_drawer_items, parent, false);
            ItemHolder holder = new ItemHolder(view);
            return holder;

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;

        } else {
            return TYPE_ITEM;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {


        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            Drawer current = data.get(position - 1);
            itemHolder.title.setText(current.title);
            itemHolder.icon.setImageResource(current.iconId);

            if (ActivityMain.mensaID == 0 & position == 1) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 1 & position == 2) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 2 & position == 3) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 3 & position == 4) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 4 & position == 5) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 5 & position == 6) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 6 & position == 7) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            } else if (ActivityMain.mensaID == 7 & position == 8) {
                itemHolder.title.setTextAppearance(context, R.style.boldText);
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView personCategory;
        TextView lifestyle;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String pC = SP.getString("prefPersonCategory", "1");
        String ls = SP.getString("prefLifestyle", "1");


        public HeaderHolder(View itemView) {
            super(itemView);
            personCategory = (TextView) itemView.findViewById(R.id.person_category);
            lifestyle = (TextView) itemView.findViewById(R.id.lifestyle);

            switch (pC) {
                case "2":
                    personCategory.setText(R.string.Student);
                    break;
                case "3":
                    personCategory.setText(R.string.Staff);
                    break;
                case "4":
                    personCategory.setText(R.string.Guest);
                    break;
            }

            switch (ls) {
                case "1":
                    lifestyle.setText(R.string.NoPreference);
                    break;
                case "2":
                    lifestyle.setText(R.string.Vegetarian);
                    break;
                case "3":
                    lifestyle.setText(R.string.Vegan);
                    break;
            }
        }
    }
}








