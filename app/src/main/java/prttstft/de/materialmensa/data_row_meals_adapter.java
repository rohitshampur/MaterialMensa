package prttstft.de.materialmensa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Max on 21.05.2015.
 */

public class data_row_meals_adapter extends RecyclerView.Adapter {
    private List<data_row_meals> dataItems;

    // Adapter constructor
    public data_row_meals_adapter(List<data_row_meals> dataItems) {
        this.dataItems = dataItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meal_custom_row, null);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        data_row_meals dataItem = dataItems.get(position);
        //Casting the viewHolder to MyViewHolder so I could interact with the views
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.meal_name.setText("My Test Meal");
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    // MyViewHolder Class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView meal_name;

        public MyViewHolder(View itemView) {
            super(itemView); // Musst call super() first
            meal_name = (TextView) itemView.findViewById(R.id.meal_name);
        }
    }
}


