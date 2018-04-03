package id.idn.datasiswa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import id.idn.datasiswa.ResponseServer.DataItem;

/**
 * Created by hp on 3/27/2018.
 */

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    List<DataItem> data;
    public static final String WebUrl = "http:// ip komputer masing-masing / nama folder di htdocs";

    public CustomAdapter(Context context, List<DataItem> dataItems) {
        this.context = context;
        this.data = dataItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listitem , parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.gender.setText(data.get(position).getSex());
        holder.hometown.setText(data.get(position).getHometown());
        holder.classs.setText(data.get(position).getJsonMemberClass());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView classs, name, gender, hometown, address;

        public MyViewHolder(View itemView) {
            super(itemView);

            classs = itemView.findViewById(R.id.classs);
            name = itemView.findViewById(R.id.nama);
            address = itemView.findViewById(R.id.address);
            hometown = itemView.findViewById(R.id.hometown);
            gender = itemView.findViewById(R.id.sex);
        }
    }
}
