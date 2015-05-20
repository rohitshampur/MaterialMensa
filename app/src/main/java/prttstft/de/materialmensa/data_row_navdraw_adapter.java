package prttstft.de.materialmensa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Max on 21.05.2015.
 */
public class data_row_navdraw_adapter extends RecyclerView.Adapter<data_row_navdraw_adapter.MyViewHolder> {

    private final LayoutInflater infalter;

    public data_row_navdraw_adapter(Context context) {
        infalter = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = infalter.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);


        }
    }
}
