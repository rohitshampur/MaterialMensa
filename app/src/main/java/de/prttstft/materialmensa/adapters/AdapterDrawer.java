package de.prttstft.materialmensa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.prttstft.materialmensa.R;
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
        TextView title;
        ImageView icon;

        public HeaderHolder(View itemView) {
            super(itemView);


        }


    }
}